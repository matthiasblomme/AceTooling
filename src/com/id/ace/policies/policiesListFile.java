package com.id.ace.policies;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Stream;

public class policiesListFile {
    /*
    The list that contains the policies to search for
     */
    static HashSet<String> csList = new HashSet<>();

    static HashSet<String> readCsFile(String csFileName){
        try(Stream<String> stream = Files.lines(Paths.get(csFileName))) {
            stream.forEach(k -> csList.add(k.substring(0,k.indexOf("."))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csList;
        //csList.stream().sorted().forEach(System.out::println);
    }

}
