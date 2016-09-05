package io.square1.tools;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.square1.tools.json.JsonDataUtils;
import io.square1.tools.json.BaseObject;
import io.square1.tools.json.DefaultJsonMapper;
import io.square1.tools.utils.DateUtils;

/**
 * Created by roberto on 14/07/2016.
 */
public class MockObject extends BaseObject {

    private String mField1;
    private String mField2;
    private Date mDate;

    public MockObject(JSONObject object){
        super(object, new DefaultJsonMapper());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MockObject that = (MockObject) o;

        if (!mField1.equals(that.mField1)) return false;
        if (!mField2.equals(that.mField2)) return false;
        return mDate.equals(that.mDate);

    }

    @Override
    public int hashCode() {
        int result = mField1.hashCode();
        result = 31 * result + mField2.hashCode();
        result = 31 * result + mDate.hashCode();
        return result;
    }


    @Override
    protected void updateWithData(JSONObject object){
        mField1 = object.optString("mField1");
        mField2 = object.optString("mField2");

        String date = JsonDataUtils.optString(object,"mDate");
        mDate = DateUtils.parseStringDate(date,getMapper().getDateFormat());
    }

    @Override
    protected void serialize(JSONObject object) throws JSONException {

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void addValuesToParcel(Parcel dest, int flags) {
        dest.writeString(this.mField1);
        dest.writeString(this.mField2);
        dest.writeLong(this.mDate != null ? this.mDate.getTime() : -1);
    }

    protected MockObject(Parcel in) {
        super(in);
        this.mField1 = in.readString();
        this.mField2 = in.readString();
        long tmpMDate = in.readLong();
        this.mDate = tmpMDate == -1 ? null : new Date(tmpMDate);
    }

    public static final Creator<MockObject> CREATOR = new Creator<MockObject>() {
        @Override
        public MockObject createFromParcel(Parcel source) {
            return new MockObject(source);
        }

        @Override
        public MockObject[] newArray(int size) {
            return new MockObject[size];
        }
    };
}
