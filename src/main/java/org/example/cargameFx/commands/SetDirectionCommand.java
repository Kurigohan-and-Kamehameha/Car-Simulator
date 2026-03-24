package org.example.cargameFx.commands;

import org.example.cargameFx.Dijkstra;
import org.example.cargameFx.Model;
import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.NodeType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Edge;
import org.example.cargameFx.graph.Graph;
import org.example.cargameFx.graph.Node;
import org.example.cargameFx.observer.ObserverDispatcher;
import org.example.cargameFx.snapshot.PathSnapshot;

import java.util.List;

public class SetDirectionCommand implements Command {
    private final Model model;
    private final Dijkstra dij;
    private final Graph graph;
    private final ObserverDispatcher dispatcher;

    private final EntityId playerId;
    private final String targetId;

    public SetDirectionCommand(Model model,
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
        if (State.DRIVE != model.getStates().get(playerId).get()) {
            if(!targetId.equals(model.getPositions().get(playerId).getSnapshot().currentNode().getId())){
                Node targetNode = graph.getNodeById(targetId);
                Node currentNode = model.getPositions().get(playerId).getSnapshot().currentNode();

                List<Edge> path = dij.calcShortestPath(currentNode, targetNode, graph.getNodes());
                if (path.isEmpty()) return;

                double totalWeight = path.stream()
                        .mapToDouble(Edge::getWeight)
                        .sum();

                double currentPower = model.getStorage().get(playerId).getSnapshot().power();

                if (currentPower < totalWeight) {
                    System.out.println("Not enough Power to reach target");
                    return;
                }

                double minDistanceToGas = graph.getNodes().stream()
                        .filter(node -> node.getType() == NodeType.GASSTATION)
                        .mapToDouble(gasNode -> {
                            if (gasNode.equals(targetNode)) return 0.0;
                            List<Edge> pathToGas = dij.calcShortestPath(targetNode, gasNode, graph.getNodes());
                            if (pathToGas.isEmpty()) return Double.POSITIVE_INFINITY;
                            return pathToGas.stream().mapToDouble(Edge::getWeight).sum();
                        })
                        .min()
                        .orElse(Double.POSITIVE_INFINITY);

                double remainingPowerAfterTarget = currentPower - totalWeight;

                if (remainingPowerAfterTarget < minDistanceToGas) {
                    System.out.println("Not enough Power to reach next GasStation after target");
                    return;
                }

                PathSnapshot newSnap = new PathSnapshot(path);
                model.getPaths().get(playerId).setSnapshot(newSnap);
                model.getStates().get(playerId).set(State.DRIVE);

                dispatcher.dispatch(() ->
                        model.getStates().get(playerId).notifyObservers()
                );

            }
        }
    }
}
