package com.michaelpalmer.rancher;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.util.Map;


public abstract class RancherWSClient extends WebSocketClient {

    public RancherWSClient(URI serverURI) {
        super(serverURI);
    }

    public RancherWSClient(URI serverUri, Map<String, String> headers) {
        super(serverUri, new Draft_17(), headers, 0);
        WebSocketImpl.DEBUG = true;
    }
}
