package com.id.ace.environment.overview;

import com.id.ace.models.AceApplication;
import com.id.ace.models.AceIntegrationServer;
import com.id.ace.models.AceMessageflow;

public class GenerateOverview {

    public static String nodeName;

    public static void main(String[] args) {
        nodeName = args[0];
        MqEnvironment mqEnv = new MqEnvironment();
        mqEnv.getQueueList();
        mqEnv.getSubscriptionList();;

        printMQ(mqEnv);

        AceEnvironment aceEnv = new AceEnvironment();
        aceEnv.buildenvironmentView(nodeName);

        printFlows(aceEnv);


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
                }
            }
        }
    }
}
