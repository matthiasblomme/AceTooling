package com.id.ace.policies;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Stream;

public class searchPolicyUsage {

    static HashSet<String> csList = new HashSet<>();
    static String csPattern;

    public static void main(String[] args) {
        //Read cs file
        String csFileName = "C:\\ProgramData\\IBM\\MQSI\\components\\ACEMIG\\policies.txt";
        //String propFileName = "C:\\ProgramData\\IBM\\MQSI\\components\\ACEMIG\\properties.txt";
        String propFileName = "C:\\ProgramData\\IBM\\MQSI\\components\\ACEMIG\\properties_old.txt";
        csList = policiesListFile.readCsFile(csFileName);
        csPattern = ".*?'(" + String.join("|", csList) +  ")'.*?";
        //System.out.println(csPattern);
        System.out.println("IntegrationServer#Application#Msgflow#Policy");
        com.id.ace.properties.handleProperties.readPropertiesFile(propFileName, csPattern);
    }

}
