package ru.cftbank.filter;

import ru.cftbank.StatisticType;
import ru.cftbank.WriteType;
import ru.cftbank.utils.FileController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class DataFilter {

    private List<String> resultStringList;
    private Path resultFilePath;
    private WriteType writeType;

    public DataFilter(Path resultFilePath,WriteType writeType) {
        this.resultFilePath = resultFilePath;
        this.writeType = writeType;
        this.resultStringList = new ArrayList<>();
    }

    public void filter(String filePath) throws IOException{
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
        return resultStringList;
    }


    protected void setResultStringList(List<String> resultStringList) {

    }

    public Path getResultFilePath() {
        return resultFilePath;
    }

}
