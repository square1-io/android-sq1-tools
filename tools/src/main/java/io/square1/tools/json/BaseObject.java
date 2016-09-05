package io.square1.tools.json;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.square1.tools.utils.DateUtils;

/**
 * Created by roberto on 13/07/2016.
 */
public abstract class BaseObject implements Parcelable {

    private int mId;
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private JsonMapper mJsonMapper;

    protected BaseObject(){
        mJsonMapper = new DefaultJsonMapper();
    }

    void setJsonMapper(JsonMapper mapper){
        mJsonMapper = mapper;
    }

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

    public final boolean update(JSONObject object){

        try {

            mId = object.optInt(mJsonMapper.getJsonFieldForId());
            String date = JsonDataUtils.optString(object, mJsonMapper.getJsonFieldForCreatedAt());
            mCreatedAt = DateUtils.parseStringDate(date, mJsonMapper.getDateFormat());

            date = JsonDataUtils.optString(object, mJsonMapper.getJsonFieldForUpdatedAt());
            mUpdatedAt = DateUtils.parseStringDate(date, mJsonMapper.getDateFormat());

            updateWithData(object);

        }catch (Exception exception){
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public final JSONObject toJSON(){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt(mJsonMapper.getJsonFieldForId(), mId);

            final String createdAt = DateUtils.dateToString(mCreatedAt,
                    mJsonMapper.getDateFormat());
            jsonObject.putOpt(mJsonMapper.getJsonFieldForCreatedAt(), createdAt);

            final String updatedAt = DateUtils.dateToString(mUpdatedAt,
                    mJsonMapper.getDateFormat());
            jsonObject.putOpt(mJsonMapper.getJsonFieldForUpdatedAt(), updatedAt);

            serialize(jsonObject);

            return jsonObject;

        }catch (Exception e){
            return null;
        }

    }


    /**
     * subclasses should override this to load any extra
     * value from a json object
     * @param object
     */
    abstract protected void updateWithData(JSONObject out) throws JSONException;

    /**
     * used to serialise an object to Json
     * this is implemented by subclasses to add values to the json object
     * @param object
     */
    abstract protected void serialize(JSONObject object) throws JSONException ;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeLong(this.mCreatedAt != null ? this.mCreatedAt.getTime() : -1);
        dest.writeLong(this.mUpdatedAt != null ? this.mUpdatedAt.getTime() : -1);
        dest.writeParcelable(this.mJsonMapper, flags);
        addValuesToParcel(dest, flags);
    }

    public abstract void addValuesToParcel(Parcel dest, int flags);

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
