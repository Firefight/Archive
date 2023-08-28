package studio.archetype.rook.network.responses;

import org.apache.commons.net.MalformedServerReplyException;

import java.nio.ByteBuffer;

public class IntegerResponse implements Response<Long> {

    @Override
    public char getIdentifier() { return ':'; }

    @Override
    public Long parse(ByteBuffer buffer) throws MalformedServerReplyException {
        String s = Response.SIMPLE_STRING.parse(buffer);
        try {
            return Long.parseLong(s);
        } catch(NumberFormatException e) {
            throw new MalformedServerReplyException("Received value \"" + s + "\"is not a valid integer!");
        }
    }

    @Override
    public ByteBuffer serialize(Long value) {
        String val = value.toString();
        return toBuffer(val, getIdentifier());
    }
}
