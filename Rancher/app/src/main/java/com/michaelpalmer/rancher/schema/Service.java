package com.michaelpalmer.rancher.schema;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Service extends BaseSchema {

    public static List<Service> ITEMS = new ArrayList<>();
    private String id;
    private String name;
    private String state;
    private String description;
    private String healthState;
    private JSONObject links;


    public Service(String id, String name, String state, String description, String healthState, JSONObject links,
                   JSONObject data) {
        super(data);
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.healthState = healthState;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public String getHealthState() {
        return healthState;
    }

    public JSONObject getLinks() {
        return links;
    }
}
