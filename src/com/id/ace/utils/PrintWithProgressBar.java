package com.id.ace.utils;

import java.util.stream.Stream;

public class PrintWithProgressBar {

    String displayMessage;
    int length;
    char incomplete = '░'; // U+2591 Unicode Character
    char complete = '█'; // U+2588 Unicode Character
    StringBuilder builder = new StringBuilder();
    public PrintWithProgressBar(String displayMessage, int length) {
        this.displayMessage = displayMessage;
        this.length = length;
        Stream.generate(() -> incomplete).limit(length).forEach(builder::append);
        System.out.println(displayMessage);
    }

    public void print(int status)
    {
            builder.replace(status,status+1,String.valueOf(complete));
            String progressBar = "\r"+builder;
            builder.append(" ");
            System.out.print(progressBar);
    }

    public static void main(String[] args)
    {
        PrintWithProgressBar printer =  new PrintWithProgressBar("Loading", 100);
        for(int i = 0; i < 25; i++)
        {
            printer.print(i);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException ignored)
            {

            }
        }
    }
}
