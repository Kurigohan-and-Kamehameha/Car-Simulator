package org.example.cargame.components;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.cargame.enums.MessageType;

public class MessageComponent extends Component {
    private final Map<MessageType, String> messages = new ConcurrentHashMap<>();

    public String getMessage(MessageType type) {
        return messages.get(type);
    }

    public void setMessage(MessageType type, String message) {
        this.messages.put(type, message);
    }

    public Map<MessageType, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<MessageType, String> messages) {
        this.messages.clear();
        this.messages.putAll(messages);
    }
}
