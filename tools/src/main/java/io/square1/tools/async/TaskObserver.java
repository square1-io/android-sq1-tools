package io.square1.tools.async;

import android.os.Bundle;

public interface TaskObserver{

        boolean observerHasLeft();
        void onTaskStart(Task task);
        void onTaskCancel(Task task);
        void onTaskWaiting(Task task);
        void onTaskFinish(Task task);
        void onTaskFailed(Task task);
        void onTaskProgress(Task task, Bundle progress);

    }