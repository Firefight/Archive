package studio.archetype.firefight.cardinal.server;

import com.google.gson.Gson;
import studio.archetype.firefight.cardinal.common.util.FileUtils;
import studio.archetype.firefight.cardinal.common.util.MiscUtils;
import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Data
public class CardinalConfig {
    // TODO Just make variables with default values for config below
    private boolean verbose = false;

    @Getter
    private String nomadApiUrl = null;
    @Getter
    private String nomadApiToken = null;
    @Getter
    private String nomadMatchJobId = "match";

    @Getter
    private String internalSocketAddress = "127.0.0.1";
    @Getter
    private int internalSocketPort = 27019;

    @Getter
    private String mongoUri = "mongodb://localhost:27017";

    @Getter
    private String mongoDatabase = "firefight";

    @Getter
    private boolean match = false;

    private ProjectileConfig projectiles = new ProjectileConfig();

    @Getter
    private int playerNetworkingPort = 27020;

    @Getter
    private String forumEndpoint = "";

    @Data
    public static class ProjectileConfig {
        private double projectileRTSegMeters = 4.46;
    }

    public static CardinalConfig get() {
        if(instance != null) return instance;

        File file = Cardinal.file("config.json");
        Cardinal.info("Loading Config at " + file.getPath());
        try {
            if (file.exists()) {
                Cardinal.info("Config exists, Loading...");
                instance = gson.fromJson(FileUtils.readAll(file), CardinalConfig.class);
            } else {
                Cardinal.info("Config does not exist, creating...");
                instance = new CardinalConfig();
                file.createNewFile();
                instance.save();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return instance;
    }

    public void save() {
        try {
            FileUtils.writeAll(Cardinal.file("config.json"), gson.toJson(this));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static final Gson gson = MiscUtils.buildGson();
    private static CardinalConfig instance;
}