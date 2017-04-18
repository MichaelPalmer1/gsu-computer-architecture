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
            dataMap.put(key, data.opt(key));
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
}
