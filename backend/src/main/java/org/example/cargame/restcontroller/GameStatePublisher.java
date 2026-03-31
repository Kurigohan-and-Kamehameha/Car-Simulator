package org.example.cargame.restcontroller;

import org.example.cargame.snapshot.GameStateDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@EnableScheduling
public class GameStatePublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameRestController gameRestController;

    public GameStatePublisher(SimpMessagingTemplate messagingTemplate, GameRestController gameRestController) {
        this.messagingTemplate = messagingTemplate;
        this.gameRestController = gameRestController;
    }

    @Scheduled(fixedRate = 50)
    public void publishGameState() {
        Map<Integer, GameStateDTO> snapshot = gameRestController.getAllGameStates();

        if (!gameRestController.getLoadingCompete() || gameRestController.getUpdateInProgress()) return;

        snapshot.forEach((id, state) -> {
            messagingTemplate.convertAndSend("/topic/game", state);
        });
    }
}
