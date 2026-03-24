package org.example.cargameFx.restcontroller;

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
        Map<String, Object> gameState = new HashMap<>();
        gameState.put("x", gameRestController.getPosX());
        gameState.put("y", gameRestController.getPosY());
        gameState.put("state", gameRestController.getState());
        gameState.put("power", gameRestController.getPower());
        gameState.put("color", gameRestController.getColor());
        gameState.put("engine", gameRestController.getEngine());
        gameState.put("message", gameRestController.getAlertMessage());
        gameState.put("warning", gameRestController.getWarningMessage());
        gameState.put("speed", gameRestController.getSpeed());

        messagingTemplate.convertAndSend("/topic/game", (Object) gameState);
    }
}
