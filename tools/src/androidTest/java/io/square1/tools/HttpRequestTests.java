package io.square1.tools;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.square1.tools.http.HttpTask;

/**
 * Created by roberto on 05/09/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class HttpRequestTests {


    @Test
    public void testGet() throws Exception {

        HttpTask<JSONObject> task = new HttpTask.Builder(HttpTask.Method.GET)
                .setScheme("https")
                .setAuthority("httpbin.org")
                .appendPath("get")
                .buildAsJSON();

        task.runSync(InstrumentationRegistry.getContext());

        JSONObject result = task.getRawData();

        Assert.assertNotNull(result);
        assert (task.getResponseCode() == 200);
    }

    @Test
    public void testDelete() throws Exception {

        HttpTask<JSONObject> task = new HttpTask.Builder(HttpTask.Method.DELETE)
                .setScheme("https")
                .setAuthority("httpbin.org")
                .appendPath("delete")
                .buildAsJSON();

        task.runSync(InstrumentationRegistry.getContext());
        JSONObject result = task.getRawData();

        Assert.assertNotNull(result);
        assert (task.getResponseCode() == 200);

    }


}
