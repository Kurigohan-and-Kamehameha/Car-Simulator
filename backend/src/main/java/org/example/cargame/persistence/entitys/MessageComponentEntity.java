package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;
import org.example.cargame.enums.MessageType;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "message_component")
public class MessageComponentEntity {

    @Id
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "messages",
            joinColumns = @JoinColumn(name = "message_component_entity_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "type")
    @Column(name = "message")
    private Map<MessageType, String> messages = new HashMap<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Map<MessageType, String> getMessages() { return messages; }
    public void setMessages(Map<MessageType, String> messages) { this.messages = messages; }
}