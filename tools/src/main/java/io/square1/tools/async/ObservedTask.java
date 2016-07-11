package io.square1.tools.async;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * An observer can observer many tasks, this will keep track of the ids
 * of the tasks observer by one particular observer
 *
 * Created by roberto on 07/07/2016.
 */
public class ObservedTask {

    private TreeSet<Integer> mTasks;

    public ObservedTask(){
        mTasks = new TreeSet<>();
    }

    public void addTask(Task task){
        mTasks.add(task.getId());
    }

    public void removeTask(Task task){
        mTasks.remove(task.getId());
    }

    public boolean isEmpty(){
        return mTasks.isEmpty();
    }

    public Set<Integer> taskIds(){
        return mTasks;
    }

}
