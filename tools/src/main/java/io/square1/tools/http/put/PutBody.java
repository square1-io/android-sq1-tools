package io.square1.tools.http.put;

import java.io.IOException;
import java.io.OutputStream;

public interface PutBody {

          void output(OutputStream outputStream) throws IOException;

    }