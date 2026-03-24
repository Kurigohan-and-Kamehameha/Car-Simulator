import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const WS_URL = 'http://localhost:8080/ws';

export class GameWebSocket {
  constructor() {
    this.client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      debug: function (str) {
        // console.log(str); // Uncomment for debugging
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });
  }

  connect(onStateUpdate, onConnect) {
    this.client.onConnect = () => {
      if (onConnect) onConnect(); // Trigger graph refresh on connection/reconnection
      this.client.subscribe('/topic/game', (message) => {
        if (message.body) {
          onStateUpdate(JSON.parse(message.body));
        }
      });
    };

    this.client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.client.activate();
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
    }
  }
}
