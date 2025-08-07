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
    private boolean isSaved = false;

    protected DataFilter(Path resultFilePath,WriteType writeType) {
        this.resultFilePath = resultFilePath;
        this.writeType = writeType;
        this.resultStringList = new ArrayList<>();
    }

    public void filter(String filePath) throws IOException{
        isSaved = false;
        if (filePath == null) {
            throw new IllegalArgumentException("Ошибка! Путь до файла не может быть пустым.");
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
            try{
                FileController.setStringArray(resultFilePath, resultStringList,writeType);
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        isSaved = true;
    }

    public boolean isSaved(){
        return isSaved;
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
