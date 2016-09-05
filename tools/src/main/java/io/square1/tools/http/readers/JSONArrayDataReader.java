package io.square1.tools.http.readers;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by roberto on 13/11/14.
 */
public class JSONArrayDataReader implements DataReader<JSONArray> {

    @Override
    public JSONArray readData(InputStream in) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();

        String line = null;
        while((line = reader.readLine()) != null){
            builder.append(line);
        }

        return new JSONArray(builder.toString());
    }
}
