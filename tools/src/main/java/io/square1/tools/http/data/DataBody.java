package io.square1.tools.http.data;

import java.io.IOException;
import java.io.OutputStream;

public interface DataBody {

    String getBodyContentType();
    void output(OutputStream outputStream) throws Exception;

}