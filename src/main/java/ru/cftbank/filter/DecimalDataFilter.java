package ru.cftbank.filter;

import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class DecimalDataFilter extends DataFilter {

    public DecimalDataFilter(Path resultFileName, WriteType writeType) {
        super(resultFileName,writeType);
    }

    @Override
    protected boolean isCorrectString(String string) {
        if (!string.contains(".") && !string.contains("E") && !string.contains("e")) {
            return false;
        }
        try {
            new BigDecimal(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected String buildStatistic(StatisticType statisticType) {
        StringBuilder statistic = new StringBuilder();
        List<String> resultStringList = getResultStringList();

        if(resultStringList.isEmpty()){
            statistic.append("- Фильтр не нашел подходящих строк в файлах.");
        }

        BigDecimal sum = new BigDecimal(resultStringList.get(0));
        BigDecimal max = sum;
        BigDecimal min = sum;

        if (statisticType == StatisticType.FULL) {
            for (String resultString : resultStringList.subList(1, resultStringList.size())) {
                BigDecimal current = new BigDecimal(resultString);
                max = current.max(max);
                min = current.min(min);
                sum = sum.add(current);
            }

            statistic.append("- Максимальное значение равно ").append(max).append(";\n");
            statistic.append("- Минимальное значение равно ").append(min).append(";\n");
            statistic.append("- Среднее значение равно ").append(sum.divide(new BigDecimal(resultStringList.size()), 5,  RoundingMode.HALF_UP)).append(";\n");
            statistic.append("- Сумма всех элементов равна ").append(sum).append(";\n");
        }

        statistic.append(String.format("- Количество элементов, записанных в файл %s, равно %d.",
                getResultFilePath(),
                resultStringList.size()));

            return statistic.toString();
    }


    @Override
    public String toString(){
        return "Фильтр чисел с плавающей точкой";
    }
}
