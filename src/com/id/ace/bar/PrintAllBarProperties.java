package com.id.ace.bar;

import java.util.Map;

import static com.id.ace.bar.BarUtils.readBar;
import static com.id.ace.bar.BarUtils.writeProperties;

public class PrintAllBarProperties {

    public static void main(String[] args) throws Exception {
        if (args.length < 1 ) {
            System.err.println("One of the parameters was not selected, please provide the source and target bar file");
            throw new RuntimeException();
        }
        String inputBar = args[0];
        Map<String,String> source = readBar(inputBar);

        writeProperties(inputBar.replace(".bar",".properties"), source);

        System.out.println();
        System.out.println("If you want to apply the properties files:");
        System.out.println("ibmint apply overrides <properties file> --bar-file <bar file name>");
    }

}
