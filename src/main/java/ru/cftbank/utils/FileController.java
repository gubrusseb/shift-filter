package ru.cftbank.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileController {
    public static List<String> getStringArray(String fileName) throws IOException {
        ArrayList<String> stringList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringList.add(line);
            }
        }
        return stringList;
    }

    public static void setStringArray(String fileName, List<String> stringList) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for(String s : stringList) {
                writer.write(s);
                writer.newLine();
            }
        }
    }
}
