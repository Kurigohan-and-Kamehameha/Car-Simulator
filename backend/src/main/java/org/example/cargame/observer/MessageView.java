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
    private final Map<EntityId, Map<MessageType, String>> cache = new ConcurrentHashMap<>();

    public MessageView(CarModel model, ObserverDispatcher dispatcher) {
        super(model, dispatcher);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            bind(id);
        }
    }

    @Override
    public void bind(EntityId id) {
        MessageComponent msgComponent = model.getMessages().get(id);
        Map<MessageType, String> map = cache.computeIfAbsent(id, k -> new ConcurrentHashMap<>());
        map.put(MessageType.ALERT, msgComponent.getMessage(MessageType.ALERT));
        map.put(MessageType.WARNING, msgComponent.getMessage(MessageType.WARNING));
        cache.put(id, map);
        msgComponent.addObserver(this);
    }

    @Override
    public void rebind() {
        cache.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        cache.remove(id);
        model.getMessages().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        MessageComponent msgComponent = model.getMessages().get(id);
        cache.computeIfAbsent(id, k -> new ConcurrentHashMap<>())
                .put(MessageType.ALERT, msgComponent.getMessage(MessageType.ALERT));
        cache.get(id)
                .put(MessageType.WARNING, msgComponent.getMessage(MessageType.WARNING));
    }

    public String alert(EntityId id) {
        Map<MessageType, String> snap = cache.get(id);
        return snap != null ? snap.getOrDefault(MessageType.ALERT, "") : "";
    }

    public String warning(EntityId id) {
        Map<MessageType, String> snap = cache.get(id);
        return snap != null ? snap.getOrDefault(MessageType.WARNING, "") : "";
    }

}
