package studio.archetype.rook.network.responses;

import org.apache.commons.net.MalformedServerReplyException;

import java.nio.ByteBuffer;

public class BulkStringResponse implements Response<String> {

    @Override
    public char getIdentifier() { return '$'; }

    @Override
    public String parse(ByteBuffer buffer) throws MalformedServerReplyException {
        long count = Response.INTEGER.parse(buffer);
        if(count == -1)
            return null;
        else if(count == 0)
            return "";

        StringBuilder b = new StringBuilder();
        for(int i = 0; i < count; i++) {
            char next = buffer.getChar();
            if(next == '\r' || next == '\n')
                throw new MalformedServerReplyException("Received illegal newline character!");
            else
                b.append(b);
        }
        return b.toString();
    }

    @Override
    public ByteBuffer serialize(String value) {
        String length = String.valueOf(value.length());
        ByteBuffer buffer = ByteBuffer.allocate(value.length() + length.length() + 5);
        buffer.putChar(getIdentifier());
        writeString(buffer, length);
        writeString(buffer, TERMINATOR);
        writeString(buffer, value);
        writeString(buffer, TERMINATOR);
        return buffer;
    }
}
