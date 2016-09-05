package io.square1.tools.json;

import android.content.Context;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import io.square1.tools.async.ProcessTaskDataHandler;

/**
 * Created by roberto on 16/07/2016.
 *
 * Maps a json object to a subclass of a BaseObject
 *
 */
public class BaseObjectReader<T extends BaseObject> implements ProcessTaskDataHandler<JSONObject> {

    private Class<T> mClass;
    private JsonMapper mMapper;

    public  BaseObjectReader(Class<T> tClass){
        this(tClass, new DefaultJsonMapper());
    }

    public  BaseObjectReader(Class<T> tClass, JsonMapper mapper){

        mClass = tClass;
        mMapper = mapper;
    }


    @Override
    public  T processReceivedData(Context context,
                                  JSONObject object,
                                  AtomicBoolean atomicBoolean) throws Exception {

        T newInstance = mClass.newInstance();
        newInstance.setJsonMapper(mMapper);
        newInstance.update(object);
        return newInstance;
    }
}
