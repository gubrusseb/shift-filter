package ru.cftbank.filter;

import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StringDataFilter extends DataFilter{

    public StringDataFilter(Path resultFileName, WriteType writeType) {
        super(resultFileName,writeType);
    }

    @Override
    protected boolean isCorrectString(String string) {
        try{
            Float.parseFloat(string);
            return false;
        }catch (NumberFormatException e){
            return true;
        }
    }

    @Override
    protected String buildStatistic(StatisticType statisticType) {
        StringBuilder statistic = new StringBuilder();

        List<String> resultStringList = getResultStringList();
        if(!resultStringList.isEmpty()){
            if(statisticType == StatisticType.FULL){
                Optional<String> maxLengthStringOptional = resultStringList.stream()
                        .max(Comparator.comparingInt(String::length));

                Optional<String> minLengthStringOptional = resultStringList.stream()
                        .min(Comparator.comparingInt(String::length));

                if(maxLengthStringOptional.isPresent()){
                    String maxLengthString = maxLengthStringOptional.get();
                    statistic.append(String.format("- Самая длинная строка %s (длина %d);",maxLengthString,maxLengthString.length())).append("\n");
                }
                if(minLengthStringOptional.isPresent()){
                    String minLengthString = minLengthStringOptional.get();
                    statistic.append(String.format("- Самая короткая строка %s (длина %d);",minLengthString,minLengthString.length())).append("\n");

                }
            }

            statistic.append(String.format("- Количество элементов, записанных в файл %s, равно %d.",
                    getResultFilePath(),
                    resultStringList.size()));
        }else{
            statistic.append("- Фильтр не нашел подходящих строк в файлах.");
        }

        return statistic.toString();
    }

    @Override
    public String toString(){
        return "Фильтр строк";
    }
}
