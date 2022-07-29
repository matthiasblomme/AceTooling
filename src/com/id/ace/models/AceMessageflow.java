package com.id.ace.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AceMessageflow {
    String name;
    String path;
    String application;
    String integrationServer;
    String state;
    HashMap properties = new HashMap<>();
    HashSet inputQueues;
    HashSet outputQueues;

    HashSet unknownQueues; //queues used by subflows, so not sure if they are input or output <Name, subflow>
    HashSet subscriptions;
    HashSet inputDirectories;
    HashSet outputDirectories;
    HashSet urlPath;

    public AceMessageflow(String messageFlowName, String integrationServerName, String applicationName, String state) {
        Pattern p = Pattern.compile("([\\w+\\.]+)\\.(\\w+)");
        Matcher m = p.matcher(messageFlowName);
        if (m.matches()) {
            this.path = m.group(1);
            this.name = m.group(2);
        } else {
            this.path = "";
            this.name = messageFlowName;
        }
        this.state = state;
        this.application = applicationName;
        this.integrationServer = integrationServerName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getIntegrationServer() {
        return integrationServer;
    }

    public void setIntegrationServer(String integrationServer) {
        this.integrationServer = integrationServer;
    }

    public HashMap getProperties() {
        return properties;
    }

    public void setProperties(HashMap properties) {
        this.properties = properties;
    }

    public HashSet getInputQueues() {
        return inputQueues;
    }

    public void setInputQueues(HashSet inputQueues) {
        this.inputQueues = inputQueues;
    }

    public HashSet getOutputQueues() {
        return outputQueues;
    }

    public void setOutputQueues(HashSet outputQueues) {
        this.outputQueues = outputQueues;
    }

    public HashSet getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(HashSet subscriptions) {
        this.subscriptions = subscriptions;
    }

    public HashSet getInputDirectories() {
        return inputDirectories;
    }

    public void setInputDirectories(HashSet inputDirectories) {
        this.inputDirectories = inputDirectories;
    }

    public HashSet getOutputDirectories() {
        return outputDirectories;
    }

    public void setOutputDirectories(HashSet outputDirectories) {
        this.outputDirectories = outputDirectories;
    }

    public HashSet getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(HashSet urlPath) {
        this.urlPath = urlPath;
    }

    public HashSet getUnknownQueues() {
        return unknownQueues;
    }

    public void setUnknownQueues(HashSet unknownQueues) {
        this.unknownQueues = unknownQueues;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
