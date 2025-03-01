package org.bxteam.commons.time;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.function.Predicate;

/**
 * Abstract class for parsing and formatting temporal amounts.
 * <p>
 * You can create new instance using constructor {@link PeriodParser#PeriodParser()} or {@link DurationParser#DurationParser()},
 * or you can use {@link DurationParser#DATE_TIME_UNITS}, {@link DurationParser#TIME_UNITS} and {@link PeriodParser#DATE_UNITS}.
 * If you want to add new units, use {@link #withUnit(String, ChronoUnit)}.
 * <p>
 * Parse methods:
 * <p>
 * - {@link #parse(String)} to parse temporal amount from string.
 * <p>
 * - {@link #format(TemporalAmount)} to format temporal amount to string.
 * <p>
 * Example usage:
 * <pre>{@code
 * TemporalAmountParser<Duration> parser = DurationParser.DATE_TIME_UNITS;
 * Duration duration = parser.parse("1d2h30m");
 * String formatted = parser.format(duration);
 * System.out.println("Parsed duration: " + duration);
 * System.out.println("Formatted duration: " + formatted);
 * }</pre>
 *
 * @param <T> the type of temporal amount (e.g. {@link Duration}, {@link java.time.Period} or subclass of {@link TemporalAmount})
 * @apiNote This class is immutable.
 */
public abstract class TemporalAmountParser<T extends TemporalAmount> {
    private static final Map<ChronoUnit, Long> UNIT_TO_NANO = new LinkedHashMap<>();
    private static final Map<ChronoUnit, Integer> PART_TIME_UNITS = new LinkedHashMap<>();

    private final Set<ChronoUnit> roundedUnits = new HashSet<>();
    private final Map<String, ChronoUnit> units = new LinkedHashMap<>();
    private final LocalDateTimeProvider baseForTimeEstimation;

    static {
        UNIT_TO_NANO.put(ChronoUnit.NANOS, 1L);
        UNIT_TO_NANO.put(ChronoUnit.MICROS, 1_000L);
        UNIT_TO_NANO.put(ChronoUnit.MILLIS, 1_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.SECONDS, 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.MINUTES, 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.HOURS, 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.DAYS, 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.WEEKS, 7 * 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.MONTHS, 30 * 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.YEARS, 365 * 24 * 60 * 60 * 1_000_000_000L);
        UNIT_TO_NANO.put(ChronoUnit.DECADES, 10 * 365 * 24 * 60 * 60 * 1_000_000_000L);

        PART_TIME_UNITS.put(ChronoUnit.NANOS, 1000);
        PART_TIME_UNITS.put(ChronoUnit.MICROS, 1000);
        PART_TIME_UNITS.put(ChronoUnit.MILLIS, 1000);
        PART_TIME_UNITS.put(ChronoUnit.SECONDS, 60);
        PART_TIME_UNITS.put(ChronoUnit.MINUTES, 60);
        PART_TIME_UNITS.put(ChronoUnit.HOURS, 24);
        PART_TIME_UNITS.put(ChronoUnit.DAYS, 7);
        PART_TIME_UNITS.put(ChronoUnit.WEEKS, 4);
        PART_TIME_UNITS.put(ChronoUnit.MONTHS, 12);
        PART_TIME_UNITS.put(ChronoUnit.YEARS, Integer.MAX_VALUE);
    }

    protected TemporalAmountParser(LocalDateTimeProvider baseForTimeEstimation) {
        this.baseForTimeEstimation = baseForTimeEstimation;
    }

    protected TemporalAmountParser(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation) {
        this.baseForTimeEstimation = baseForTimeEstimation;
        this.units.putAll(units);
    }

    public TemporalAmountParser<T> roundOff(ChronoUnit unit) {
        this.roundedUnits.add(unit);
        return this;
    }

    public TemporalAmountParser<T> withUnit(String symbol, ChronoUnit chronoUnit) {
        if (this.units.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol " + symbol + " is already used");
        }

        if (!this.validCharacters(symbol, Character::isLetter)) {
            throw new IllegalArgumentException("Symbol " + symbol + " contains non-letter characters");
        }

        Map<String, ChronoUnit> newUnits = new LinkedHashMap<>(this.units);
        newUnits.put(symbol, chronoUnit);
        return clone(newUnits, this.baseForTimeEstimation);
    }

    public TemporalAmountParser<T> withLocalDateTimeProvider(LocalDateTimeProvider baseForTimeEstimation) {
        return clone(this.units, baseForTimeEstimation);
    }

    protected abstract TemporalAmountParser<T> clone(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation);

    private boolean validCharacters(String content, Predicate<Character> predicate) {
        for (int i = 0; i < content.length(); i++) {
            if (!predicate.test(content.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses the given input string to a temporal amount.
     * <p>
     * The input string should be in the format of <number><unit>, e.g., "1d2h30m".
     *
     * @param input the input string to parse
     * @return the parsed temporal amount
     * @throws IllegalArgumentException if the input string is empty, contains invalid characters, or is not in the correct format
     */
    public T parse(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input is empty");
        }

        T total = this.getZero();
        boolean negative = false;

        StringBuilder number = new StringBuilder();
        StringBuilder unit = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '-') {
                if (i != 0) {
                    throw new IllegalArgumentException("Minus sign is only allowed at the start of the input");
                }
                negative = true;
                continue;
            }

            if (Character.isDigit(c)) {
                number.append(c);
                continue;
            }

            if (Character.isLetter(c)) {
                unit.append(c);
            } else {
                throw new IllegalArgumentException("Invalid character " + c + " in input");
            }

            if (i == input.length() - 1 || Character.isDigit(input.charAt(i + 1))) {
                TemporalEntry temporalEntry = this.parseTemporal(number.toString(), unit.toString());
                total = this.plus(this.baseForTimeEstimation, total, temporalEntry);
                number.setLength(0);
                unit.setLength(0);
            }
        }

        if (number.length() > 0 || unit.length() > 0) {
            throw new IllegalArgumentException("Input is not in the format of <number><unit>");
        }

        if (negative) {
            total = this.negate(total);
        }

        return total;
    }

    protected abstract T plus(LocalDateTimeProvider baseForTimeEstimation, T temporalAmount, TemporalEntry temporalEntry);

    protected abstract T negate(T temporalAmount);

    protected abstract T getZero();

    private TemporalEntry parseTemporal(String number, String unit) {
        if (number.isEmpty()) {
            throw new IllegalArgumentException("Missing number before unit " + unit);
        }

        ChronoUnit chronoUnit = this.units.get(unit);
        if (chronoUnit == null) {
            throw new IllegalArgumentException("Unknown unit " + unit);
        }

        try {
            long count = Long.parseLong(number);
            return new TemporalEntry(count, chronoUnit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number " + number);
        }
    }

    /**
     * Formats the given temporal amount to a string.
     * <p>
     * The output string will be in the format of <number><unit>, e.g., "1d2h30m".
     *
     * @param temporalAmount the temporal amount to format
     * @return the formatted string
     * @throws IllegalArgumentException if the temporal amount contains unsupported units
     */
    public String format(T temporalAmount) {
        StringBuilder builder = new StringBuilder();
        Duration duration = this.toDuration(this.baseForTimeEstimation, temporalAmount);

        if (duration.isNegative()) {
            builder.append('-');
            duration = duration.negated();
        }

        List<String> keys = new ArrayList<>(this.units.keySet());
        Collections.reverse(keys);

        for (String key : keys) {
            ChronoUnit chronoUnit = this.units.get(key);
            if (roundedUnits.contains(chronoUnit)) {
                continue;
            }

            Long part = UNIT_TO_NANO.get(chronoUnit);
            if (part == null) {
                throw new IllegalArgumentException("Unsupported unit " + chronoUnit);
            }

            BigInteger currentCount = this.durationToNano(duration).divide(BigInteger.valueOf(part));
            BigInteger maxCount = BigInteger.valueOf(PART_TIME_UNITS.get(chronoUnit));
            BigInteger count = currentCount.equals(maxCount) ? BigInteger.ONE : currentCount.mod(maxCount);

            if (count.equals(BigInteger.ZERO)) {
                continue;
            }

            builder.append(count).append(key);
            duration = duration.minusNanos(count.longValue() * part);
        }

        return builder.toString();
    }

    protected abstract Duration toDuration(LocalDateTimeProvider baseForTimeEstimation, T temporalAmount);

    public interface LocalDateTimeProvider {
        LocalDateTime get();

        static LocalDateTimeProvider now() {
            return LocalDateTime::now;
        }

        static LocalDateTimeProvider startOfToday() {
            return of(LocalDate.now());
        }

        static LocalDateTimeProvider of(LocalDateTime localDateTime) {
            return () -> localDateTime;
        }

        static LocalDateTimeProvider of(LocalDate localDate) {
            return localDate::atStartOfDay;
        }
    }

    BigInteger durationToNano(Duration duration) {
        return BigInteger.valueOf(duration.getSeconds())
                .multiply(BigInteger.valueOf(1_000_000_000))
                .add(BigInteger.valueOf(duration.getNano()));
    }

    protected record TemporalEntry(long count, ChronoUnit unit) { }
}
