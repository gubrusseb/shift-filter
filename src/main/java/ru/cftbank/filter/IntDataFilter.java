package ru.cftbank.filter;


import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;

import java.nio.file.Path;
import java.util.IntSummaryStatistics;
import java.util.List;

public class IntDataFilter extends DataFilter{

    public IntDataFilter(Path resultFilePath, WriteType writeType) {
        super(resultFilePath,writeType);
    }

    @Override
    protected boolean isCorrectString(String string) {
        try{
            Long.parseLong(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    protected String buildStatistic(StatisticType statisticType) {
        StringBuilder statistic = new StringBuilder();

        List<String> resultStringList = getResultStringList();
        if(!resultStringList.isEmpty()){
            if (statisticType == StatisticType.FULL) {
                IntSummaryStatistics stats = resultStringList.stream()
                        .mapToInt(Integer::parseInt)
                        .summaryStatistics();
                statistic.append("- Максимальное значение равно ").append(stats.getMax()).append(";\n");
                statistic.append("- Минимальное значение равно ").append(stats.getMin()).append(";\n");
                statistic.append("- Среднее значение равно ").append(stats.getAverage()).append(";\n");
                statistic.append("- Сумма всех элементов равна ").append(stats.getSum()).append(";\n");
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
        return "Фильтр целых чисел";
    }
}
