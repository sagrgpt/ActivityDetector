package com.example.csvwriter;

import android.os.Environment;
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
    private File file;
    private OutputStream outputStream;
    private String separatorColumn = ",";
    private String seperatorLine = "\r\n";

    /**
     * It is used to create a csv file by processing the data received from the user.
     *
     * @param fileName     Name of will create file
     * @param headerList   Header list of csv file
     * @param data         List of csv data
     * @param fileCallback Callback of file
     */
    public void createCsvFile(String fileName,
                              List<String> headerList,
                              List<String> data,
                              final FileCallback fileCallback) {
        fileName = fileName
              .replace(" ", "_")
              .replace(":", "_");

        file = new File(Environment.getExternalStorageDirectory() +
                        File.separator +
                        fileName +
                        ".csv");
        outputStream = null;

        try {
            file.createNewFile();
        } catch (IOException e) {
            fileCallback.onFail(e.getMessage());
        }

        List<String> headerListWithComma = Utils.separatorReplace(separatorColumn,
                                                                  seperatorLine,
                                                                  headerList);
        List<String> dataListWithComma = Utils.separatorReplace(separatorColumn,
                                                                seperatorLine,
                                                                data);

        file = writeDataToFile(file,
                               containAllData(headerListWithComma, dataListWithComma),
                               fileCallback);

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
     * Sets the line delimiter character(s) to be used (default: {@link CsvWriter#seperatorLine}).
     */
    public void setSeperatorLine(String seperatorLine) {
        this.seperatorLine = seperatorLine;
    }

    /**
     * @param file         Created csv file's object
     * @param dataList     List of csv data
     * @param fileCallback Callback of file
     */
    private File writeDataToFile(final File file,
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

            return file;

        }
        else {
            fileCallback.onFail("Couldn't create CSV file");
        }

        return file;
    }

    /**
     * Merge headerlist and datalist
     *
     * @param headerList Header list of csv
     * @param dataList   Data list of csv
     */
    private List<String> containAllData(List<String> headerList, List<String> dataList) {
        List<String> stringList = new ArrayList<>();
        for (String value : headerList) {
            stringList.add(value);
        }
        for (String value : dataList) {
            stringList.add(value);
        }

        return stringList;
    }
}
