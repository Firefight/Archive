package studio.archetype.rook.utils;

@FunctionalInterface
public interface Serializable<K> {
    K serialize();
}
