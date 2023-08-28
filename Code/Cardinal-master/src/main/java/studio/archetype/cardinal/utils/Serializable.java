package studio.archetype.cardinal.utils;

@FunctionalInterface
public interface Serializable<K> {
    K serialize();
}
