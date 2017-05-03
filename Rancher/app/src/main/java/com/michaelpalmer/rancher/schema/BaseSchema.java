package com.michaelpalmer.rancher.schema;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class BaseSchema {

    private HashMap<String, Object> dataMap = new HashMap<>();

    /**
     * Instantiate base schema
     *
     * @param data Data
     */
    BaseSchema(JSONObject data) {
        Iterator<String> keys = data.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object obj = data.opt(key);

            // Try to convert "null" to null
            try {
                if (obj.toString().equals("null")) {
                    obj = null;
                }
            } catch (Exception e) {
                // If something goes wrong, fallback to default action.
            }
            dataMap.put(key, obj);
        }
    }

    /**
     * Retrieve the value of a property
     *
     * @param key Property
     * @return Property value
     */
    public Object getProperty(String key) {
        return dataMap.get(key);
    }

    /**
     * Get a property string
     * @param key Property
     * @return Property value
     */
    public String getString(String key) {
        try {
            return (String) getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get all of the available keys
     *
     * @return Key set
     */
    public Set<String> getPropertyKeys() {
        return dataMap.keySet();
    }

    /**
     * Whether a property exists
     *
     * @return True if the property exists, otherwise false
     */
    public boolean hasProperty(String key) {
        return dataMap.containsKey(key);
    }

    public String getName() {
        try {
            return (String) getProperty("name");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return this objects actions
     *
     * @return JSON object of actions
     */
    public JSONObject getActions() {
        try {
            return (JSONObject) getProperty("actions");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieve the type of this resource
     *
     * @return Resource type
     */
    public String getType() {
        try {
            return (String) getProperty("type");
        } catch (Exception e) {
            return null;
        }
    }
}
