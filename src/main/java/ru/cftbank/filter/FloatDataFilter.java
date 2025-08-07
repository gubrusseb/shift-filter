package ru.cftbank.filter;

import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;

import java.nio.file.Path;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;

public class FloatDataFilter extends DataFilter {

    public FloatDataFilter(Path resultFileName, WriteType writeType) {
        super(resultFileName,writeType);
    }

    @Override
    protected boolean isCorrectString(String string) {
        try{
            Float.parseFloat(string);
            if(!string.contains(".") && !string.contains("E") && !string.contains("e")){
                return false;
            }
        }catch(NumberFormatException e){
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
                DoubleSummaryStatistics stats = resultStringList.stream()
                        .mapToDouble(Double::parseDouble)
                        .summaryStatistics();
                statistic.append("- Максимальное значение равно ").append(stats.getMax()).append('\n');
                statistic.append("- Минимальное значение равно ").append(stats.getMin()).append('\n');
                statistic.append("- Среднее значение равно ").append(stats.getAverage()).append('\n');
                statistic.append("- Сумма всех элементов равна ").append(stats.getSum()).append('\n');
            }
            statistic.append(String.format("- Количество элементов, записанных в файл %s, равно %d",
                    getResultFilePath(),
                    resultStringList.size()));
        }else{
            statistic.append("Фильтр не нашел подходящих строк в файлах");
        }
            return statistic.toString();
    }
}
