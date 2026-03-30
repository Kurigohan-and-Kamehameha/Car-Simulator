package org.example.cargame.commands;

import org.example.cargame.CarModel;
import org.example.cargame.Dijkstra;
import org.example.cargame.Model;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.MessageType;
import org.example.cargame.enums.NodeType;
import org.example.cargame.enums.State;
import org.example.cargame.graph.Edge;
import org.example.cargame.graph.Graph;
import org.example.cargame.graph.Node;
import org.example.cargame.observer.ObserverDispatcher;
import org.example.cargame.snapshot.PathSnapshot;

import java.util.List;

public class SetDirectionCommand implements Command {
    private final CarModel model;
    private final Dijkstra dij;
    private final Graph graph;
    private final ObserverDispatcher dispatcher;

    private final EntityId playerId;
    private final String targetId;

    public SetDirectionCommand(CarModel model,
            Dijkstra dij,
            Graph graph,
            ObserverDispatcher dispatcher,
            EntityId playerId,
            String targetId) {
        this.model = model;
        this.dij = dij;
        this.graph = graph;
        this.dispatcher = dispatcher;
        this.playerId = playerId;
        this.targetId = targetId;
    }

    @Override
    public void run() {
        if (!model.getAllEntities().isEmpty() && State.DRIVE != model.getStates().get(playerId).get()) {
            String currentId = model.getPositions().get(playerId).getSnapshot().currentNode().getId();
            if (!targetId.equals(currentId)) {
                Node targetNode = graph.getNodeById(targetId);
                Node currentNode = model.getPositions().get(playerId).getSnapshot().currentNode();

                List<Edge> path = dij.calcShortestPath(currentNode, targetNode, graph.getNodes());

                if (path.isEmpty())
                    return;

                double currentPower = model.getStorage().get(playerId).getSnapshot().power();
                double capacity = model.getStorage().get(playerId).getSnapshot().capacity();

                for (Edge edge : path) {
                    double weight = edge.getWeight();
                    if (currentPower < weight) {
                        model.getMessages().get(playerId)
                                .setMessage(MessageType.ALERT, "Not enough Power to reach target");
                        dispatcher.dispatch(() -> model.getMessages().get(playerId).notifyObservers(playerId));
                        return;
                    }
                    currentPower -= weight;

                    Node toNode = edge.getTo();
                    if (toNode.getType() == NodeType.GASSTATION) {
                        currentPower = capacity;
                    }
                }

                double minDistanceToGas = graph.getNodes().stream()
                        .filter(node -> node.getType() == NodeType.GASSTATION)
                        .mapToDouble(gasNode -> {
                            if (gasNode.equals(targetNode))
                                return 0.0;
                            List<Edge> pathToGas = dij.calcShortestPath(targetNode, gasNode, graph.getNodes());
                            if (pathToGas.isEmpty())
                                return Double.POSITIVE_INFINITY;
                            return pathToGas.stream().mapToDouble(Edge::getWeight).sum();
                        })
                        .min()
                        .orElse(Double.POSITIVE_INFINITY);

                if (currentPower < minDistanceToGas) {
                    model.getMessages().get(playerId)
                            .setMessage(MessageType.ALERT, "Not enough Power to reach next gas station after target");
                    dispatcher.dispatch(() -> model.getMessages().get(playerId).notifyObservers(playerId));
                    return;
                }

                PathSnapshot newSnap = new PathSnapshot(path);
                model.getPaths().get(playerId).setSnapshot(newSnap);
                model.getStates().get(playerId).set(State.DRIVE);
                model.getMessages().get(playerId).setMessage(MessageType.WARNING,
                        "Must be at workshop to change engine or color.");
                model.getMessages().get(playerId).setMessage(MessageType.ALERT, "");
                dispatcher.dispatch(() -> {
                    model.getStates().get(playerId).notifyObservers(playerId);
                    model.getMessages().get(playerId).notifyObservers(playerId);
                });
            }
        }
    }

}
