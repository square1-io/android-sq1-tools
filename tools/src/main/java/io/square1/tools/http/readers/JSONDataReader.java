package io.square1.tools.http.readers;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by roberto on 13/11/14.
 */
public class JSONDataReader implements DataReader<JSONObject> {

    @Override
    public JSONObject readData(InputStream in) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();

        String line = null;
        while((line = reader.readLine()) != null){
            builder.append(line);
        }

        return new JSONObject(builder.toString());
    }
}
