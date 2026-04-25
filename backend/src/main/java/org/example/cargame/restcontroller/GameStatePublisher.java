package org.example.cargame.restcontroller;

import org.example.cargame.snapshot.GameStateDTO;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.example.cargame.Servicelayer;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableScheduling
public class GameStatePublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final Servicelayer serviceLayer;

    public GameStatePublisher(SimpMessagingTemplate messagingTemplate, Servicelayer serviceLayer) {
        this.messagingTemplate = messagingTemplate;
        this.serviceLayer = serviceLayer;
    }

    private final Map<Integer, GameStateDTO> lastPublished = new ConcurrentHashMap<>();

    private long lastHeartbeatTime = System.currentTimeMillis();

    @Scheduled(fixedRate = 50)
    public void publishGameState() {
        Map<Integer, GameStateDTO> snapshot = serviceLayer.getAllGameStates();

        if (!serviceLayer.getLoadingCompete() || serviceLayer.getUpdateInProgress())
            return;

        boolean forceHeartbeat = (System.currentTimeMillis() - lastHeartbeatTime) > 2000;

        snapshot.forEach((id, state) -> {
            GameStateDTO lastState = lastPublished.get(id);
            if (forceHeartbeat || !state.equals(lastState)) {
                messagingTemplate.convertAndSend("/topic/game", state);
                lastPublished.put(id, state);
            }
        });

        if (forceHeartbeat) {
            lastHeartbeatTime = System.currentTimeMillis();
        }
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {
        Map<Integer, GameStateDTO> snapshot = serviceLayer.getAllGameStates();
        snapshot.forEach((id, state) -> {
            messagingTemplate.convertAndSend("/topic/game", state);
            lastPublished.put(id, state);
        });
    }
}
