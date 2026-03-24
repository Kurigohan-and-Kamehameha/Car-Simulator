package org.example.cargameFx.components;

import java.util.HashMap;

import org.example.cargameFx.enums.MessageType;

public class MessageComponent extends Component {
    private HashMap<MessageType, String> messages = new HashMap<MessageType, String>();

    public String getMessage(MessageType type) {
        return messages.get(type);
    }

    public void setMessage(MessageType type, String message) {
        this.messages.put(type, message);
    }
}
