package ru.cftbank.utils;

import ru.cftbank.WriteType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileController {
    public static List<String> getStringArray(String filePath) throws IOException {
        ArrayList<String> stringList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringList.add(line);
            }
        }
        return stringList;
    }

    public static void setStringArray(Path filePath, List<String> stringList, WriteType writeType) throws IOException {

        Path parentDir = filePath.getParent();

        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        if (Files.exists(filePath) && !Files.isWritable(filePath)) {
            throw new IOException("Ошибка! Нет прав на запись в файл: " + filePath + ".");
        }

        FileWriter fileWriter;
        switch (writeType) {
            case APPEND:
                fileWriter = new FileWriter(filePath.toFile(), true);
                break;
            case REWRITE:
                fileWriter = new FileWriter(filePath.toFile(),false);
                break;
            default:
                throw new IOException("Ошибка! Неверный тип записи данных.");
        }

        try(BufferedWriter writer = new BufferedWriter(fileWriter)) {
            for(String s : stringList){
                writer.write(s);
                writer.newLine();
            }
        }

    }
}
