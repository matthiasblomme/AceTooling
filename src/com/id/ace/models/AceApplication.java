package com.id.ace.models;

import com.id.ace.environment.overview.AceEnvironment;

import java.util.HashMap;

public class AceApplication {
    String name;
    //HashMap integrationServer = new HashMap();
    HashMap<String, AceMessageflow> children = new HashMap();

    public AceApplication(String name) {
        this.name = name;
    }

    public AceApplication(String name, AceMessageflow flow) {
        this.name = name;
        addChild(flow);;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, AceMessageflow> getChildren() {
        return children;
    }

    public AceMessageflow getFlow(String flowName){
        return children.get(flowName);
    }

    public void setChildren(HashMap children) {
        this.children = children;
    }

    public void addChild(AceMessageflow flow){
        this.children.putIfAbsent(flow.getName(), flow);
    }
}
