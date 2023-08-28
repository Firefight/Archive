package studio.archetype.firefight.cardinal.proxy.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocketMessage<T> {
    private SocketMessageType type;
    private T data;

    @SuppressWarnings("unchecked") // Go fuck yourself
    public <D> D data() {
        return ((SocketMessage<D>) this).getData();
    }
}
