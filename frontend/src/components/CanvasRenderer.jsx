import React, { useRef, useEffect } from 'react';

const CanvasRenderer = ({ graph, gameState, onNodeClick }) => {
    const canvasRef = useRef(null);
    const animRef = useRef({
        currentX: 0,
        currentY: 0,
        queue: [], // Queue of {x, y, color} targets from backend
        initialized: false,
        lastColor: '#3498db'
    });

    // Queue up incoming WebSocket states to ensure every point is reached
    useEffect(() => {
        if (gameState && gameState.x !== undefined && gameState.y !== undefined) {
            const nextTarget = {
                x: Number(gameState.x),
                y: Number(gameState.y),
                color: gameState.color || '#3498db'
            };

            if (!animRef.current.initialized) {
                animRef.current.currentX = nextTarget.x;
                animRef.current.currentY = nextTarget.y;
                animRef.current.lastColor = nextTarget.color;
                animRef.current.initialized = true;
            } else {
                const q = animRef.current.queue;
                const last = q.length > 0 
                    ? q[q.length - 1] 
                    : { x: animRef.current.currentX, y: animRef.current.currentY, color: animRef.current.lastColor };

                if (last.x !== nextTarget.x || last.y !== nextTarget.y || last.color !== nextTarget.color) {
                    q.push(nextTarget);
                }
            }
        }
    }, [gameState]);

    useEffect(() => {
        if (!graph || !graph.nodes) return;
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');
        let animationFrameId;

        const draw = () => {
            const anim = animRef.current;
            
            // Determine active target: front of queue or stay put
            if (anim.initialized) {
                let target = anim.queue.length > 0 ? anim.queue[0] : null;

                if (target) {
                    const dx = target.x - anim.currentX;
                    const dy = target.y - anim.currentY;
                    const dist = Math.sqrt(dx * dx + dy * dy);

                    // Snapping threshold: if we are close enough, jump to target to finish the move
                    // We use a larger threshold if there are more items in the queue to catch up
                    const snapThreshold = anim.queue.length > 1 ? 5.0 : 0.5;

                    if (dist < snapThreshold) {
                        anim.currentX = target.x;
                        anim.currentY = target.y;
                        anim.lastColor = target.color;
                        anim.queue.shift();
                    } else {
                        // More aggressive interpolation to minimize lag behind sidebar updates
                        const baseSpeed = 0.4; // Increased from 0.25
                        const queueInfluence = Math.min(anim.queue.length * 0.1, 0.5);
                        const factor = Math.min(baseSpeed + queueInfluence, 1.0);
                        
                        anim.currentX += dx * factor;
                        anim.currentY += dy * factor;
                    }
                }
            }

            // Clear and redraw EVERYTHING
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            // 1. Draw Edges
            if (graph.edges) {
                ctx.strokeStyle = 'rgba(255, 255, 255, 0.2)';
                ctx.lineWidth = 2;
                graph.edges.forEach(edge => {
                    const sourceNode = graph.nodes.find(n => n.id === edge.source);
                    const targetNode = graph.nodes.find(n => n.id === edge.target);
                    if (sourceNode && targetNode) {
                        ctx.beginPath();
                        ctx.moveTo(sourceNode.x, sourceNode.y);
                        ctx.lineTo(targetNode.x, targetNode.y);
                        ctx.stroke();
                    }
                });
            }

            // 2. Draw Nodes
            graph.nodes.forEach(node => {
                ctx.beginPath();
                const radius = 22;
                ctx.arc(node.x, node.y, radius, 0, 2 * Math.PI);
                
                if (node.type === 'WORKSHOP') ctx.fillStyle = '#e74c3c';
                else if (node.type === 'GASSTATION') ctx.fillStyle = '#a8e6cf';
                else ctx.fillStyle = '#ffffff';
                
                ctx.fill();
                ctx.strokeStyle = '#ffffff';
                ctx.lineWidth = 2;
                ctx.stroke();

                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                ctx.fillStyle = '#ffffff';
                if (node.type === 'GASSTATION') {
                    ctx.font = '28px sans-serif';
                    ctx.fillText('⛽', node.x, node.y + 3);
                } else if (node.type === 'WORKSHOP') {
                    ctx.font = '24px sans-serif';
                    ctx.fillText('🔧', node.x, node.y + 3);
                }
            });

            // 3. Draw Interpolated Player
            if (anim.initialized) {
                ctx.beginPath();
                ctx.arc(anim.currentX, anim.currentY, 22, 0, 2 * Math.PI);
                ctx.fillStyle = anim.lastColor;
                ctx.fill();
            }

            animationFrameId = requestAnimationFrame(draw);
        };

        animationFrameId = requestAnimationFrame(draw);
        return () => cancelAnimationFrame(animationFrameId);
    }, [graph]); // Redraw loop depends on graph topology, uses refs for movement

    const handleCanvasClick = (event) => {
        if (!graph || !graph.nodes) return;
        const canvas = canvasRef.current;
        const rect = canvas.getBoundingClientRect();
        const scaleX = canvas.width / rect.width;
        const scaleY = canvas.height / rect.height;
        const x = (event.clientX - rect.left) * scaleX;
        const y = (event.clientY - rect.top) * scaleY;

        const clickedNode = graph.nodes.find(node => {
            const dx = node.x - x;
            const dy = node.y - y;
            return Math.sqrt(dx * dx + dy * dy) < 30;
        });

        if (clickedNode && onNodeClick) onNodeClick(clickedNode.id);
    };

    return (
        <div className="canvas-wrapper">
            <canvas
                ref={canvasRef}
                width={1000}
                height={800}
                onClick={handleCanvasClick}
                style={{ 
                    width: '100%', 
                    height: 'auto', 
                    display: 'block',
                    background: '#1e272e', 
                    cursor: 'crosshair', 
                    borderRadius: '8px', 
                    boxShadow: '0 4px 6px rgba(0,0,0,0.3)' 
                }}
            />
        </div>
    );
};

export default CanvasRenderer;
