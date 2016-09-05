package io.square1.tools.http.readers;

import java.io.InputStream;

/**
 * Created by roberto on 13/11/14.
 */
public interface DataReader<T> {
    public T readData(InputStream in) throws Exception;
}
