package org.example.cargame.snapshot;

import org.example.cargame.enums.MessageType;

import java.util.Map;

public record MessageSnapshot(Map<MessageType, String> messages) {
}
