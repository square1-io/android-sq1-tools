package io.square1.tools.async;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by roberto on 12/11/14.
 */
public class TaskQueue {

    private static final String TAG = TaskQueue.class.getName();


    private Handler mHandler;
    private int mStartCount;
    private ExecutorService mService;

    private SparseArray<Task> mTasks = new SparseArray<Task>();
    private HashMap<TaskObserver,ObservedTask> mObservedTask;

    private Context mApplicationContext;

    //map requests Id to observer
    private SparseArray<TaskObserver> mTasksObserver = new SparseArray<TaskObserver>();

    public TaskQueue(Context ctx){
        mApplicationContext = ctx.getApplicationContext();
        mHandler = new Handler(Looper.getMainLooper());
        mService = Executors.newCachedThreadPool();
        mObservedTask = new HashMap<>();
        mStartCount = 100;
    }

     public <T> T runSyncTask(Task<T> task, Bundle args ){

         if(task == null){
             return null;
         }
         mStartCount ++;
         final int id = mStartCount;
         task.assignTaskToQueue( mApplicationContext, this, mStartCount, args);
         return task.runImplementation();
    }

    public int addTask(Task task, TaskObserver observer){
       return addTask(task,  observer, null, false);
    }
    public int addTask(Task task){
        return addTask(task, null, null, false);
    }

    public int addTask(Task task, Bundle args){
        return addTask(task, null,args, false);
    }


    /**
     *
     * @param task
     * @param observer
     * @param args
     * @param blockingTask set to true so all other tasks added to the queue after this will
     *                     wait for this task to complete before execuring their code.
     * @return the unique id of this task in the queue
     */
    public int addTask(final Task task,  TaskObserver observer, Bundle args, boolean blockingTask){

        if(task == null){
            return -1;
        }

        if(blockingTask == true){

            if(mBlockingTask == null){
                setBlockingTask(task);
            }else {
                Log.e(TAG, "error blocking task already running");
                return  -1;
            }
        }

        mStartCount ++;
        //this will setup the task id too , so call before using the taskId
        task.assignTaskToQueue( mApplicationContext, this, mStartCount, args);

        final int taskId = task.getId();

        mTasks.append(taskId, task);

        //resolve the observer
        ObservedTask observedTask = getObservedTask(observer);

        if(observedTask != null){
            observedTask.addTask(task);
        }

        if(observer != null){
            mTasksObserver.append(taskId, observer);
        }


        //submit
        mHandler.post( new Runnable() {
            @Override
            public void run() {
                mService.submit(task);
            }
        });

        return taskId;
    }

    public void cancel(int taskId){
        Task t = mTasks.get(taskId);
        if(t != null){
            t.cancel();
        }
    }


    private ObservedTask getObservedTask(TaskObserver observer){

        if(observer == null) {
            return null;
        }
        ObservedTask observedTask = mObservedTask.get(observer);

        if (observedTask == null){
            observedTask = new ObservedTask();
            mObservedTask.put(observer, observedTask);
        }

        return observedTask;

    }


    public boolean clearObserver(TaskObserver observer) {

        if(observer == null) {
            return false;
        }

        ObservedTask observedTask = mObservedTask.remove(observer);

        if(observedTask != null &&
                observedTask.isEmpty() == false) {

            Set<Integer> taskIds = observedTask.taskIds();
            for(Integer id : taskIds){
                cancel(id);
            }
        }

        return false;

    }

    private TaskObserver getObserverForTask(final Task task, boolean remove){

        int id = task.getId();

        final TaskObserver observer = mTasksObserver.get(id);

        if(observer != null && remove){
            mTasksObserver.remove(id);
        }

        return observer;
    }

//    protected void notifyOnStart(final Task task){
//        int id = task.getId();
//        final TaskObserver observer = mTasksObserver.get(id);
//        if(observer != null){
//            mHandler.post( new Runnable() {
//                @Override
//                public void run() {
//                    observer.onTaskStart(task.getBundle());
//                }
//            });
//        }
//    }

//    protected void notifyOnError(final Task task){
//        final int id = task.getId();
//        final TaskObserver observer = mTasksObserver.get(id);
//        mTasksObserver.remove(id);
//        signalBLockingTask(id);
//        if(observer != null){
//            mHandler.post( new Runnable() {
//                @Override
//                public void run() {
//                    observer.onTaskFailed(task.getBundle());
//                    mTasks.remove(id);
//                }
//            });
//        }
//    }
//    protected void notifyOnFinish(final Task task){
//        final int id = task.getId();
//        final TaskObserver observer = mTasksObserver.get(id);
//        mTasksObserver.remove(id);
//        signalBLockingTask(id);
//        if(observer != null){
//            mHandler.post( new Runnable() {
//                @Override
//                public void run() {
//                    observer.onTaskFinish(task.getBundle());
//                    mTasks.remove(id);
//                }
//            });
//        }
//    }

//    protected void notifyOnCancel(final Task task){
//        int id = task.getId();
//        final TaskObserver observer = mTasksObserver.get(id);
//        mTasksObserver.remove(id);
//        mTasks.remove(id);
//        signalBLockingTask(id);
//        if(observer != null){
//            mHandler.post( new Runnable() {
//                @Override
//                public void run() {
//                    observer.onTaskCancel(task.getBundle());
//                }
//            });
//        }
//    }

    /**
     * this will call the taskUpdatedImpl on the main
     * looper thread as we will be pushing updated to the ui from here
     * @param task
     */
    protected void taskStateUpdated(final Task task){

        mHandler.post( new Runnable() {
            @Override
            public void run() {
                taskStateUpdatedImpl(task);
            }
        });
    }
    private void taskStateUpdatedImpl(final Task task){

        final int state = task.getState();

        final boolean finishing = Task.STATE_CANCELED == task.getState() ||
                Task.STATE_FINISHED == task.getState() ;

        //pull out the observer
        final TaskObserver observer = getObserverForTask(task, finishing);

        if(observer != null &&
                observer.observerHasLeft()){
            mTasksObserver.remove(task.getId());
        }

        switch (state) {
            case Task.STATE_CANCELED:{
                if(observer != null){
                    observer.onTaskCancel(task);
                }
                break;
            }
            case Task.STATE_WAITING: {
                if(observer != null){
                    observer.onTaskWaiting(task);
                }
                break;
            }
            case Task.STATE_STARTED: {
                if(observer != null){
                    observer.onTaskStart(task);
                }
                break;
            }
            case Task.STATE_FINISHED:{
                if(observer != null){
                    if(task.getResultCode() == Task.RESULT_OK) {
                        observer.onTaskFinish(task);
                    }
                    else if(task.getResultCode() == Task.RESULT_FAILED) {
                        observer.onTaskFailed(task);
                    }
                }
                break;
            }
        }

        //if this was a blocking task
        // release all other pending tasks.
        if(finishing == true){
            //remove task from list of running tasks
            mTasks.remove(task.getId());

            if(observer != null) {
                //get the collection of tasks for this observer
                ObservedTask observedTask = getObservedTask(observer);
                if(observedTask != null) {
                    observedTask.removeTask(task);
                    //this observer is not waiting for any other task
                    // we can remove it from the mapping
                    if(observedTask.isEmpty() == true){
                        mObservedTask.remove(observer);
                    }
                }

            }
            signalBLockingTask(task.getId());
        }
    }

    public Task getTask(int taskId) {
        return mTasks.get(taskId);
    }

    /**
     * there can only be one task blocking the queue at any given time
     * if use tries to add a new one where there is another running an exception will be thrown
     */
    private Task mBlockingTask;

    private void setBlockingTask(Task task){
        synchronized (this){
            mBlockingTask = task;
        }
    }

    private void signalBLockingTask(int id){

        synchronized (this) {
            if (mBlockingTask != null && mBlockingTask.getId() == id) {
                setBlockingTask(null);
                this.notify();
            }
        }
    }

    public void shouldWaitOnBlockingTask(Task task) {
        synchronized (this){
            if(mBlockingTask != null && task != mBlockingTask){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void taskStateProgress(final Task task, final Bundle progress) {

        mHandler.post( new Runnable() {
            @Override
            public void run() {
                //pull out the observer
                final TaskObserver observer = getObserverForTask(task, false);

                if(observer != null &&
                        observer.observerHasLeft()){
                    mTasksObserver.remove(task.getId());
                }else {
                    observer.onTaskProgress(task, progress);
                }
            }
        });

    }
}
