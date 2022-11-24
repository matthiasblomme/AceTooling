package com.id.ace.models;

import java.util.HashMap;
import java.util.List;

public class AceIntegrationServer {
    String name;
    HashMap<String, AceApplication> children = new HashMap();

    public AceIntegrationServer(String name, AceApplication app) {
        this.name = name;
        this.addChild(app);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, AceApplication> getChildren() {
        return children;
    }

    public void setChildren(HashMap children) {
        this.children = children;
    }

    public void addChild(AceApplication app){
        children.putIfAbsent(app.getName(), app);
    }

}
