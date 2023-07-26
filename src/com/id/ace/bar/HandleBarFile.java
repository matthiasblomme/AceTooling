package com.id.ace.bar;

import com.id.ace.utils.Command;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.id.ace.bar.BarUtils.*;

public class HandleBarFile {

    public static void main(String[] args) throws Exception {
        if (args.length < 2 ) {
            System.err.println("One of the parameters was not selected, please provide the source and target bar file");
            throw new RuntimeException();
        }
        String sourceBar = args[0];
        String targetBar = args[1];
        Map<String,String> source = readBar(sourceBar);
        Map<String,String> target = readBar(targetBar);
        Map<String,String> result = compare(source, target);

        writeProperties(sourceBar.replace(".bar",".properties"), source);
        writeProperties(targetBar.replace(".bar",".properties"), target);

        if (result.isEmpty()) {
            System.out.println();
            System.out.println("No differences between bars");
            return;
        } else {
            System.out.println();
            System.out.println("The following settings are different:");
            result.forEach((k,v) -> System.out.println(k+"="+v));
        }
        System.out.println();
        writeProperties((new File(targetBar).getParent())+"\\overwrite.properties",result);

        System.out.println();
    }
}
