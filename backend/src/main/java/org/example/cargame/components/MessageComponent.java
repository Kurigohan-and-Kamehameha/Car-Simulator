package org.example.cargame.components;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.cargame.enums.MessageType;
import org.example.cargame.snapshot.MessageSnapshot;

public class MessageComponent extends Component<MessageSnapshot> {

    private volatile MessageSnapshot snapshot;

    public MessageSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(MessageSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public void addMessage(MessageType type, String message) {
        Map<MessageType, String> newMap = new ConcurrentHashMap<>();
        if (this.snapshot != null && this.snapshot.messages() != null) {
            newMap.putAll(this.snapshot.messages());
        }

        newMap.put(type, message);

        this.snapshot = new MessageSnapshot(Map.copyOf(newMap));
    }

}
