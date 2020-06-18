package com.example.csvwriter;

import java.io.File;

public interface FileCallback {
    void onSuccess(File file);

    void onFail(String err);
}
