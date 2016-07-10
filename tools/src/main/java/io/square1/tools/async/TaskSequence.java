package io.square1.tools.async;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by roberto on 22/12/14.
 */
public abstract class TaskSequence<T> extends Task<T> {



    @Override
    protected T executeTask() throws Exception {

        T result = null;
        Bundle bundle = null;

        Task<T> t = null;
        while ( (t = getNextTask(t)) != null ){
            result = t.executeTask();
        }

        if(t != null) {
            Parcelable p = Task.taskData(t.getBundle());
            setData(p);
        }

        return  result;
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
        return "TaskSequence:" + getId();
    }

    abstract public Task<T> getNextTask(Task<T> lastFinishedTask);
}
