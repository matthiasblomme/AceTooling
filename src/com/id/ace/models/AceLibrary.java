package com.id.ace.models;

import java.util.HashMap;
import java.util.HashSet;

public class AceLibrary {
    String name;
    HashSet<String> integrationServers;
    HashMap children = new HashMap();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<String> getIntegrationServers() {
        return integrationServers;
    }

    public void setIntegrationServers(HashSet<String> integrationServers) {
        this.integrationServers = integrationServers;
    }

    public HashMap getChildren() {
        return children;
    }

    public void setChildren(HashMap children) {
        this.children = children;
    }
}
