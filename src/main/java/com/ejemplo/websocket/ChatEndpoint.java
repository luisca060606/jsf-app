package com.ejemplo.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chatServer")
public class ChatEndpoint {

    private static Set<Session> usuarios = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void alAbrir(Session session) {
        usuarios.add(session);
        System.out.println("DEBUG: Usuario conectado al chat. ID: " + session.getId());
    }

    @OnMessage
    public void alRecibirMensaje(String mensaje, Session session) {
        synchronized (usuarios) {
            for (Session s : usuarios) {
                try {
                    if (s.isOpen()) {
                        s.getBasicRemote().sendText(mensaje);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClose
    public void alCerrar(Session session) {
        usuarios.remove(session);
        System.out.println("DEBUG: Usuario desconectado.");
    }

    @OnError
    public void alError(Throwable t) {
        System.err.println("Error en WebSocket: " + t.getMessage());
    }
}