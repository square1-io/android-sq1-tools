package io.square1.testapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.square1.tools.async.Task;
import io.square1.tools.async.TaskObserver;
import io.square1.tools.async.TaskQueue;

public class MainActivity extends AppCompatActivity implements TaskObserver {

    private class BlockingTask extends Task<String> {


        @Override
        protected String executeTask() throws Exception {

            Integer integer = -10;

            while(isCanceled() == false && integer <= 0){

                integer ++;
                Thread.sleep(800);
                Bundle bundle = new Bundle();
                bundle.putInt("progress",integer);
                postProgress(bundle);
            }

            return "Now you can start counting";
        }

        @Override
        public void onStart(Bundle param) {

        }

        @Override
        public void onCanceled(Bundle param) {

        }

        @Override
        public void onFinish(String result) {

        }
    }

    private class TaskCount extends Task<Void> {

        @Override
        protected Void executeTask() throws Exception {

            Integer integer = 0;

            while(isCanceled() == false){

                integer ++;
                if(integer == 1){
                    Thread.sleep(5000);
                }else {
                    Thread.sleep(800);
                }

                Bundle bundle = new Bundle();
                bundle.putInt("progress",integer);
                postProgress(bundle);
            }

            return null;
        }

        @Override
        public void onStart(Bundle param) {

        }

        @Override
        public void onCanceled(Bundle param) {

        }

        @Override
        public void onFinish(Void result) {

        }
    }

    private TaskQueue mQueue;
    private TextView mProgressTextView;
    private TextView mBlockingTextView;

    private Button mCancelButton;

    private int mBlockingTaskId;
    private int mCountingTaskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressTextView = (TextView)findViewById(R.id.progress);
        mBlockingTextView = (TextView)findViewById(R.id.blocking_progress);

        mCancelButton = (Button)findViewById(R.id.button);

        mQueue = new TaskQueue(this);

        //this will block any other till it finishes
        mBlockingTaskId = mQueue.addTask( new BlockingTask(), this, null, true);

       mCountingTaskId = mQueue.addTask( new TaskCount(), this);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue.cancel(mCountingTaskId);
            }
        });


    }


    @Override
    public boolean observerHasLeft() {
        return false;
    }

    @Override
    public void onTaskStart(Task task) {
        if(mCountingTaskId == task.getId()) {
            mProgressTextView.setText(task.getId() + " Started");
        }
    }

    @Override
    public void onTaskCancel(Task task) {
        if(mCountingTaskId == task.getId()) {
            mProgressTextView.setText(task.getId() + " Canceled");
        }
    }

    @Override
    public void onTaskWaiting(Task task) {
        if(mCountingTaskId == task.getId()) {
            mProgressTextView.setText(task.getId() + " Waiting");
        }
    }

    @Override
    public void onTaskFinish(Task task) {
        if(mCountingTaskId == task.getId()) {
            mProgressTextView.setText(task.getId() + " Finish");
        }else {
            BlockingTask currentTask = (BlockingTask)task;
            mBlockingTextView.setText(currentTask.getResult());
        }
    }

    @Override
    public void onTaskFailed(Task task) {

    }

    @Override
    public void onTaskProgress(Task task, Bundle progress) {

        Integer value = progress.getInt("progress");

        if(mCountingTaskId == task.getId()) {
            mProgressTextView.setText(String.valueOf(value));
        }else {
            mBlockingTextView.setText(String.valueOf(value));
        }


    }
}
