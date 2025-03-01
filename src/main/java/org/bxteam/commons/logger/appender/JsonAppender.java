package org.bxteam.commons.logger.appender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bxteam.commons.logger.ExtendedLogger;
import org.bxteam.commons.logger.LogEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Appender that outputs log entries in JSON format.
 */
public class JsonAppender implements Appender {
    private static final ExtendedLogger logger = new ExtendedLogger("JsonAppender");
    private final boolean printToConsole;
    private final boolean printToFile;
    private final String filePath;
    private final Gson gson;

    /**
     * Constructs a JsonAppender.
     *
     * @param prettyPrint    whether to format JSON with indents
     * @param printToConsole whether to print JSON to the console
     * @param printToFile    whether to write JSON to a file
     * @param filePath       the file path for writing logs (used if printToFile is true)
     */
    public JsonAppender(boolean prettyPrint, boolean printToConsole, boolean printToFile, String filePath) {
        this.printToConsole = printToConsole;
        this.printToFile = printToFile;
        this.filePath = filePath;
        GsonBuilder gsonBuilder = new GsonBuilder();

        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }

        gsonBuilder.registerTypeAdapterFactory(new ThrowableTypeAdapterFactory());
        this.gson = gsonBuilder.create();
    }

    /**
     * Converts the log entry to JSON and outputs it to the console and/or file based on configuration.
     *
     * @param logEntry the log entry to append
     */
    @Override
    public void append(LogEntry logEntry) {
        String json = gson.toJson(logEntry);

        if (printToConsole) {
            System.out.println(json);
        }

        if (printToFile) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(json);
                writer.newLine();
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }

    /**
     * Closes the appender. No resources to release in this implementation.
     */
    @Override
    public void close() {
        // No resources to close.
    }

    /**
     * A factory for creating type adapters for Throwable serialization.
     */
    @SuppressWarnings("unchecked")
    static class ThrowableTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!Throwable.class.isAssignableFrom(type.getRawType())) {
                return null;
            }

            return (TypeAdapter<T>) new ThrowableTypeAdapter();
        }
    }

    /**
     * A type adapter for serializing and deserializing Throwable objects to/from JSON.
     */
    static class ThrowableTypeAdapter extends TypeAdapter<Throwable> {
        @Override
        public void write(JsonWriter out, Throwable throwable) throws IOException {
            if (throwable == null) {
                out.nullValue();
                return;
            }

            out.beginObject();
            out.name("type").value(throwable.getClass().getName());
            out.name("message").value(throwable.getMessage());
            out.name("stackTrace");
            out.beginArray();

            for (StackTraceElement element : throwable.getStackTrace()) {
                out.beginObject();
                out.name("className").value(element.getClassName());
                out.name("methodName").value(element.getMethodName());
                out.name("fileName").value(element.getFileName());
                out.name("lineNumber").value(element.getLineNumber());
                out.endObject();
            }

            out.endArray();
            Throwable cause = throwable.getCause();

            if (cause != null && cause != throwable) {
                out.name("cause");
                write(out, cause);
            }

            out.endObject();
        }

        @Override
        public Throwable read(JsonReader in) throws IOException {
            in.beginObject();
            String message = null;
            String type = null;
            Throwable cause = null;
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "type":
                        type = in.nextString();
                        break;
                    case "message":
                        message = in.nextString();
                        break;
                    case "cause":
                        cause = read(in);
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();

            return new Throwable(message, cause);
        }
    }
}
