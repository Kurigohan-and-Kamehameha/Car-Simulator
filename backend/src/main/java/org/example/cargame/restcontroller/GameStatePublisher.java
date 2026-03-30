package org.example.cargame.restcontroller;

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
        for(int id : gameRestController.getAllIds()){
            if(!gameRestController.getAllIds().isEmpty()){
                try{
                    Map<String, Object> gameState = new HashMap<>();
                    gameState.put("x", gameRestController.getPosX(id));
                    gameState.put("y", gameRestController.getPosY(id));
                    gameState.put("state", gameRestController.getState(id));
                    gameState.put("power", gameRestController.getPower(id));
                    gameState.put("color", gameRestController.getColor(id));
                    gameState.put("engine", gameRestController.getEngine(id));
                    gameState.put("message", gameRestController.getAlertMessage(id));
                    gameState.put("warning", gameRestController.getWarningMessage(id));
                    gameState.put("speed", gameRestController.getSpeed(id));

                    messagingTemplate.convertAndSend("/topic/game", (Object) gameState);
                } finally {

                }
            }
        }
    }
}
