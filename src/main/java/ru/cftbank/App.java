package ru.cftbank;

import ru.cftbank.filter.DataFilter;
import ru.cftbank.filter.DecimalDataFilter;
import ru.cftbank.filter.IntegerDataFilter;
import ru.cftbank.filter.StringDataFilter;
import ru.cftbank.utils.ParameterController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


public class App {

    private static ParameterController parameterController;
    public static void main(String[] args){
        parameterController = new ParameterController(args);
        parameterController.outputInfo();


        List<DataFilter> dataFilters = getDataFilters();

        System.out.println("\nЗапуск работы программы:");

        System.out.println("- Чтение файлов началось.");
        scanFiles(dataFilters);
        System.out.println("- Чтение файлов завершено.");

        System.out.println("- Запись результата началась.");
        writeResult(dataFilters);
        System.out.println("- Запись результата завершена.");

        System.out.println("\nВывод статистики: ");
        outputStatistic(dataFilters);
    }


    private static List<DataFilter> getDataFilters(){

        Path fullPath = parameterController.getResultPath();
        String prefix = parameterController.getPrefix();
        WriteType writeType = parameterController.getWriteType();

        return Arrays.asList(
                new IntegerDataFilter(fullPath.resolve( prefix + "integers.txt"),writeType),
                new DecimalDataFilter(fullPath.resolve(prefix + "floats.txt"),writeType),
                new StringDataFilter(fullPath.resolve(prefix + "strings.txt"),writeType));
    }

    private static void scanFiles(List<DataFilter> dataFilters) {

        List<String> inputFileNames = parameterController.getInputFileNames();
        for(String inputFileName : inputFileNames){
            for(DataFilter dataFilter : dataFilters){
                try{
                    dataFilter.filter(inputFileName);
                }catch(IOException e){
                    System.out.printf("Внимание! Файл %s не был найден, он пропущен.%n", inputFileName);
                    break;
                }
            }
        }
    }

    private static void writeResult(List<DataFilter> dataFilters) {
        for(DataFilter dataFilter : dataFilters){
            try {
                dataFilter.save();
            } catch (IOException e) {
                System.out.printf("Внимание! Произошла ошибка при сохранении результата в файл %s: %s.%n", dataFilter.getResultFilePath(),e);
            }
        }
    }

    private static void outputStatistic(List<DataFilter> dataFilters) {
        for(DataFilter dataFilter : dataFilters){
            System.out.println(dataFilter);
            if(dataFilter.isSaved()){
            System.out.println(dataFilter.getStatistic(parameterController.getStatisticType()));
            }else{
                System.out.println("- Фильтр не смог сохранить данные в файл.");
            }
            System.out.println();
        }
    }
}
