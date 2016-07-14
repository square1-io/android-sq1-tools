package io.square1.tools.http.put;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by roberto on 14/07/2016.
 */
public class JsonPutBody implements PutBody {

    private  JSONObject mObject;

    public JsonPutBody(JSONObject object){
        mObject = object;
    }

    @Override
    public void output(OutputStream outputStream) throws IOException {

        if(mObject != null){
            byte[] data = mObject.toString().getBytes("UTF-8");
            outputStream.write(data);
        }

    }
}
