package ru.cftbank.filter;

import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;
import ru.cftbank.utils.FileController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DataFilter {

    private final  List<String> resultStringList;
    private final Path resultFilePath;
    private final WriteType writeType;

    protected DataFilter(Path resultFilePath,WriteType writeType) {
        this.resultFilePath = resultFilePath;
        this.writeType = writeType;
        this.resultStringList = new ArrayList<>();
    }

    public void filter(String filePath) throws IOException{
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        List<String> strings = FileController.getStringArray(filePath);
        for(String string : strings){
            if (isCorrectString(string)){
                resultStringList.add(string);
            }
        }

    }

    public void save() throws IOException{
        if(!resultStringList.isEmpty()){
        FileController.setStringArray(resultFilePath, resultStringList,writeType);
        }
    }

    public String getStatistic(StatisticType statisticType){
        return buildStatistic(statisticType);
    }
    protected abstract boolean isCorrectString(String string);

    protected abstract String buildStatistic(StatisticType statisticType);

    public List<String> getResultStringList() {
        return Collections.unmodifiableList(resultStringList);
    }


    public Path getResultFilePath() {
        return resultFilePath;
    }

}
