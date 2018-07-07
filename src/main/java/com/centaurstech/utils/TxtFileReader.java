package com.centaurstech.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Feliciano on 7/6/2018.
 */
public class TxtFileReader {

    /**
     * Read a txt file into a set
     * @param file
     * @param set
     * @return
     * @throws IOException
     */
    public static Set<String> readSet(String file, Set<String> set) throws IOException {
        Path path = Paths.get(file);
        List<String> docs = Files.readAllLines(path);
        set.addAll(docs);
        return set;
    }

    /**
     * Read a txt file into a map, split by whitespace
     * @param map
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String, String> readMap(Map<String, String> map, String file) throws IOException {
        return readMap(map, file, "\\s+");
    }

    /**
     * Read a txt file into a map, split by a regular expression
     * @param map
     * @param file
     * @param regex
     * @return
     * @throws IOException
     */
    public static Map<String, String> readMap(Map<String, String> map, String file, String regex) throws IOException {
        Path path = Paths.get(file);
        List<String> docs = Files.readAllLines(path);
        for (String doc : docs) {
            String[] split = doc.split(regex);
            map.put(split[0], split[1]);
        }
        return map;
    }

}
