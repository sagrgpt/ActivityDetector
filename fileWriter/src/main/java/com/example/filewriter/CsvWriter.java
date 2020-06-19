package com.example.filewriter;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CsvWriter {
    private OutputStream outputStream;
    private String separatorColumn = ",";
    private String separatorLine = "\r\n";

    /**
     * It is used to create a csv file by processing the data received from the user.
     *
     * @param headerList   Header list of csv file
     * @param data         List of csv data
     * @param fileCallback Callback of file
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createCsvFile(File file,
                              List<String> headerList,
                              List<String> data,
                              final FileCallback fileCallback) {
        outputStream = null;

        try {
            file.createNewFile();
        } catch (IOException e) {
            fileCallback.onFail(e.getMessage());
        }

        List<String> headerListWithComma = Utils.separatorReplace(separatorColumn,
                                                                  separatorLine,
                                                                  headerList);
        List<String> dataListWithComma = Utils.separatorReplace(separatorColumn,
                                                                separatorLine,
                                                                data);

        writeDataToFile(file, containAllData(headerListWithComma, dataListWithComma), fileCallback);

        fileCallback.onSuccess(file);
    }

    /**
     * Sets the column delimiter character(s) to be used (default:
     * {@link CsvWriter#separatorColumn}).
     */
    public void setSeparatorColumn(String separatorColumn) {
        this.separatorColumn = separatorColumn;
    }

    /**
     * Sets the line delimiter character(s) to be used (default: {@link CsvWriter#separatorLine}).
     */
    public void setSeparatorLine(String separatorLine) {
        this.separatorLine = separatorLine;
    }

    /**
     * @param file         Created csv file's object
     * @param dataList     List of csv data
     * @param fileCallback Callback of file
     */
    private void writeDataToFile(final File file,
                                 List<String> dataList,
                                 final FileCallback fileCallback) {

        if (file.exists()) {
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                fileCallback.onFail(e.getMessage());
            }

            final OutputStream finalFo = outputStream;

            String[] headerArray = new String[dataList.size() - 1];
            headerArray = dataList.toArray(headerArray);

            Observable
                  .fromArray(headerArray)
                  .subscribe(new Observer() {
                      @Override
                      public void onSubscribe(Disposable d) {
                      }

                      @Override
                      public void onNext(Object o) {
                          String dataWithLineBreak = (String) o;
                          try {
                              finalFo.write(dataWithLineBreak.getBytes());
                          } catch (IOException e) {
                              fileCallback.onFail(e.getMessage());
                          }
                      }

                      @Override
                      public void onError(Throwable e) {
                          fileCallback.onFail(e.getMessage());
                      }

                      @Override
                      public void onComplete() {
                          try {
                              finalFo.close();
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                  });
        }
        else {
            fileCallback.onFail("Couldn't create CSV file");
        }
    }

    /**
     * Merge headerlist and datalist
     *
     * @param headerList Header list of csv
     * @param dataList   Data list of csv
     */
    private List<String> containAllData(List<String> headerList, List<String> dataList) {
        List<String> stringList = new ArrayList<>();
        stringList.addAll(headerList);
        stringList.addAll(dataList);
        return stringList;
    }
}
