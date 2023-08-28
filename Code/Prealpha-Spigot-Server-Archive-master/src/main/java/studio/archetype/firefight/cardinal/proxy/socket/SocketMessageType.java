package studio.archetype.firefight.cardinal.proxy.socket;

import com.google.gson.*;

import java.lang.reflect.Type;

public enum SocketMessageType {
    MATCH_JOIN,
    ;

    public static class Serializer implements JsonSerializer<SocketMessageType> {
        @Override
        public JsonElement serialize(SocketMessageType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.ordinal());
        }
    }

    public static class Deserializer implements JsonDeserializer<SocketMessageType> {
        @Override
        public SocketMessageType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            int typeInt = json.getAsInt();
            return SocketMessageType.values()[typeInt]; // get by ordinal
        }
    }
}
