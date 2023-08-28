package studio.archetype.firefight.cardinal.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import studio.archetype.firefight.cardinal.proxy.socket.SocketMessageType;

public interface MiscUtils {
    static Gson buildGson() {
        // TODO: Add serializers for item stacks here
        return new Gson();
    }

    static Gson buildGsonBungee() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SocketMessageType.class, new SocketMessageType.Serializer());
        gsonBuilder.registerTypeAdapter(SocketMessageType.class, new SocketMessageType.Deserializer());

        return gsonBuilder.create();
    }
}
