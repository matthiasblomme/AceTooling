package com.id.ace.environment.overview;

import com.id.ace.models.AceApplication;
import com.id.ace.models.AceIntegrationServer;
import com.id.ace.models.AceMessageflow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateOverview {

    public static String nodeName;
    public static String basePath;
    public static String qmgrName;

    public static void main(String[] args) {
        nodeName = args[0];
        basePath = args[1];
        qmgrName = args[2];

        MqEnvironment mqEnv = new MqEnvironment();
        mqEnv.buildenvironmentView(qmgrName);

        //printMQ(mqEnv);

        AceEnvironment aceEnv = new AceEnvironment();
        aceEnv.buildenvironmentView(nodeName, basePath);

        printFlowsCsv(aceEnv);

    }
    private static void printMQ(MqEnvironment mqEnv){
        System.out.println("### Queues");
        mqEnv.getQueueMap().forEach((k, v) -> System.out.println(k));
        System.out.println("\n### Subscriptions");
        mqEnv.getSubcriptionMap().forEach((k, v) -> System.out.println(k));
    }

    private static void printFlows(AceEnvironment aceEnv){
        System.out.println("\n### integration servers");
        for(String is: aceEnv.getIntegrationServers().keySet()){
            System.out.println(is);
            AceIntegrationServer aceIs = aceEnv.getIntegrationServers().get(is);
            for(String app: aceIs.getChildren().keySet()){
                System.out.println("    " + app);
                AceApplication aceApp = aceIs.getChildren().get(app);
                for(String flow: aceApp.getChildren().keySet()){
                    System.out.println("        "  + flow + " > " + aceApp.getChildren().get(flow).getState());
                    AceMessageflow msgFlow = aceApp.getFlow(flow);
                    if(!msgFlow.getInputQueues().isEmpty()){

                    }
                }
            }
        }
    }

    private static void printFlowsCsv(AceEnvironment aceEnv){
        System.out.println("Integration Server, Application, Flow, state, mqinput, mqouput, ...");
        for(String is: aceEnv.getIntegrationServers().keySet()){
            StringBuilder printLine = new StringBuilder("is");
            AceIntegrationServer aceIs = aceEnv.getIntegrationServers().get(is);
            for(String app: aceIs.getChildren().keySet()){
                printLine.append(",app");
                AceApplication aceApp = aceIs.getChildren().get(app);
                for(String flow: aceApp.getChildren().keySet()){
                    printLine.append(",flow");
                    printLine.append(",").append(aceApp.getChildren().get(flow).getState());
                    AceMessageflow msgFlow = aceApp.getFlow(flow);
                    printLine.append((msgFlow.getInputQueues().isEmpty()) ? "no" : "yes");
                    printLine.append((msgFlow.getOutputQueues().isEmpty()) ? "no" : "yes");
                }
            }
            System.out.println(printLine.toString());
        }
    }

    private static void printFlowDirs(AceEnvironment aceEnv) {
        System.out.println("\n### integration servers");
        for(String is: aceEnv.getIntegrationServers().keySet()){
            System.out.println(is);
            AceIntegrationServer aceIs = aceEnv.getIntegrationServers().get(is);
            for(String app: aceIs.getChildren().keySet()){
                System.out.println("    " + app);
                AceApplication aceApp = aceIs.getChildren().get(app);
                for(String flow: aceApp.getChildren().keySet()){
                    System.out.println("        "  + flow + " > " + aceApp.getChildren().get(flow).getState());
                    System.out.println("            inputDir: " + aceApp.getChildren().get(flow).getInputDirectories().toString());
                    System.out.println("            ftpInput: " + aceApp.getChildren().get(flow).getFtpInput().toString());
                    System.out.println("            inputQueue: " + aceApp.getChildren().get(flow).getInputQueues().toString());
                    System.out.println("            outputQueue: " + aceApp.getChildren().get(flow).getOutputQueues().toString());

                    aceApp.getChildren().get(flow).getProperties().keySet().forEach(k -> {
                        Matcher m = Pattern.compile(".*directory.*").matcher(k.toString().toLowerCase());
                        if (m.matches()) System.out.println("            dirAttr: " + aceApp.getChildren().get(flow).getProperties().get(k).toString());

                    });
                }
            }
        }
    }
}
