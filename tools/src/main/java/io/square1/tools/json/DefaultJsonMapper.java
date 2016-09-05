package io.square1.tools.json;

import android.os.Parcel;

import org.json.JSONObject;

import io.square1.tools.utils.DateUtils;

/**
 * Created by roberto on 14/07/2016.
 */
public class DefaultJsonMapper implements JsonMapper {

    @Override
    public String getJsonFieldForId() {
        return "id";
    }

    @Override
    public String getJsonFieldForCreatedAt() {
        return "created_at";
    }

    @Override
    public String getJsonFieldForUpdatedAt() {
        return "updated_at";
    }

    ///some pagination fields
    @Override
    public String getJsonFieldForPaginationTotalResults() {
        return "total";
    }

    @Override
    public String getJsonFieldForPaginationCurrentPage() {
        return "page";
    }

    @Override
    public String getJsonFieldForPaginationPages() {
        return "pages";
    }

    @Override
    public boolean hasValidPagination(JSONObject object){

        if(object == null) return false;

        if(object.has(getJsonFieldForPaginationCurrentPage()) == true) return true;

        return false;
    }

    @Override
    public String getDateFormat() {
        return DateUtils.FORMAT_1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public DefaultJsonMapper() {
    }

    protected DefaultJsonMapper(Parcel in) {
    }

    public static final Creator<DefaultJsonMapper> CREATOR = new Creator<DefaultJsonMapper>() {
        @Override
        public DefaultJsonMapper createFromParcel(Parcel source) {
            return new DefaultJsonMapper(source);
        }

        @Override
        public DefaultJsonMapper[] newArray(int size) {
            return new DefaultJsonMapper[size];
        }
    };
}
