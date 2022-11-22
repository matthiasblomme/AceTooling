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

public class HandleBarFile {

    static Pattern p = Pattern.compile("\\s+(.*)\\s*=\\s*(.*)");
    //TODO replace with dynamic server dir from env
    static String aceServerDir = "C:\\Program Files\\IBM\\ACE\\12.0.6.0\\server\\";

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
        //System.out.println("If you want to apply the properties files:");
        //System.out.println("mqsiapplybaroverride -b <bar file name> -p <properties file>");
    }

    public static Map<String,String> readBar(String barName) throws Exception {
        Map<String,String> data = new HashMap<>();

        //String[] command = {"cmd.exe", "/c", aceServerDir + "bin\\mqsireadbar.bat", "-b", barName, "-r"};
        String[] command = {"-b", barName, "-r"};
        System.out.println(Arrays.toString(command));

        Command comm = new Command();
        int returnVal = comm.Exec(aceServerDir + "bin\\mqsireadbar.bat", command);
        if(returnVal < 0) {
            throw new Exception("error during execute of " + command.toString());
        }

        ArrayList<String> output = comm.getOutput();

        System.out.println(output.toString());
        System.err.println(comm.getError().toString());


        //Process process = Runtime.getRuntime().exec(command);
        //BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        output.forEach(line -> {
            Matcher m = p.matcher(line);
            if (m.matches()) {
                data.put(m.group(1), m.group(2));
                //System.out.println(m.group(1) + " <> " + m.group(2));
            }
        });

        return data;
    }

    public static Map<String, String> compare(Map<String,String> source, Map<String,String> target) {
        Map<String,String> result = new HashMap<>();
        for (String key: source.keySet()) {
            if (!target.containsKey(key)) {
                System.out.println();
                System.out.println("Property " + key + " is not available in the new bar file, please check!");
                continue;
            }

            if (!target.get(key).equals(source.get(key))) {
                result.put(key, source.get(key));
            }
        }
        System.out.println();
        return result;
    }

    public static void writeProperties(String fileName, Map<String,String> data) throws IOException {
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path))
        {
            data.forEach((k,v) -> {
                try {
                    writer.write(k + "=" + v);
                    writer.write(System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("Written properties to " + fileName);
    }
}
