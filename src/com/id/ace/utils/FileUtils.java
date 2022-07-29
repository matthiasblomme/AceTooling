package com.id.ace.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static List<Path> find(String searchDirectory, String query){
        try (Stream<Path> files = Files.walk(Paths.get(searchDirectory))) {
            return files
                    .filter(f -> f.getFileName().toString().contains(query))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
