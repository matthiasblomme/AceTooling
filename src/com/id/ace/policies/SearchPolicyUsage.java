package com.id.ace.policies;

import com.id.ace.properties.HandleProperties;

import java.util.HashSet;

public class SearchPolicyUsage {

    static HashSet<String> csList = new HashSet<>();
    static String csPattern;

    public static void main(String[] args) {
        //Read cs file
        String csFileName = "C:\\ProgramData\\IBM\\MQSI\\components\\ACEMIG\\policies.txt";
        //String propFileName = "C:\\ProgramData\\IBM\\MQSI\\components\\ACEMIG\\properties.txt";
        String propFileName = "C:\\ProgramData\\IBM\\MQSI\\components\\ACEMIG\\properties_old.txt";
        csList = PoliciesList.readCsFile(csFileName);
        csPattern = ".*?'(" + String.join("|", csList) +  ")'.*?";
        //System.out.println(csPattern);
        System.out.println("IntegrationServer#Application#Msgflow#Policy");
        HandleProperties.readPropertiesFile(propFileName, csPattern);
    }

}
