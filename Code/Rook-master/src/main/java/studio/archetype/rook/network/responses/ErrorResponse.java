package studio.archetype.rook.network.responses;

public class ErrorResponse extends SimpleStringResponse {

    @Override
    public char getIdentifier() { return '-'; }
}
