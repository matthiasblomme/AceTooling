package com.id.ace.properties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleProperties {

    static String instegrationServerPatter = "IS=\\(.*\\)\\s";
    static String applicationPattern = "Application";
    static String msgflowPattern = "\\s+MessageFlow";
    static String msgflowPattern2 = "^MessageFlow";
    static String labelPattern = "\\s+label='\\(.*?\\)'";

    public static void readPropertiesFile(String propFileName, String searchPattern) {
        try(BufferedReader br = new BufferedReader(new FileReader(propFileName))) {
            String line;
            boolean applicationFound = false;
            boolean flowFound = false;
            String applicationName = "";
            String flowName = "";
            String integrationServerName = "";
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile(instegrationServerPatter);
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    integrationServerName = m.group(1);
                }

                p = Pattern.compile(applicationPattern);
                m = p.matcher(line);
                if (m.matches()) {
                    applicationFound = true;
                    flowFound = false;
                    flowName = "";
                    applicationName = "";
                }

                p = Pattern.compile(msgflowPattern2);
                m = p.matcher(line);
                if (m.matches()) {
                    applicationFound = false;
                    applicationName = "";
                    flowFound = true;
                    flowName = "";
                }

                p = Pattern.compile(msgflowPattern);
                m = p.matcher(line);
                if (m.matches()) {
                    flowFound = true;
                    flowName = "";
                }

                p = Pattern.compile(labelPattern);
                m = p.matcher(line);
                if (m.matches()) {
                    if (applicationFound && applicationName.equals("")) applicationName = m.group(1);
                    if (flowFound && flowName.equals("")) {
                        flowName = m.group(1);
                        //System.out.println(applicationName + " -- " + flowName);
                    }
                }

                p = Pattern.compile(searchPattern);
                m = p.matcher(line);
                if (m.matches()) {
                    if (!line.contains("uuid") && !line.contains("label") && !line.contains("esql"))
                        System.out.println(integrationServerName + "#" + applicationName + "#" + flowName + "#" + m.group(1));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
