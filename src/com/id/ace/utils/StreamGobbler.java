package com.id.ace.utils;
import java.util.*;
import java.io.*;
class StreamGobbler extends Thread
{
    InputStream is;
    String type;

    ArrayList<String> result;

    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    public void run()
    {
        try
        {
            result = new ArrayList<>();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                result.add(line);
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public ArrayList<String> getOutput()
    {
        return result;
    }
}
