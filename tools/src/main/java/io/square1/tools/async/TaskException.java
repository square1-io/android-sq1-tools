package io.square1.tools.async;

/**
 * Created by roberto on 06/07/2016.
 */
public class TaskException extends NullPointerException {

    private Task mTask;

    public TaskException(Task task){
        super("task has returned an invalid value");
        mTask = task;
    }

}
