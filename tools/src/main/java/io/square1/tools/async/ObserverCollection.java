package io.square1.tools.async;

import java.util.ArrayList;

/**
 * Created by roberto on 07/07/2016.
 */
public class ObserverCollection {

    private ArrayList<TaskObserver> mObservers;

    public ObserverCollection(){
        mObservers = new ArrayList<>();
    }

}
