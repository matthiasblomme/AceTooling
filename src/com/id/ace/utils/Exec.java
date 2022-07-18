package com.id.ace.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Exec
{
    public void Exec(String args[]){
        Exec("cmd.exe", args);
    }

    public void Exec(String executable, String args[]){
        Exec(System.getProperty("user.home"), executable, args);

    }

    public void Exec(String runDirectory, String executable, String args[])
    {
        try
        {
            ArrayList<String> command = new ArrayList<>();
            command.add(executable);
            command.addAll(List.of(args));

            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(runDirectory));
            builder.command(command);

            Process proc = builder.start();
            // any error message?
            StreamGobbler errorGobbler = new
                    StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new
                    StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            int exitVal = proc.waitFor();

            String output = String.join("\n", outputGobbler.getOutput());
            String error = String.join("\n", errorGobbler.getOutput());

            System.out.println("ExitValue: " + exitVal);
            System.out.println("Output: ");
            System.out.println(output);
            System.out.println("Error: ");
            System.out.println(error);
        } catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
}
