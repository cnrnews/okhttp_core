package com.imooc.core;

import java.io.IOException;
import java.io.OutputStream;

public interface Bindary {
    long fileLength();
    String mimType();
    String fileName();
    void onWrite(OutputStream outputStream) throws IOException;
}
