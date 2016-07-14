package io.square1.tools.http;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import io.square1.tools.async.ProcessTaskDataHandler;
import io.square1.tools.async.Task;
import io.square1.tools.http.data.DataReader;
import io.square1.tools.http.data.JSONArrayDataReader;
import io.square1.tools.http.data.JSONDataReader;
import io.square1.tools.http.post.MultipartElement;
import io.square1.tools.http.post.MultipartPostBody;
import io.square1.tools.http.put.PutBody;
import io.square1.tools.utils.CleanupStack;
import io.square1.tools.utils.StringUtils;

/**
 * Created by roberto on 12/11/14.
 */
public  class HttpTask<T> extends Task<T> {


    public static class Builder {

        private HashMap<String,String> mHeaders;
        private HashMap<String,String> mInternalParams;
        private Uri.Builder mUriBuilder;
        private ProcessTaskDataHandler mHandler;

        private HttpRequestCacheProvider mCacheProvider;

        //elements for a multipart body
        private ArrayList<MultipartElement> mMultipartElements;
        //elements for a url encoded post
        private HashMap<String,String> mKeyValuesToSend;

        // writer for the body of a PUT method, ignored if method is not PUT
        private PutBody mPutBody;

        private final Method mMethod;


        public Builder(Method method){
            mMethod = method;
            mHeaders = new HashMap<>();
            mInternalParams = new HashMap<>();
            mUriBuilder = new Uri.Builder();
            mKeyValuesToSend = new HashMap();
        }

        public Builder(String url){
            this(Method.GET, url);
        }

        public Builder(Method method, String url){
            this(method);
            Uri uri = Uri.parse(url);
            mUriBuilder = uri.buildUpon();
        }


        public Builder setScheme(String scheme) {
            mUriBuilder.scheme(scheme);
            return this;
        }

        /**
         * add an element for a multipart post. setting one of this
         * will automatically switch the method to POST / multipart
         * @param element
         * @return
         */
        public Builder addParam(MultipartElement element){
            if(mMultipartElements == null){
                mMultipartElements = new ArrayList<>();
            }
            mMultipartElements.add(element);
            return this;
        }

        public Builder setAuthority(String authority) {
            mUriBuilder.authority(authority);
            return this;
        }

        public Builder appendPath(String path) {
            mUriBuilder.appendPath(path);
            return this;
        }


        public Builder addParam(String name, long value) {
            addParam(name, String.valueOf(value));
            return this;
        }

        /**
         * set a provider the body of a PUT request
         * @param putBody
         * @return
         */
        public Builder setPutBody(PutBody putBody) {
            mPutBody = putBody;
            return this;
        }

        /**
         * Encodes the name and value and then appends the parameter to the
         * query string.
         *
         * @param name which will be encoded
         * @param value which will be encoded
         */
        public Builder appendQueryParameter(String name, String value) {

            if(!TextUtils.isEmpty(value) && !TextUtils.isEmpty(name)  ) {
                mUriBuilder.appendQueryParameter(name, value);
                mInternalParams.put(name,value);
            }
            return this;
        }

        /**
         *
         * @param name
         * @param value
         * @return
         */
        public Builder addParam(String name, String value) {
            if(!TextUtils.isEmpty(value) && !TextUtils.isEmpty(name)  ) {
                    mKeyValuesToSend.put(name,value);
                }
            mInternalParams.put(name,value);
            return this;
        }

        public Builder addHeader(String name, String value) {
            if(TextUtils.isEmpty(name) == false &&
                    TextUtils.isEmpty(value) == false) {
                mHeaders.put(name, value);
            }
            return this;
        }

        public Builder setProcessTaskDataHandler(ProcessTaskDataHandler handler) {
            mHandler = handler;
            return this;
        }

        public Builder setCacheProvider( HttpRequestCacheProvider cacheProvider){
            mCacheProvider = cacheProvider;
            return this;
        }

        public HttpTask buildAsJSON(){
            return build(new JSONDataReader());
        }

        public HttpTask buildAsJSONArray(){
            return build(new JSONArrayDataReader());
        }

        public String buildUriString(){
            return mUriBuilder.build().toString();
        }

        public HttpTask build(DataReader reader){

            //if it is a get we just append the keyValues to send as query parameters
             if (mMethod == Method.GET){
                for (Map.Entry<String,String> param : mKeyValuesToSend.entrySet()) {
                    appendQueryParameter(param.getKey(),param.getValue());
                }
                 mKeyValuesToSend.clear();
            }

            HttpTask task =  new HttpTask(mUriBuilder.build(),
                    reader,
                    mCacheProvider);

            task.mMethod = mMethod;

            //this will override any method to POST as we want a multipart post here
            if(mMultipartElements != null &&
                    mMultipartElements.isEmpty() == false){
                task.mMethod = Method.POST;
                task.mBody = new MultipartPostBody(mMultipartElements);

            }else if(task.mMethod == Method.POST){
                // otherwise we just post some data URL encoded
                task.mByteBody = buildBody();
            }
            if(task.mMethod == Method.PUT){
                //this is ignored if the method is not PUT
                task.mPutBody = mPutBody;
            }

            task.mHeaders = mHeaders;

            for(String k : mInternalParams.keySet()){
                task.setParam(k,mInternalParams.get(k));
            }
            task.setDataHandler(mHandler);
            return task;
        }

        private byte[] buildBody(){

            if(mKeyValuesToSend == null || mKeyValuesToSend.size() == 0){
                return null;
            }

            StringBuilder postData = new StringBuilder();
            try {
                for (Map.Entry<String,String> param : mKeyValuesToSend.entrySet()) {

                    if (postData.length() != 0) {
                        postData.append('&');
                    }
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                return postData.toString().getBytes("UTF-8");
            }catch (Exception exc){

            }

            return null;

        }

    }


    public  enum Method {
        POST,
        GET,
        PUT
    }

    private int mContentLenght;
    private Uri mURL;
    private DataReader<T> mDataReader;
    private HttpRequestCacheProvider mCacheProvider;
    private HashMap<String,String> mHeaders;

    private MultipartPostBody mBody;
    private byte[] mByteBody;
    private PutBody mPutBody;
    private Method mMethod;


    HttpTask(Uri uri, DataReader reader, HttpRequestCacheProvider cache) {
        super();
        mURL = uri;
        mDataReader = reader;
        mCacheProvider = cache;

    }

    @Override
    protected T executeTask() throws Exception {

        T result = null;
        boolean dataFromCache = false;
        CleanupStack cleanupStack = new CleanupStack();
        HttpURLConnection urlConnection  = null;
        Exception exc = null;
        Bundle data = new Bundle();
        try {

            final String urlString = mURL.toString();

            URL url = new URL(urlString);
            if(mCacheProvider != null){
                Log.d("API", "trying from cache...");
                InputStream cached = mCacheProvider.getInputStream(urlString);
                if(cached != null){
                    cleanupStack.add(cached);
                    result = parseIncomingData( new BufferedInputStream(cached));
                    dataFromCache = (result != null);
                }
            }

            if(dataFromCache == false) {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(mMethod.name());

                final Set<String> headers = mHeaders.keySet();
                //set request headers
                for(String header : headers) {
                    String value = mHeaders.get(header);
                    urlConnection.setRequestProperty(header, value);
                }
                if(mMethod == Method.PUT){
                    if(mPutBody != null) {
                        urlConnection.setDoOutput(true);
                        OutputStream outputStream = urlConnection.getOutputStream();
                        mPutBody.output(outputStream);
                    }
                }
                else if(mBody != null){
                    //set content type
                    urlConnection.setRequestProperty("Content-Type", mBody.getBodyContentType());
                    urlConnection.setDoOutput(true);
                    OutputStream outputStream = urlConnection.getOutputStream();
                    mBody.output(outputStream);
                }
                else if (mByteBody != null){
                    urlConnection.setDoOutput(true);
                    urlConnection.getOutputStream().write(mByteBody);
                }

                mContentLenght = urlConnection.getContentLength();
                InputStream in = null;
                if(mCacheProvider != null){
                    ///save stream to cache and return a new readable stream
                    //from the cache this time
                    in = mCacheProvider.saveInputStream(urlString,cleanupStack.add(new BufferedInputStream(urlConnection.getInputStream())));
                }else{
                    in = urlConnection.getInputStream();
                }
                result = parseIncomingData(cleanupStack.add(new BufferedInputStream(in)));
            }

        }catch ( Exception raised) {
            exc = raised;
            throw  exc;
        }
        finally {
            cleanupStack.close();
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }

        return result;
    }

    @Override
    public String toString() {
       return StringUtils.maxLengthString(mURL.getPath()+"?"+mURL.getQuery(), 40);
    }

    public String getURL(){
        return mURL.toString();
    }

    protected  T parseIncomingData(BufferedInputStream in)  throws Exception {
        return mDataReader.readData(in);
    }


    @Override
    public void onStart(Bundle param) {

    }

    @Override
    public void onCanceled(Bundle param) {

    }

    @Override
    public void onFinish(T result) {

    }

    @Override
    public String getCacheId() {
        return mURL.toString();
    }


}
