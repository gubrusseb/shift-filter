package ru.cftbank.filter;


import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.List;

public class IntegerDataFilter extends DataFilter {

    public IntegerDataFilter(Path resultFilePath, WriteType writeType) {
        super(resultFilePath, writeType);
    }

    @Override
    protected boolean isCorrectString(String string) {
        try {
            new BigInteger(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    protected String buildStatistic(StatisticType statisticType) {
        List<String> resultStringList = getResultStringList();

        if(resultStringList.isEmpty()){
            return "- Фильтр не нашел подходящих строк в файлах.";
        }

        StringBuilder statistic = new StringBuilder();

        BigInteger sum = new BigInteger(resultStringList.get(0));
        BigInteger max = sum;
        BigInteger min = sum;

        if (statisticType == StatisticType.FULL) {
            for (String resultString : resultStringList.subList(1, resultStringList.size())) {
                BigInteger current = new BigInteger(resultString);
                max = current.max(max);
                min = current.min(min);
                sum = sum.add(current);
            }

            statistic.append("- Максимальное значение равно ").append(max).append(";\n");
            statistic.append("- Минимальное значение равно ").append(min).append(";\n");
            statistic.append("- Среднее значение равно ").append(new BigDecimal(sum).divide(new BigDecimal(resultStringList.size()), 5,  RoundingMode.HALF_UP)).append(";\n");
            statistic.append("- Сумма всех элементов равна ").append(sum).append(";\n");
        }

        statistic.append(String.format("- Количество элементов, записанных в файл %s, равно %d.",
                getResultFilePath(),
                resultStringList.size()));

        return statistic.toString();
    }



    @Override
    public String toString(){
        return "Фильтр целых чисел";
    }
}
