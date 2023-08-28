package studio.archetype.rook.instances.slime;

public class SlimeParseException extends Exception {

    public SlimeParseException(String format, Object... values) {
        super(String.format(format, values));
    }
}
