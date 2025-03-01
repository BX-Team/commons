package org.bxteam.commons.updater;

import com.google.gson.Gson;
import org.apache.maven.artifact.versioning.ComparableVersion;

import java.util.Map;

public class ReposiliteVersionFetcher implements VersionFetcher {
    private final String pluginName;
    private ComparableVersion newestVersion;

    public ReposiliteVersionFetcher(String pluginName) {
        this.pluginName = pluginName;
        this.newestVersion = null;
    }

    @Override
    public ComparableVersion fetchNewestVersion() {
        if (newestVersion != null) return newestVersion;

        String jsonString = VersionFetcher.getDataFromUrl("https://repo.bxteam.org/api/maven/latest/version/releases/org/bxteam/" + pluginName);
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(jsonString, Map.class);
        String versionStr = (String) data.get("version");

        newestVersion = new ComparableVersion(versionStr);
        return newestVersion;
    }

    @Override
    public String getDownloadUrl() {
        return "https://modrinth.com/plugin/" + pluginName;
    }
}
