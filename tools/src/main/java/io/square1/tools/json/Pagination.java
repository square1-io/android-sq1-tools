package io.square1.tools.json;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;



public class Pagination implements Parcelable {

    public static final int FIRST_PAGE = 0;
    public static final Pagination NO_PAGE = new Pagination();

    private int mCurrentPage;
    private int mTotalPages;
    private int mTotalResults;

    private JsonMapper mJsonParser;

    public Pagination(){
       this(new DefaultJsonMapper());
    }

    public Pagination(JsonMapper jsonMapper){
        mJsonParser = jsonMapper;
        mCurrentPage = FIRST_PAGE - 1;
        mTotalPages = Integer.MAX_VALUE;
        mTotalResults = Integer.MAX_VALUE;
    }

    public Pagination(JSONObject jsonObject, JsonMapper jsonMapper){
        this(jsonMapper);
        mCurrentPage = jsonObject.optInt(mJsonParser.getJsonFieldForPaginationCurrentPage());
        mTotalPages = jsonObject.optInt(mJsonParser.getJsonFieldForPaginationPages());
        mTotalResults = jsonObject.optInt(mJsonParser.getJsonFieldForPaginationTotalResults());
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public boolean hasNext(){
        return  mCurrentPage < ( mTotalPages - 1);
    }

    public int nextPage(){
        return  mCurrentPage + 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCurrentPage);
        dest.writeInt(mTotalPages);
        dest.writeInt(mTotalResults);
        dest.writeParcelable(this.mJsonParser, flags);
    }

    protected Pagination(Parcel in) {
        mCurrentPage = in.readInt();
        mTotalPages = in.readInt();
        mTotalResults = in.readInt();
        mJsonParser = in.readParcelable(JsonMapper.class.getClassLoader());
    }

    public static final Parcelable.Creator<Pagination> CREATOR = new Parcelable.Creator<Pagination>() {
        @Override
        public Pagination createFromParcel(Parcel source) {
            return new Pagination(source);
        }

        @Override
        public Pagination[] newArray(int size) {
            return new Pagination[size];
        }
    };
}
