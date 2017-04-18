package com.michaelpalmer.rancher.schema;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Host extends BaseSchema {

    public static List<Host> ITEMS = new ArrayList<>();
    private String id;
    private String name;
    private JSONObject links;


    public Host(String id, String name, JSONObject links, JSONObject data) {
        super(data);
        this.id = id;
        this.name = name;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        try {
            Object name = getProperty("name");
            if (name != null) {
                return name.toString();
            }

            name = getProperty("hostname");
            if (name != null) {
                return name.toString();
            }
        } catch (NullPointerException e) {
            // Let it go
        }

        return "Unknown";
    }

    public JSONObject getLinks() {
        return links;
    }
}
