package ru.cftbank.utils;

import ru.cftbank.WriteType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileController {

    private FileController(){
    }

    public static List<String> getStringArray(String filePath) throws IOException {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()){
                stringList.add(line);
                }
            }
        }
        return stringList;
    }

    public static void setStringArray(Path filePath, List<String> stringList, WriteType writeType) throws IOException {
        Objects.requireNonNull(writeType, "Тип записи не может быть null");

        Path parentDir = filePath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(filePath.toFile(), writeType == WriteType.APPEND),
                        StandardCharsets.UTF_8))) {
            for (String s : stringList) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Ошибка записи в файл: " + filePath, e);
        }
    }
}