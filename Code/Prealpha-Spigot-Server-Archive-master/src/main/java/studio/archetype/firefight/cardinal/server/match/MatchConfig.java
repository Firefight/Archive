package studio.archetype.firefight.cardinal.server.match;

import com.google.gson.Gson;
import studio.archetype.firefight.cardinal.common.util.FileUtils;
import studio.archetype.firefight.cardinal.common.util.MiscUtils;
import studio.archetype.firefight.cardinal.server.Cardinal;
import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Data
public class MatchConfig {
    @Getter
    private Map map = Map.SMALL;

    @Getter
    private String matchId = System.getenv("CARDINAL_MATCH_ID");

    @Getter
    private int matchLength = 10; // minutes

    public static MatchConfig get() {
        if (instance != null) return instance;
        File file = Cardinal.file("match.json");
        Cardinal.info("Loading Match Config at " + file.getPath());

        if(file.exists()) {
            try {
                instance = gson.fromJson(FileUtils.readAll(file), MatchConfig.class);
            } catch(Throwable e) {
                instance = new MatchConfig();
                Cardinal.warn("Failed to load config at " + file.getPath() + ". Using default config.");
            }
        } else instance = new MatchConfig();

        try {
            FileUtils.writeAll(file, gson.toJson(instance));
        } catch(Throwable e) {
            e.printStackTrace();
        }

        return instance;
    }

    public void save() {
        try {
            FileUtils.writeAll(Cardinal.file("match.json"), gson.toJson(instance));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static final Gson gson = MiscUtils.buildGson();
    private static MatchConfig instance;
}
