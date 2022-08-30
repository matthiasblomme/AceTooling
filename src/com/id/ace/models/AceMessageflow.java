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
    HashSet inputQueues = new HashSet<>();
    HashSet outputQueues = new HashSet<>();
    //queues used by subflows, so not sure if they are input or output <Name, subflow>
    HashSet unknownQueues = new HashSet<>();
    HashSet subscriptions = new HashSet<>();
    HashSet inputDirectories = new HashSet<>();
    HashSet ftpInput = new HashSet();
    HashSet outputDirectories = new HashSet<>();
    HashSet inputUrlPath = new HashSet<>();
    HashSet outputUrlPath = new HashSet<>();
    HashMap inputSoap = new HashMap();

    HashMap outputSoap = new HashMap();

    HashMap outputRest = new HashMap();

    HashSet esqlModules = new HashSet();

    HashMap databaseConnections = new HashMap<String, String>();

    HashSet javaModules = new HashSet();
    HashSet mappingModules = new HashSet();

    public AceMessageflow(String messageFlowName, String integrationServerName, String applicationName, String state) {
        Pattern p = Pattern.compile("([\\w+.]+)\\.(\\w+)");
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

    public AceMessageflow(String messageFlowName) {
        Pattern p = Pattern.compile("([\\w+.]+)\\.(\\w+)");
        Matcher m = p.matcher(messageFlowName);
        if (m.matches()) {
            this.path = m.group(1);
            this.name = m.group(2);
        } else {
            this.path = "";
            this.name = messageFlowName;
        }
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

    public void addProperty(String propertyName, Object property) {
        this.properties.putIfAbsent(propertyName, property);
    }

    public HashSet getInputQueues() {
        return inputQueues;
    }

    public void addInputQueue(String inputQueue) { this.inputQueues.add(inputQueue); }

    public HashSet getOutputQueues() {
        return outputQueues;
    }

    public void addOutputQueue(String outputQueue) {this.outputQueues.add(outputQueue);}

    public HashSet getSubscriptions() {
        return subscriptions;
    }

    public void setSubscription(String subscription) {
        this.subscriptions.add(subscription);
    }

    public HashSet getInputDirectories() {
        return inputDirectories;
    }

    public void addInputDirectory(String inputDirectory) {this.inputDirectories.add(inputDirectory);}

    public HashSet getFtpInput() {
        return ftpInput;
    }

    //TODO: expand to all ftp properties
    public void addFtpInput(String ftpInput) {this.ftpInput.add(ftpInput);}


    public HashSet getOutputDirectories() {
        return outputDirectories;
    }

    public void addOutputDirectory(String outputDirectory) {this.outputDirectories.add(outputDirectory);}

    public HashSet getUnknownQueues() {
        return unknownQueues;
    }

    public void addUnknownQueue(String unknownQueue) {
        this.unknownQueues.add(unknownQueue);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public HashSet getInputUrlPath() {
        return inputUrlPath;
    }

    public void addInputUrlPath(String inputUrlPath) {
        this.inputUrlPath.add(inputUrlPath);
    }

    public HashSet getOutputUrlPath() {
        return outputUrlPath;
    }

    public void addOutputUrlPath(String outputUrlPath) {
        this.outputUrlPath.add(outputUrlPath);
    }

    public HashMap getDatabaseConnections() {
        return databaseConnections;
    }

    public void addDatabaseConnection(String source, String expression) {
        this.databaseConnections.putIfAbsent(source, expression);
    }

    public HashMap getInputSoap() {
        return inputSoap;
    }

    public void addInputSoap(String url, String wsdl) {
        this.inputSoap.putIfAbsent(url, wsdl);
    }

    public HashSet getEsqlModules() {
        return esqlModules;
    }

    public void addEsqlModule(String module) {
        this.esqlModules.add(module);
    }

    public HashMap getOutputSoap() {
        return outputSoap;
    }

    public void addOutputSoap(String url, String wsdl) {
        this.outputSoap.putIfAbsent(url, wsdl);
    }

    public HashMap getOutputRest() {
        return outputRest;
    }

    public void addOutputRest(String operation, String definition) {
        this.outputRest.putIfAbsent(operation, definition);
    }

    public HashSet getJavaModules() {
        return javaModules;
    }

    public void addJavaModules(String javaModule) {
        this.javaModules.add(javaModule);
    }

    public HashSet getMappingModules() {
        return mappingModules;
    }

    public void addMappingModules(String mappingModule) {
        this.mappingModules.add(mappingModule);
    }

    public void print(){
        //TODO: print all properties cleanly
    }
}
