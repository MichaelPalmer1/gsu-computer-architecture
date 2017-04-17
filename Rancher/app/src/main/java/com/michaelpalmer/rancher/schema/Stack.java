package com.michaelpalmer.rancher.schema;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Stack {

    public static List<Stack> ITEMS = new ArrayList<>();
    private String id;
    private String name;
    private String state;
    private String description;
    private String group;
    private String healthState;
    private ArrayList<String> serviceIds;
    private JSONObject links, actions;


    public Stack(String id, String name, String state, String description, String group, String healthState,
                 JSONObject links, JSONObject actions) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.group = group;
        this.healthState = healthState;
        this.links = links;
        this.actions = actions;
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

    public String getGroup() {
        return group;
    }

    public String getHealthState() {
        return healthState;
    }

    public ArrayList<String> getServiceIds() {
        return serviceIds;
    }

    public JSONObject getLinks() {
        return links;
    }

    public JSONObject getActions() {
        return actions;
    }

    public void setServiceIds(ArrayList<String> serviceIds) {
        this.serviceIds = serviceIds;
    }
}
