package studio.archetype.cardinal.math;

@SuppressWarnings("SuspiciousNameCombination")
public record Vec2i(int x, int y) {

    public Vec2i add(int x) {
        return add(x, x);
    }

    public Vec2i add(Vec2i vec) {
        return add(vec.x, vec.y);
    }

    public Vec2i add(int x, int y) {
        return new Vec2i(this.x + x, this.y + y);
    }
}
