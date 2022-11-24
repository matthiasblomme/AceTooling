package com.id.ace.environment.overview;

import com.id.ace.models.AceApplication;
import com.id.ace.models.AceIntegrationServer;
import com.id.ace.models.AceLibrary;
import com.id.ace.models.AceMessageflow;
import com.id.ace.utils.Command;
import com.id.ace.utils.PrintWithProgressBar;

import java.io.BufferedReader;
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

    String flowPattern = ".*Message\\s+flow\\s+'(.*?)'.*?'(.*?)'.*?is\\s(\\w*).*?'(.*?)'.*?'(.*?)'.*";

    String nodeBasePath;
    HashMap <String, AceIntegrationServer> integrationServers = new HashMap<>();
    HashMap <String, AceLibrary> libraries = new HashMap<>();
    HashMap <String, AceApplication> applications = new HashMap<>();
    HashMap <String, AceMessageflow> messageFlows = new HashMap<>();

    String inputHttpPattern = ".*ComIbmWSInput.*?URLSpecifier=\"(.*?)\".*"; //flows and restapi
    String inputDbPattern = ".*ComIbmDatabaseInput.*?dataSource=\"(.*?)\".*?databaseInputExpression=\"(.*?)\".*";
    String inputQueuePattern = ".*ComIbmMQ(?:Input|Get).*?queueName=\"(.*?)\".*";
    String inputFilePattern = ".*ComIbmFileInput.*?inputDirectory=\"(.*?)\".*";
    String inputFtpPattern = ".*ComIbmFileInput.*?inputDirectory=\"(.*?)\".*?fileFtp=\"(.*?)\".*?remoteTransferType=\"(.*?)\".*fileFtpServer=\"(.*?)\".*fileFtpDirectory=\"(.*?)\".*";
    String inputSoapPattern = ".*ComIbmSOAPInput.*?wsdlFileName=\"(.*?)\".*?urlSelector=\"(.*?)\".*";

    String computePattern = ".*ComIbmCompute.*?dataSource=\"(.*?)\".*?computeExpression=\"(.*?)\".*";

    String outputHttpPattern = ".*ComIbmWSRequest.*?URLSpecifier=\"(.*?)\".*";
    String outputAsyncHttpPattern = ".*ComIbmHTTPAsyncRequest.*?URLSpecifier=\"(.*?)\".*";
    String outputQueuePattern = ".*ComIbmMQOutput.*?queueName=\"(.*?)\".*";
    String outputFilePattern = ".*ComIbmFileOutput.*?outputDirectory=\"(.*?)\".*";
    String outputSoapPattern = ".*ComIbmSOAPRequest.*?wsdlFileName=\"(.*?)\".*?webServiceURL=\"(.*?)\".*";
    String outputSoapAsyncPattern = ".*ComIbmSOAPAsyncRequest.*?wsdlFileName=\"(.*?)\".*?webServiceURL=\"(.*?)\".*";
    String outputRestPattern = ".*ComIbmRESTRequest.*?definitionFile=\"(.*?)\".*?operationName=\"(.*?)\".*";
    String outputRestAsyncPattern = ".*ComIbmRESTAsyncRequest.*?definitionFile=\"(.*?)\".*?operationName=\"(.*?)\".*";
    String javaComputePattern = ".*ComIbmJavaCompute.*?javaClass=\"(.*?)\".*";
    String mappingNodePattern = ".*ComIbmMSLMapping.*?mappingExpression=\"(.*?)\".*";
    String udpPattern = "";
    String attributePattern = ".*?<Attribute uuid=\"(.*?)\".*?>(.*?)<.*?";

    public void buildenvironmentView(String nodeName, String basePath){
        String[] commandString = {nodeName, "-r"};

        //"C:\\IBM\\Nodes\\V12NODE\\components\\" + nodeName + "\\servers";
        nodeBasePath = basePath + "\\" + nodeName + "\\servers";
        Command comm = new Command();
        int exitValue = Command.Exec("mqsilist", commandString);

        ArrayList<String> output = comm.getOutput();

        if (exitValue > 0) {
            ArrayList<String> error = comm.getError();
            System.err.println(output);
            System.err.println(error);
            return;
        }
        for(String line: output) parseFlowInfo(line);
        readAllRuntimeFiles(nodeBasePath);
        System.out.println("handled all message flows");
    }

    private void readAllRuntimeFiles(String baseDir){
        //C:\IBM\Nodes\V12NODE\components\nodeName\servers\IS1\run\BTM_1\com\mbl\btm\httpflow.msgflow
        //D:\IBM\mqsi\Nodes\components\nodeName\servers\APEXAdapter\run
        List<String> fileList = new ArrayList<>();
        try {
            fileList = listFilesUsingFilesList(baseDir);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        PrintWithProgressBar printer =  new PrintWithProgressBar("Scanning runtime", 100);
        int totalFiles = fileList.size();
        int fileNumber = 0;

        for(String file : fileList){
            printer.print(Math.round(fileNumber++ / totalFiles));
            if (file.endsWith(".msgflow")) {
                readMessageFlow(file);
            } else if(file.endsWith(".msgflow.dfmxml")) { //migrated flow
                //TODO: test
                readMessageFlow(file);
                //identical to msgflow
            } else if(file.endsWith(".esql")) {
                //skip for now
                //check for destination properties
                //map with msgflow
            } else if (file.endsWith(".subflow")) {
                //skip for now
                //identical to msgflow
            } else if (file.endsWith(".subflow.dfmxml")) { //migrated subflow
                //skip for now
                //identical to msgflow
            } else if (file.endsWith("broker.xml")) {
                //parse property overrides
                //file: IS\run\APP\META-INF\broker.xml
                //<ConfigurableProperty uri="udp#udp1" override="hahahaha">
                // uri = flow # property
                //<ConfigurableProperty uri="udp#HTTP Input.URLSpecifier" override="/tes">
                // uri = flow # node . property
            }
        }
    }

    public List listFilesUsingFilesList(String dir) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), Integer.MAX_VALUE)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    public void readMessageFlow(String file){
        AceMessageflow flow;
        Pattern flowDirNamePattern = Pattern.compile(nodeBasePath.replace("\\","\\\\") + "\\\\(.*?)\\\\run\\\\(.*?)\\\\(.*)");
        Matcher m = flowDirNamePattern.matcher(file);
        if(m.matches()) {
            flow = getOrCreateMessageFlow(m.group(3),m.group(2), m.group(1));
        } else {
            System.err.println("Can't parse file path for: " + file);
            return;
        }

        try(BufferedReader br = Files.newBufferedReader(Paths.get(file))) {
            String line;
            while((line = br.readLine()) != null){
                m = Pattern.compile(inputHttpPattern).matcher(line);
                if (m.matches()) flow.addInputUrlPath(m.group(1));

                m = Pattern.compile(inputDbPattern).matcher(line);
                if (m.matches()) flow.addDatabaseConnection(m.group(1), m.group(2));

                m = Pattern.compile(inputQueuePattern).matcher(line);
                if (m.matches())   flow.addInputQueue(m.group(1));

                m = Pattern.compile(inputFilePattern).matcher(line);
                if (m.matches()) flow.addInputDirectory(m.group(1));

                //TODO: expand to all ftp properties
                m = Pattern.compile(inputFtpPattern).matcher(line);
                if (m.matches()) {
                    if (m.group(2).equals("true"))
                        flow.addFtpInput(m.group(5));
                }

                m = Pattern.compile(inputSoapPattern).matcher(line);
                if (m.matches()) flow.addInputSoap(m.group(2), m.group(1));

                m = Pattern.compile(computePattern).matcher(line);
                if (m.matches()) {
                    flow.addEsqlModule(m.group(2));
                    if (!m.group(1).equals("")) flow.addDatabaseConnection(m.group(1), m.group(2));
                };

                m = Pattern.compile(outputHttpPattern).matcher(line);
                if (m.matches()) flow.addOutputUrlPath(m.group(1));

                m = Pattern.compile(outputAsyncHttpPattern).matcher(line);
                if (m.matches()) flow.addOutputUrlPath(m.group(1));

                m = Pattern.compile(outputQueuePattern).matcher(line);
                if (m.matches()) flow.addOutputQueue(m.group(1));

                m = Pattern.compile(outputFilePattern).matcher(line);
                if (m.matches()) flow.addOutputDirectory(m.group(1));

                m = Pattern.compile(outputSoapPattern).matcher(line);
                if (m.matches()) flow.addOutputSoap(m.group(2), m.group(1));

                m = Pattern.compile(outputSoapAsyncPattern).matcher(line);
                if (m.matches()) flow.addOutputSoap(m.group(2), m.group(1));

                m = Pattern.compile(outputRestPattern).matcher(line);
                if (m.matches()) flow.addOutputRest(m.group(2), m.group(1));

                m = Pattern.compile(outputRestAsyncPattern).matcher(line);
                if (m.matches()) flow.addOutputRest(m.group(2), m.group(1));

                m = Pattern.compile(javaComputePattern).matcher(line);
                if (m.matches()) flow.addJavaModules(m.group(1));

                m = Pattern.compile(mappingNodePattern).matcher(line);
                if (m.matches()) flow.addMappingModules(m.group(1));

                //TODO: handle all properties
                m = Pattern.compile(".*fileFtpDirectory=\"(.*?)\".*").matcher(line);
                if (m.matches()) flow.addFtpInput(m.group(1));

                //TODO handle pattern, put while for all propess
                m = Pattern.compile(attributePattern).matcher(line);
                while(m.find()){
                    flow.addProperty(m.group(1), m.group(2));
                }

            }
        } catch (IOException e) {
            System.err.println("Something went wrong trying to read: " + file);
            System.err.println(e.getMessage());
        }
    }

    private AceMessageflow getOrCreateMessageFlow(String name, String appName, String isName) {
        //TODO check if iss exissstss
        Matcher m = Pattern.compile("([\\w+.\\\\]*?)(\\w+\\.\\w+)").matcher(name);
        String shortName = name;
        if (m.matches()) shortName = m.group(2).replace(".msgflow", "");

        if (this.messageFlows.containsKey(shortName)) return this.messageFlows.get(shortName);

        AceMessageflow flow = new AceMessageflow(name.replace("\\","."), isName, appName, "unknown");
        messageFlows.putIfAbsent(flow.getName(), flow);

        if(!integrationServers.containsKey(isName)){
            AceApplication app = new AceApplication(appName);
            applications.putIfAbsent(appName, app);
            AceIntegrationServer is = new AceIntegrationServer(isName, app);
            integrationServers.putIfAbsent(is.getName(), is);
            app.addChild(flow);
        }
        return flow;
    }

    private void readSubFlow(String file){

    }

    private void readMigratedMessageFlow(String file){

    }

    private void readMigratedSubflow(String file){

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
            flow = getOrCreateMessageFlow(messageFlowName, applicationName, integrationServerName);
            flow.setState(state);
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
