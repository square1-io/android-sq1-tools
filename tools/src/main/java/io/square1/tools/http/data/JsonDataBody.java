package io.square1.tools.http.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by roberto on 14/07/2016.
 */
public class JsonDataBody implements DataBody {

    private  Object mObject;

    public JsonDataBody(JSONObject object){
        mObject = object;
    }

    public JsonDataBody(JSONArray object){
        mObject = object;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public void output(OutputStream outputStream) throws IOException {

        if(mObject != null){
            byte[] data = mObject.toString().getBytes("UTF-8");
            outputStream.write(data);
        }

    }
}
