import React, { useState, useEffect } from 'react';
import { getGraph, setDirection } from './api';
import { GameWebSocket } from './websocket';
import CanvasRenderer from './components/CanvasRenderer';
import ControlPanel from './components/ControlPanel';

function App() {
  const [graph, setGraph] = useState(null);
  const [gameState, setGameState] = useState(null);
  
  useEffect(() => {
    // Function to fetch graph from backend
    const fetchGraph = () => getGraph().then(setGraph).catch(console.error);

    // Setup WebSockets
    const ws = new GameWebSocket();
    ws.connect(
      (state) => setGameState(state),
      () => fetchGraph() // Re-fetch graph on every connection/reconnection
    );

    return () => {
      ws.disconnect();
    };
  }, []);

  const handleNodeClick = (nodeId) => {
    setDirection(nodeId).catch(console.error);
  };

  return (
    <div className="app-container">
      <header className="header">
        <h1>Car Game Web UI</h1>
      </header>
      <main className="main-content">
        <div className="game-view">
          <CanvasRenderer 
            graph={graph} 
            gameState={gameState} 
            onNodeClick={handleNodeClick} 
          />
        </div>
        <aside className="sidebar">
          <ControlPanel gameState={gameState} />
        </aside>
      </main>
    </div>
  );
}

export default App;
