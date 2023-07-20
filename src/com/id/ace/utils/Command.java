package com.id.ace.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *
 */
public class Command
{
    StreamGobbler errorGobbler;
    StreamGobbler outputGobbler;
    ArrayList<String> output;
    ArrayList<String> error;
    String OS = "WIN";

    /**
     *
     * @return
     */
    public ArrayList<String> retreiveOutput() {
        return (outputGobbler != null ? outputGobbler.getOutput() : new ArrayList<String> ());
    }

    /**
     *
     * @return
     */
    public ArrayList<String> retreiveError() {
        return (errorGobbler != null ? errorGobbler.getOutput() : new ArrayList<String> ());
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getOutput() {
        return output;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getError() {
        return error;
    }

    /**
     *
     * @param args
     * @return
     */
    public int Exec(String[] args){
        //TODO check os for default run command
        ArrayList<String> list = new ArrayList<>();
        list.add("/c");
        list.addAll(Arrays.asList(args));
        return Exec("cmd", (String[]) list.toArray(new String[0]));
    }

    /**
     *
     * @param executable
     * @param args
     * @return
     */
    public int Exec(String executable, String[] args){
        return Exec(System.getProperty("user.home"), executable, args);

    }

    /**
     *
     * @param runDirectory
     * @param executable
     * @param args
     * @return
     */
    public int Exec(String runDirectory, String executable, String[] args)
    {
        int returnValue = -1;

        try
        {
            ArrayList<String> command = new ArrayList<>();
            command.add(executable);
            command.addAll(Arrays.asList(args));
            ProcessBuilder builder = new ProcessBuilder();
            Map<String, String> environment = builder.environment();
            builder.directory(new File(runDirectory));
            builder.command(command);
            System.out.println(runDirectory);
            System.out.println(command);
            Process proc = builder.start();
            errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();

            returnValue = proc.waitFor();

            output = retreiveOutput();
            error = retreiveError();

        }
        catch (Throwable t)
        {
            t.printStackTrace();
            error = new ArrayList<String> ();
            error.add(t.getMessage());
            // return error from exception, like
            // Cannot run program "cmd /c" (in directory "C:\Users\blmm_m"): CreateProcess error=2, The system cannot find the file specified
        }

        return returnValue;
    }

    /**
     *
     * @param commandString
     * @return
     */
    public String[] ParseCommandString(String commandString){
        String quotePattern = ".*?(\".*?\").*?";
        HashMap<String,String> replaceList = new HashMap<>();
        Pattern p = Pattern.compile(quotePattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commandString);

        int matchNumber = 0;
        while (m.find()) {
            matchNumber++;
            String replaceId = "##param_" + matchNumber + "##";
            replaceList.put(replaceId, m.group(1));
            commandString = commandString.replace(m.group(1), replaceId);
        }
        ArrayList<String> paramsListTemp = new ArrayList<>(Arrays.asList(commandString.split(" ")));

        ArrayList<String> paramList = new ArrayList<String>();

        paramsListTemp.forEach(k -> {
            paramList.add(replaceList.containsKey(k) ? replaceList.get(k) : k);
        });

        if (paramList.size() == 0) paramList.add(commandString) ;

        return (String[]) paramList.toArray(new String[0]);
    }

    /**
     *
     * @param outputData
     */
    public void WriteOutputToFile(ArrayList<String> outputData, String outputFile) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            for(String line: outputData) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
