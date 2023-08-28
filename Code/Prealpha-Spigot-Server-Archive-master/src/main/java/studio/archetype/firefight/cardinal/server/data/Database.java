package studio.archetype.firefight.cardinal.server.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import studio.archetype.firefight.cardinal.server.CardinalConfig;

import java.util.concurrent.TimeUnit;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {
    private static MongoClient client = null;

    public static void connect(String uri) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(pojoCodecRegistry)
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(86400000, TimeUnit.MILLISECONDS);
                })
                .applyConnectionString(new ConnectionString(uri))
                .build();
        client = MongoClients.create(settings);
    }

    public static void connect() {
        System.out.println(CardinalConfig.get().getMongoUri());
        connect(CardinalConfig.get().getMongoUri());
    }

    public static MongoDatabase getDatabase(String name) {
        if (!isConnected()) return null;
        return client.getDatabase(name);
    }

    public static MongoDatabase getDatabase() {
        System.out.println(CardinalConfig.get().getMongoDatabase());
        return getDatabase(CardinalConfig.get().getMongoDatabase());
    }

    public static void disconnect() {
        if (isConnected()) {
            client.close();
            client = null;
        }
    }

    public static boolean isConnected() { return client != null; }
    public static MongoClient getClient() { return client; }
}
