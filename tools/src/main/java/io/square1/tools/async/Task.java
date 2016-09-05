package io.square1.tools.async;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by roberto on 12/11/14.
 */
public abstract class Task<T> implements Runnable {


    private static final boolean DEBUG = true;

    public static final int RESULT_FAILED = -100;
    public static final int RESULT_OK = 100;

    private static final String ARG_CACHE_ID = "TASK_CACHE_ID";
    private static final String ARG_ID = "TASK_ARG_ID";
    private static final String ARG_RESULT_CODE = "TASK_ARG_RESULT_CODE";




    public static final int STATE_WAITING = 0;
    public static final int STATE_STARTED = 1;
    public static final int STATE_CANCELED = 2;
    public static final int STATE_FINISHED = 3;

    private TaskQueue mCurrentQueue;

    private int mTaskId;
    private long mStartTimeStamp;
    private long mEndTimeStamp;

    private Bundle mParams;
    private int mTag;
    private T mRawData;
    private Parcelable mResult;

    private ProcessTaskDataHandler mDataHandler;
    private Thread mCurrentThread;

    private Context mApplicationContext;
    private Exception mLastException;

    private AtomicInteger mState = new AtomicInteger(STATE_WAITING);
    private AtomicBoolean mCanceled = new AtomicBoolean(false);

   public Task(){
       mTag = -1;
       mParams = new Bundle();
    }

    protected void addBundle(Bundle in){
        if(in != null) {
            mParams.putAll(in);
        }
    }

    protected void setResultCode(int result){
        mParams.putInt(ARG_RESULT_CODE, result);
    }

    protected int getResultCode(){
       return mParams.getInt(ARG_RESULT_CODE, RESULT_OK);
    }


    void assignTaskToQueue(Context ctx, TaskQueue queue, int taskId, int tag,  Bundle params){

        mApplicationContext = ctx;
        mCurrentQueue = queue;
        mTaskId = taskId;
        mTag = tag;

        addBundle(params);

        mParams.putString(ARG_CACHE_ID, getCacheId());
        mParams.putInt(ARG_ID, mTaskId);

    }

    public final int getState(){
        return mState.get();
    }


    private void handleStateCanceled(){
        mCanceled.set(true);
        onCanceled(mParams);
        setResultCode(RESULT_OK);
        mCurrentQueue.taskStateUpdated(this);

        synchronized (this) {
            if(mCurrentThread != null ){
                mCurrentThread.interrupt();
                mCurrentThread = null;
            }
        }
    }

    private void handleStateRunning(){
        mStartTimeStamp = System.currentTimeMillis();
        onStart(mParams);
        mCurrentQueue.taskStateUpdated(this);
    }

    private void handleStateFinished(){

        if(mRawData != null &&
                mDataHandler != null){

            // some data here , see if we can do some processing on it
            try{

                final long startProcessing  = System.currentTimeMillis();
                mResult = mDataHandler.processReceivedData(mApplicationContext, mRawData, mCanceled);
                long endProcessing = System.currentTimeMillis();

                if(DEBUG){
                    Log.d("API", String.format(" %s - processing Time %f",
                            this.toString(),
                            (endProcessing - startProcessing)/1000f));
                }

            }catch (Exception e){
                mLastException = e;
            }
        }

        //call on finish , subclasses of task will handle it
        onFinish(mRawData);

        //when we get here we have either
        // got a result for the task and processed it correctly
        // or we failed getting a mRawData or something went wrong in processing the data
        if(mLastException != null) {
            setResultCode(RESULT_FAILED);
            mCurrentQueue.taskStateUpdated(this);
        }else {
            setResultCode(RESULT_OK);
            mCurrentQueue.taskStateUpdated(this);
        }

        mEndTimeStamp = System.currentTimeMillis();

        if(DEBUG){
            Log.d("API", String.format(" %s - executionTime %d",
                    this.toString(),
                    (mEndTimeStamp - mStartTimeStamp)));
        }

    }

    private void updateState(int state){

        final int currentState = mState.get();

        //canceled already so do nothing.
        if(currentState == Task.STATE_CANCELED) {
            return;
        }

        boolean apply = state != currentState;

        if(apply == true){

            mState.set(state);

            switch (state){

                case STATE_CANCELED:{
                    handleStateCanceled();
                    break;
                }
                case STATE_STARTED:{
                    handleStateRunning();
                    break;
                }
                case STATE_FINISHED:{
                    handleStateFinished();
                    break;
                }
            }

        }

    }

    public final boolean isCanceled(){
        return mState.get() == STATE_CANCELED;
    }

    public void cancel(){
        synchronized (this){
            updateState(STATE_CANCELED);
        }
    }

    @Override
    public void run() {

        synchronized (this) {
            mCurrentThread = Thread.currentThread();
        }
        mCurrentQueue.shouldWaitOnBlockingTask(this);
        runImplementation();
    }

     T runImplementation(){

        updateState(STATE_STARTED);

        try {
            mRawData = executeTask();
        }catch (Exception exc){
            mLastException = exc;
        }
        //we always expect a non null result
        if(mRawData == null && mLastException == null){
            mLastException = new TaskException(this);
        }
        updateState(STATE_FINISHED);
        return mRawData;
    }

    public final T runSync(Context ctx){
        //dummy queue
        mCurrentQueue = new TaskQueue(ctx);
        return runImplementation();
    }

    @Override
    public String toString() {

        String tmp = "taskId: " + mTaskId;
        return TextUtils.substring(tmp,0,tmp.length() > 40
                ? 40 :
                tmp.length()  ) ;
    }

    public T getRawData(){
        return mRawData;
    }

    public Parcelable getResult(){
        return mResult;
    }

    public int getTag(){
        return mTag;
    }

    public Bundle getBundle(){
        return mParams;
    }

    protected void setParam(String key, String value){
        mParams.putString(key,value);
    }

    protected void setParam(String key, int value){
        mParams.putInt(key, value);
    }

    protected int getIntParam(String key){
        return mParams.getInt(key,0);
    }

    protected String getStringParam(String key){
        return mParams.getString(key,"");
    }

    protected void setDataHandler(ProcessTaskDataHandler process){
        mDataHandler = process;
    }

    protected abstract T executeTask() throws Exception;

    /**
     * prepare the task for execution
     * @param param
     */
    public abstract void onStart(Bundle param);
    public abstract void onCanceled(Bundle param);
    public abstract void onFinish(T result);

    public void postProgress(Bundle progress){
        if(progress != null && isCanceled() == false){
            mCurrentQueue.taskStateProgress(this, progress);
        }
    }

    public int getId() {
        return mTaskId;
    }

    protected Context getApplicationContext(){
        return mApplicationContext;
    }

    public String getCacheId() {
        return "Task:" + getId();
    }
}
