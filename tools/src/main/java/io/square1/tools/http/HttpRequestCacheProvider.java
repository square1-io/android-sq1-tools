package io.square1.tools.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by roberto on 20/11/14.
 */
public interface HttpRequestCacheProvider {

    public InputStream getInputStream(String url) throws IOException;
    public InputStream saveInputStream(String url, InputStream inputStream) throws IOException;

}
