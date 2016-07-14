package io.square1.tools.laravel;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;

import io.square1.tools.json.JsonDataUtils;
import io.square1.tools.utils.DateUtils;

/**
 * Created by roberto on 13/07/2016.
 */
public abstract class BaseObject implements Parcelable {

    private int mId;
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private JsonMapper mJsonMapper;

    public BaseObject(JSONObject object){
        this(object, new DefaultJsonMapper());
    }

    public BaseObject(JSONObject object, JsonMapper parser){
        mJsonMapper = parser;
        update(object);
    }

    public int getId(){
        return mId;
    }

    public final void update(JSONObject object){

        mId = object.optInt(mJsonMapper.getJsonFieldForId());
        String date = JsonDataUtils.optString(object, mJsonMapper.getJsonFieldForCreatedAt());
        mCreatedAt = DateUtils.parseStringDate(date, mJsonMapper.getDateFormat());

        date = JsonDataUtils.optString(object, mJsonMapper.getJsonFieldForUpdatedAt());
        mUpdatedAt = DateUtils.parseStringDate(date, mJsonMapper.getDateFormat());

        updateWithData(object);

    }

    /**
     * subclasses should override this to load any extra
     * value from a json object
     * @param object
     */
    abstract protected void updateWithData(JSONObject object);


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeLong(this.mCreatedAt != null ? this.mCreatedAt.getTime() : -1);
        dest.writeLong(this.mUpdatedAt != null ? this.mUpdatedAt.getTime() : -1);
        dest.writeParcelable(this.mJsonMapper, flags);
    }

    protected BaseObject(Parcel in) {
        this.mId = in.readInt();
        long tmpMCreatedAt = in.readLong();
        this.mCreatedAt = tmpMCreatedAt == -1 ? null : new Date(tmpMCreatedAt);
        long tmpMUpdatedAt = in.readLong();
        this.mUpdatedAt = tmpMUpdatedAt == -1 ? null : new Date(tmpMUpdatedAt);
        this.mJsonMapper = in.readParcelable(JsonMapper.class.getClassLoader());
    }

    public JsonMapper getMapper(){
        return mJsonMapper;
    }


}
