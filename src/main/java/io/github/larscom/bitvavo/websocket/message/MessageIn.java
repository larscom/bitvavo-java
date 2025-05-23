package io.github.larscom.bitvavo.websocket.message;

/// Base incoming message from WebSocket
public interface MessageIn {
    MessageInEvent getEvent();
}
