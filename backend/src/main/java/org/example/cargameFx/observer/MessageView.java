package org.example.cargameFx.observer;

import org.example.cargameFx.Model;
import org.example.cargameFx.enums.MessageType;
import org.springframework.stereotype.Component;

@Component
public class MessageView extends PlayerView implements MessageObserver {
    private volatile String alertMsg;
    private volatile String warningMsg;

    public MessageView(Model model) {
        super(model);
        this.alertMsg = model.getMessages().get(playerId).getMessage(MessageType.ALERT);
        this.warningMsg = model.getMessages().get(playerId).getMessage(MessageType.WARNING);
        model.getMessages().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        this.alertMsg = model.getMessages().get(playerId).getMessage(MessageType.ALERT);
        this.warningMsg = model.getMessages().get(playerId).getMessage(MessageType.WARNING);
    }

    public String alert() {
        return this.alertMsg;
    }

    public String warning() {
        return this.warningMsg;
    }

}
