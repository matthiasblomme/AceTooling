package com.id.ace.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command
{
    static StreamGobbler errorGobbler;
    static StreamGobbler outputGobbler;

    public static ArrayList getOutput() {
        return outputGobbler.getOutput();
    }

    public static ArrayList getError() {
        return errorGobbler.getOutput();
    }

    static ArrayList output;
    static ArrayList error;
    public static int Exec(String args[]){
        return Exec("cmd /c /wait", args);
    }

    public static int Exec(String executable, String[] args){
        return Exec(System.getProperty("user.home"), executable, args);

    }

    public static int Exec(String runDirectory, String executable, String[] args)
    {
        try
        {
            ArrayList<String> command = new ArrayList<>();
            command.add(executable);
            command.addAll(Arrays.asList(args));
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(runDirectory));
            builder.command(command);
            System.out.println(runDirectory);
            System.out.println(command);
            //Process proc = Runtime.getRuntime().exec(executable, args);
            Process proc = builder.start();
            // any error message?
            errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            return proc.waitFor();
        } catch (Throwable t)
        {
            t.printStackTrace();
        }
        return -1;
    }
}
