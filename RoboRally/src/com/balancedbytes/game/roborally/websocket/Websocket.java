package com.balancedbytes.game.roborally.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ServerEndpoint(value = "/websocket")
public class Websocket {

    private static final Logger LOG = LogManager.getLogger(Websocket.class);

    private static final Set<Websocket> sConnections = new CopyOnWriteArraySet<>();

    private Session fSession;

    public Websocket() {
        super();
    }

    @OnOpen
    public void onOpen(Session session) {
        fSession = session;
        sConnections.add(this);
        // String message = String.format("* %s %s", nickname, "has joined.");
        // broadcast(message);
        // LOG.info(message);
    }


    @OnClose
    public void onClose(Session session) {
        sConnections.remove(this);
        // String message = String.format("* %s %s", nickname, "has disconnected.");
        // broadcast(message);
    }


    @OnMessage
    public void onMessage(String message) {
        // Never trust the client
        // String filteredMessage = String.format("%s: %s", nickname, HtmlFilter.filter(message.toString()));
        // broadcast(filteredMessage);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        LOG.error("Chat Error: " + t.toString(), t);
    }

    public void sendText(String message) {
        try {
            synchronized (this) {
                fSession.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            LOG.debug("Chat Error: Failed to send message to client", e);
            sConnections.remove(this);
            try {
                fSession.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }
    
}
