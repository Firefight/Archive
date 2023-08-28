package studio.archetype.rook.network.responses;

import org.apache.commons.net.MalformedServerReplyException;

import java.nio.ByteBuffer;

public class SimpleStringResponse implements Response<String> {

    @Override
    public char getIdentifier() { return '+'; }

    @Override
    public String parse(ByteBuffer buffer) throws MalformedServerReplyException {
        StringBuilder b = new StringBuilder();
        char next;
        while((next = buffer.getChar()) != '\r' && next != '\n') {
            b.append(next);
        }
        if(next == '\r' && buffer.getChar() == '\n')
            return b.toString();
        else
            throw new MalformedServerReplyException("Received illegal newline character!");
    }

    @Override
    public ByteBuffer serialize(String value) {
        return toBuffer(value, getIdentifier());
    }
}
