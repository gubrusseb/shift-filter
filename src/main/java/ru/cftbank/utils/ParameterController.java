package ru.cftbank.utils;

import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParameterController {

    private final List<String> inputFileNames = new ArrayList<>();
    private WriteType writeType = WriteType.REWRITE;
    private StatisticType statisticType = null;
    private Path resultPath = Paths.get(System.getProperty("user.dir"));
    private String prefix = "";
    private StringBuilder warnings = new StringBuilder();

    public ParameterController(String... args){
        StringBuilder errors = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (isParam(arg)) {
                switch (arg) {
                    case "-f":
                        if (statisticType == StatisticType.SHORT) {
                            warnings.append("Внимание! Одновременно получены параметры -f и -s, будет выведена полная статистика.\n");
                        }
                        statisticType = StatisticType.FULL;
                        break;

                    case "-s":
                        if (statisticType == StatisticType.FULL) {
                            warnings.append("Внимание! Одновременно получены параметры -f и -s, будет выведена полная статистика.\n");
                        }
                        break;

                    case "-a":
                        writeType = WriteType.APPEND;
                        break;

                    case "-o":
                        if (!hasArgument(args, i)) {
                            errors.append("Ошибка! После параметра -o отсутствует путь для результатов.\n");
                        } else {
                            resultPath = resultPath.resolve(args[++i]);
                        }
                        break;

                    case "-p":
                        if (!hasArgument(args, i)) {
                            errors.append("Ошибка! После параметра -p отсутствует префикс.\n");
                        } else {
                            prefix = args[++i];
                        }
                        break;

                    default:
                        errors.append("Ошибка! Неизвестный параметр: ").append(arg).append(".\n");
                }
            } else {
                inputFileNames.add(arg);
            }
        }
        if(inputFileNames.isEmpty()){
            errors.append("Ошибка! Необходимо передать хотя бы одно имя файла.");
        }
        if (errors.length() > 0) {
            throw new RuntimeException(errors.toString());
        }

    }

    public  List<String> getInputFileNames() {
        return inputFileNames;
    }

    public  WriteType getWriteType() {
        return writeType;
    }

    public  StatisticType getStatisticType() {
        return statisticType;
    }

    public  Path getResultPath() {
        return resultPath;
    }

    public  String getPrefix() {
        return prefix;
    }


    public void outputInfo() {
        if (warnings != null && warnings.length() > 0) {
            System.out.println(warnings);
            System.out.println();
        }

        System.out.println("Информация по работе программы:");
        System.out.printf("- Файлы, которые будут проверены: %s;%n", inputFileNames);
        System.out.printf("- Вид отображаемой статистики: %s;%n",
                statisticType == StatisticType.FULL ? "полная статистика" : "краткая статистика");
        System.out.printf("- Если файлы результатов содержат информацию, то она будет %s;%n",
                writeType == WriteType.APPEND ? "дополнена результатом" : "перезаписана");


        System.out.printf("- Файлы результатов будут сохранены в каталог: %s;%n", resultPath);

        System.out.printf("- Префикс у файлов результатов: %s.%n",
                prefix.isEmpty() ? "отсутствует" : prefix);
    }

    private static boolean isParam(String str) {
        return str.startsWith("-");
    }

    private static boolean hasArgument(String[] args, int currentIndex) {
        return currentIndex + 1 < args.length && !isParam(args[currentIndex + 1]);
    }

    
}
