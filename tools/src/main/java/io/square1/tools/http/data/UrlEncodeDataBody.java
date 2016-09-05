package io.square1.tools.http.data;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.square1.tools.http.data.DataBody;

/**
 * Created by roberto on 15/07/2016.
 */
public class UrlEncodeDataBody implements DataBody {

    private HashMap<String,String> mKeyValuesToSend;

    public UrlEncodeDataBody(HashMap<String, String> keyValues){
        mKeyValuesToSend = keyValues;
    }

    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    public void output(OutputStream outputStream) throws Exception {

        if(mKeyValuesToSend == null || mKeyValuesToSend.size() == 0){
            return ;
        }

        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String,String> param : mKeyValuesToSend.entrySet()) {

                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
         }

        byte[] data = postData.toString().getBytes("UTF-8");
        outputStream.write(data);


    }

}
