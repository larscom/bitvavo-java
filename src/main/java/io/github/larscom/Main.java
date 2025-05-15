package io.github.larscom;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(final String[] args) throws URISyntaxException, InterruptedException {
        final var c = new MyClient(new URI("wss://ws.bitvavo.com/v2"));
//        c.connect();
        c.connectBlocking();
        System.out.println("Kom ik hier?");
    }
}