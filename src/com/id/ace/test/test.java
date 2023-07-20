package com.id.ace.test;

import com.id.ace.utils.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    static String quotePattern = ".*?(\".*?\").*?";
    static HashMap<String,String> replaceList = new HashMap<>();

    public static void main(String[] args) {
        String commandLine = "echo hello";

        int returnValue = 0;
        Command command = new Command();
        String[] commandParameters = command.ParseCommandString(commandLine);
        //check os

        //check .bat or .sh

        returnValue = command.Exec(commandParameters);

        ArrayList<String> output = command.getOutput();

        if (returnValue != 0) {
            ArrayList<String> error = command.getError();
            System.err.println(output);
            System.err.println(error);
            return;
        } else {
            System.out.println(output);
        }

    }

}
