package com.id.ace.bar;

import com.id.ace.utils.Command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BarUtils {
    public static Map<String,String> readBar(String barName) throws Exception {
        Map<String,String> data = new HashMap<>();
        String aceServerDir = System.getenv("PROSPECTIVE_MQSI_FILEPATH") + "\\";
        Pattern p = Pattern.compile("\\s+(.*)\\s*=\\s*(.*)");

        String[] command = {"-b", barName, "-r"};
        Command comm = new Command();
        int returnVal = comm.Exec(aceServerDir + "bin\\mqsireadbar.bat", command);
        if(returnVal < 0) {
            throw new Exception("error during execute of " + command.toString());
        }

        ArrayList<String> output = comm.getOutput();
        String errorText = comm.getError().toString();
         if (!errorText.equals("[]")) System.err.println(errorText);
        output.forEach(line -> {
            Matcher m = p.matcher(line);
            if (m.matches()) {
                data.put(m.group(1), m.group(2));
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
