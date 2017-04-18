package com.michaelpalmer.rancher.schema;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Stack extends BaseSchema {

    public static List<Stack> ITEMS = new ArrayList<>();
    private String id, name, state, description, group, healthState;
    private JSONObject links, actions;


    public Stack(String id, String name, String state, String description, String group, String healthState,
                 JSONObject links, JSONObject actions, JSONObject data) {
        super(data);
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

    public JSONObject getLinks() {
        return links;
    }

    public JSONObject getActions() {
        return actions;
    }
}
