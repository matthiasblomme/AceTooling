package com.id.ace.environment.overview;

import com.id.ace.models.AceApplication;
import com.id.ace.models.AceIntegrationServer;
import com.id.ace.models.AceLibrary;
import com.id.ace.models.AceMessageflow;
import com.id.ace.utils.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AceEnvironment {

    String flowPattern = "..*Message\\s+flow\\s+\\'(.*?)\\'.*?\\'(.*?)\\'.*?is\\s(\\w*).*?\\'(.*?)\\'.*?\\'(.*?)\\'.*";

    HashMap <String, AceIntegrationServer> integrationServers = new HashMap<String, AceIntegrationServer>();
    HashMap <String, AceLibrary> libraries = new HashMap<>();
    HashMap <String, AceApplication> applications = new HashMap<>();
    HashMap <String, AceMessageflow> messageFlows = new HashMap<>();

    public void buildenvironmentView(String nodeName){
        String[] commandString = {nodeName, "-r"};

        String nodeBasePath = "C:\\IBM\\Nodes\\V12NODE\\components\\" + nodeName + "\\servers";
        Command command = new Command();
        int exitValue = Command.Exec("mqsilist", commandString);

        ArrayList<String> output = command.getOutput();
        for(String line: output) parseFlowInfo(line);

        getAllMessagFlowFiles(nodeBasePath);
    }

    private void getAllMessagFlowFiles(String baseDir){
        //C:\IBM\Nodes\V12NODE\components\nodeName\servers\IS1\run\BTM_1\com\mbl\btm\httpflow.msgflow
        //D:\IBM\mqsi\Nodes\components\nodeName\servers\APEXAdapter\run
        List<String> fileList = new ArrayList<>();
        try {
            fileList = listFilesUsingFilesList(baseDir);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        for(String file : fileList){
            if file.endsWith(".msgflow") {

            } else if(file.endsWith(".msgflow.dfmxml")) {

            } else if(file.endsWith(".esql")) {

            } else if (file.endsWith(""))
        }

    }

    public List listFilesUsingFilesList(String dir) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), Integer.MAX_VALUE)) {
            return stream
                    .filter(file -> !Files.isDirectory((Path) file))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    private void readFlowDetails(){

    }

    private void parseFlowInfo(String flowInfo){
        AceMessageflow flow;
        AceApplication app;
        Pattern p = Pattern.compile(flowPattern);
        Matcher m = p.matcher(flowInfo);
        if (m.matches()) {
            String messageFlowName = m.group(1);
            String integrationServerName = m.group(2);
            String state = m.group(3);
            String applicationName = m.group(4);

            if (!messageFlows.containsKey(messageFlowName)){
                flow = new AceMessageflow(messageFlowName, integrationServerName, applicationName, state);
            } else {
                flow = messageFlows.get(messageFlowName);
            }
            messageFlows.putIfAbsent(flow.getName(), flow);
            app = checkApplicationExists(applicationName, flow);
            checkIntegrationServerExists(integrationServerName, app);
        }
    }

    private AceApplication checkApplicationExists(String name, AceMessageflow flow){
        AceApplication app;
        if(!applications.containsKey(name)) {
            app = new AceApplication(name, flow);
        } else {
            app = applications.get(name);
            app.addChild(flow);
        }
        applications.putIfAbsent(name, app);
        return app;
    }

    private void checkIntegrationServerExists(String name, AceApplication app){
        AceIntegrationServer is;;
        if(!integrationServers.containsKey(name)) {
            is = new AceIntegrationServer(name, app);
        } else {
            is = integrationServers.get(name);
            is.addChild(app);
        }
        integrationServers.putIfAbsent(name, is);
    }

    public HashMap<String, AceIntegrationServer> getIntegrationServers() {
        return integrationServers;
    }

    public HashMap<String, AceLibrary> getLibraries() {
        return libraries;
    }

    public HashMap<String, AceApplication> getApplications() {
        return applications;
    }

    public HashMap<String, AceMessageflow> getMessageFlows() {
        return messageFlows;
    }
}
