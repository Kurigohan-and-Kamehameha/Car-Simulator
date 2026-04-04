package org.example.cargame.restcontroller;

import org.example.cargame.snapshot.GameStateDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.example.cargame.Servicelayer;

import java.util.Map;

@Service
@EnableScheduling
public class GameStatePublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final Servicelayer serviceLayer;

    public GameStatePublisher(SimpMessagingTemplate messagingTemplate, Servicelayer serviceLayer) {
        this.messagingTemplate = messagingTemplate;
        this.serviceLayer = serviceLayer;
    }

    @Scheduled(fixedRate = 50)
    public void publishGameState() {
        Map<Integer, GameStateDTO> snapshot = serviceLayer.getAllGameStates();

        if (!serviceLayer.getLoadingCompete() || serviceLayer.getUpdateInProgress())
            return;

        snapshot.forEach((id, state) -> messagingTemplate.convertAndSend("/topic/game", state));
    }
}
