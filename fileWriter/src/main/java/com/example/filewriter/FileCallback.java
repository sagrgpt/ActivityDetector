package com.example.filewriter;

import java.io.File;

public interface FileCallback {
    void onSuccess(File file);

    void onFail(String err);
}
