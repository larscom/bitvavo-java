package io.github.larscom.websocket;

/// Base incoming message from WebSocket
public interface MessageIn {
    MessageInEvent getEvent();
}
