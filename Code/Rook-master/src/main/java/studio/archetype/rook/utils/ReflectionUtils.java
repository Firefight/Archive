package studio.archetype.rook.utils;

import studio.archetype.rook.Cardinal;

import java.lang.reflect.Field;

public final class ReflectionUtils {

    public static <E> E merge(E given, E defaultValue) {
        try {
            for(Field f : given.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object val = f.get(given);
                Object def = f.get(defaultValue);
                if(val == null)
                    f.set(given, def);
            }
            return given;
        } catch(IllegalAccessException e) {
            Cardinal.LOGGER.error("Failed to merge objects: " + e.getClass().getSimpleName());
            if(e.getMessage() != null)
                Cardinal.LOGGER.error("\t" + e.getMessage());
            return defaultValue;
        }
    }
}
