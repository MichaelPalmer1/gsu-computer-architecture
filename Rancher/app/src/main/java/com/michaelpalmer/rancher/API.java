package com.michaelpalmer.rancher;


import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class API {

    /**
     * Perform a GET request for the specified URL.
     *
     * @param url URL to request
     * @return String response or null
     */
    @Nullable
    public static String GET(String url) {
        String s, response = "";
        try {
            // Create connection
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            // Check response code
            if (conn.getResponseCode() != 200) {
                throw new Exception(
                        String.format(Locale.US,
                                "Could not get data from remote source. HTTP response: %s (%d)",
                                conn.getResponseMessage(), conn.getResponseCode()
                        )
                );
            }

            // Save response to a string
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

            return response;

        } catch (Exception e) {
            Log.e("API", "Error encountered while sending API request: " + e.getMessage());
        }

        return null;
    }

    /**
     * Perform a POST request for the specified URL.
     *
     * @param url URL to request
     * @return String response or null
     */
    @Nullable
    public static String POST(String url) {
        return POST(url, null);
    }

    /**
     * Perform a POST request for the specified URL with some data.
     *
     * @param url URL to request
     * @param data Data to send
     * @return String response or null
     */
    public static String POST(String url, JSONObject data) {

        String s, response = "";
        try {
            // Create connection
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");

            // Check if posting data
            if (data != null) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                // Write data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data.toString());
                writer.close();
                os.close();
            }

            // Check response code
            if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299) {
                throw new Exception(
                        String.format(Locale.US,
                                "Could not get data from remote source. HTTP response: %s (%d)",
                                conn.getResponseMessage(), conn.getResponseCode()
                        )
                );
            }

            // Save response to a string
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

            return response;

        } catch (Exception e) {
            Log.e("API", "Error encountered while sending API request: " + e.getMessage());
        }

        return null;
    }

}
