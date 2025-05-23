package io.github.larscom.bitvavo.websocket;

/// Base incoming message from WebSocket
public interface MessageIn {
    MessageInEvent getEvent();
}
