package io.square1.tools.http.data.element;

import java.io.IOException;
import java.io.OutputStream;

public interface MultipartElement {

          String getHeader();
          int getSize();
          void write(OutputStream outputStream) throws IOException;

    }