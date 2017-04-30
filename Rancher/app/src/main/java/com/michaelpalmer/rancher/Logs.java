package com.michaelpalmer.rancher;

import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.StatusLine;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Map;

public class Logs  {

    public static void main(String[] args) {
        String url = "";
        String token = "";

        final String rancher_access_key = "";
        final String rancher_secret_key = "";

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(rancher_access_key, rancher_secret_key.toCharArray());
            }
        });

        // Parse the data
        try {
            // Create client and connect
            WebSocket socket = new WebSocketFactory().createSocket(url);
            socket.addHeader("Authorization", "Bearer " + token);
            socket.addListener(new WebSocketListener() {
                @Override
                public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
                    System.out.println("New state: " + newState.toString());
                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    System.out.println("Connected!");
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
                    System.out.println("Connect error: " + cause.getMessage());
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    System.out.println("Disconnected. Closed by server = " + closedByServer);
                }

                @Override
                public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Frame: " + frame.getPayloadText());
                }

                @Override
                public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("More frame: " + frame.getPayloadText());
                }

                @Override
                public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Text frame!");
                }

                @Override
                public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Binary frame: " + frame.getPayloadText());
                }

                @Override
                public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Close frame: " + frame.toString());
                }

                @Override
                public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Ping");
                }

                @Override
                public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Pong");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    System.out.printf("Text message received: %s\n", text);
                }

                @Override
                public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
                    System.out.println("Binary message!");
                }

                @Override
                public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Sending frame: " + frame.getPayloadText());
                }

                @Override
                public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Frame sent: " + frame.getPayloadText());
                }

                @Override
                public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    System.out.println("Frame unsent: " + frame.getPayloadText());
                }

                @Override
                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                    System.out.println("Error: " + cause.getMessage());
                }

                @Override
                public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
                    System.out.println("Frame error: " + cause.getMessage());
                }

                @Override
                public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
                    System.out.println("Message error: " + cause.getMessage());
                }

                @Override
                public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
                    System.out.println("Decompression error: " + cause.getMessage());
                }

                @Override
                public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
                    System.out.println("Text message error: " + cause.getMessage());
                }

                @Override
                public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
                    System.out.println("Send error: " + cause.getMessage());
                }

                @Override
                public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
                    System.out.println("Unexpected error: " + cause.getMessage());
                }

                @Override
                public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
                    System.out.println("Callback error: " + cause.getMessage());
                }

                @Override
                public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {
                    System.out.println("Sending handshake");
                }
            });

            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpeningHandshakeException e) {
            e.printStackTrace();

            // Status line.
            StatusLine sl = e.getStatusLine();
            System.out.println("=== Status Line ===");
            System.out.format("HTTP Version  = %s\n", sl.getHttpVersion());
            System.out.format("Status Code   = %d\n", sl.getStatusCode());
            System.out.format("Reason Phrase = %s\n", sl.getReasonPhrase());

            // HTTP headers.
            Map<String, List<String>> headers = e.getHeaders();
            System.out.println("=== HTTP Headers ===");
            for (Map.Entry<String, List<String>> entry : headers.entrySet())
            {
                // Header name.
                String name = entry.getKey();

                // Values of the header.
                List<String> values = entry.getValue();

                if (values == null || values.size() == 0)
                {
                    // Print the name only.
                    System.out.println(name);
                    continue;
                }

                for (String value : values)
                {
                    // Print the name and the value.
                    System.out.format("%s: %s\n", name, value);
                }
            }

        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

}
