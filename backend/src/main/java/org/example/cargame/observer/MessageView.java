package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.components.MessageComponent;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.MessageType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageView extends ParentView<CarModel> implements MessageObserver {
    private final Map<EntityId, Map<MessageType, String>> messages = new ConcurrentHashMap<>();

    public MessageView(CarModel model) {
        super(model);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            MessageComponent msgComponent = model.getMessages().get(id);
            Map<MessageType, String> map = new ConcurrentHashMap<>();
            map.put(MessageType.ALERT, msgComponent.getMessage(MessageType.ALERT));
            map.put(MessageType.WARNING, msgComponent.getMessage(MessageType.WARNING));

            messages.put(id, map);

            msgComponent.addObserver(this);
        }
    }

    @Override
    public void bind(EntityId id) {
        MessageComponent msgComponent = model.getMessages().get(id);
        Map<MessageType, String> map = new ConcurrentHashMap<>();
        map.put(MessageType.ALERT, msgComponent.getMessage(MessageType.ALERT));
        map.put(MessageType.WARNING, msgComponent.getMessage(MessageType.WARNING));
        messages.put(id, map);
        msgComponent.addObserver(this);
    }

    @Override
    public void rebind() {
        messages.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        messages.remove(id);
        model.getMessages().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        MessageComponent msgComponent = model.getMessages().get(id);

        messages.computeIfAbsent(id, k -> new ConcurrentHashMap<>())
                .put(MessageType.ALERT, msgComponent.getMessage(MessageType.ALERT));

        messages.get(id)
                .put(MessageType.WARNING, msgComponent.getMessage(MessageType.WARNING));
    }

    public String alert(EntityId id) {
        return messages.getOrDefault(id, Map.of())
                .get(MessageType.ALERT);
    }

    public String warning(EntityId id) {
        return messages.getOrDefault(id, Map.of())
                .get(MessageType.WARNING);
    }

}
