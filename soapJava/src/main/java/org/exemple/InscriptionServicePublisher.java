package org.exemple;

import javax.xml.ws.Endpoint;

public class InscriptionServicePublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8081/inscription", new InscriptionServiceImpl());
        System.out.println("Service d'inscription démarré !");
    }
}