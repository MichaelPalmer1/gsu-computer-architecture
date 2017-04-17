package com.michaelpalmer.rancher.schema;

import java.util.ArrayList;
import java.util.List;

public class Service {

    public static List<Service> ITEMS = new ArrayList<>();
    private String id;
    private String name;
    private String state;
    private String description;
    private String healthState;


    public Service(String id, String name, String state, String description, String healthState) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.healthState = healthState;
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
}
