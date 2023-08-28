package studio.archetype.rook.network.responses;

import org.apache.commons.net.MalformedServerReplyException;

import java.nio.ByteBuffer;

public interface Response<T> {

    String TERMINATOR = "\r\n";

    char getIdentifier();

    T parse(ByteBuffer buffer) throws MalformedServerReplyException;
    ByteBuffer serialize(T value);

    default ByteBuffer toBuffer(String value, char prefix) {
        ByteBuffer buffer = ByteBuffer.allocate(value.length() + 3);
        buffer.putChar(prefix);
        writeString(buffer, value);
        writeString(buffer, TERMINATOR);
        return buffer;
    }

    default void writeString(ByteBuffer buffer, String str) {
        for(char c : str.toCharArray())
            buffer.putChar(c);
    }

    Response<String> SIMPLE_STRING = new SimpleStringResponse();
    Response<String> ERROR = new ErrorResponse();
    Response<Long> INTEGER = new IntegerResponse();
}
