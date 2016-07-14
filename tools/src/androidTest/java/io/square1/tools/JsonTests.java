package io.square1.tools;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;

import io.square1.tools.laravel.DefaultJsonMapper;
import io.square1.tools.laravel.Pagination;

/**
 * Created by roberto on 14/07/2016.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class JsonTests {


    int page = 3;
    int pages = 200;
    int total = 234;

    int mJsonId = 123;

    private DefaultJsonMapper mDefaultJsonParser;
    private JSONObject mPaginationJson;
    private JSONObject mMockJson;

    @Before
    public void createLogHistory() {
        mDefaultJsonParser = new DefaultJsonMapper();
        mPaginationJson = new JSONObject();
        mMockJson = new JSONObject();
        try {
            mPaginationJson.put(mDefaultJsonParser.getJsonFieldForPaginationCurrentPage(), page);
            mPaginationJson.put(mDefaultJsonParser.getJsonFieldForPaginationPages(), pages);
            mPaginationJson.put(mDefaultJsonParser.getJsonFieldForPaginationTotalResults(), total);

            mMockJson.put(mDefaultJsonParser.getJsonFieldForId(),mJsonId);
            mMockJson.put(mDefaultJsonParser.getJsonFieldForCreatedAt(),"1999-11-22 18:01:22");
            mMockJson.put(mDefaultJsonParser.getJsonFieldForUpdatedAt(),"2000-01-23 18:01:22");
            mMockJson.put("mDate","2000-01-23 18:01:22");

        }catch (Exception e){}
    }

    @Test
    public void paginationFromJson_test() throws Exception {

        Pagination pagination = new Pagination(mPaginationJson, mDefaultJsonParser);
        Assert.assertEquals(pagination.getCurrentPage(), page);
        Assert.assertEquals(pagination.getTotalPages(), pages);
        Assert.assertEquals(pagination.getTotalResults(), total);
    }

    @Test
    public void paginationFromParcel_test() throws Exception {

        Pagination pagination = new Pagination(mPaginationJson, mDefaultJsonParser);

        // Write the data.
        Parcel parcel = Parcel.obtain();
        pagination.writeToParcel(parcel, pagination.describeContents());
        parcel.setDataPosition(0);

        Pagination pagination2 = Pagination.CREATOR.createFromParcel(parcel);

        Assert.assertEquals(pagination2.getCurrentPage(), page);
        Assert.assertEquals(pagination2.getTotalPages(), pages);
        Assert.assertEquals(pagination2.getTotalResults(), total);
    }

    @Test
    public void mockFromParcel_test() throws Exception {

        MockObject mockObject = new MockObject(mMockJson);

        // Write the data.
        Parcel parcel = Parcel.obtain();
        mockObject.writeToParcel(parcel, mockObject.describeContents());
        parcel.setDataPosition(0);

        MockObject mockObject2 = MockObject.CREATOR.createFromParcel(parcel);

        Assert.assertEquals(mockObject,mockObject2);
        Assert.assertEquals(mockObject2.getId(), mJsonId);

    }
}
