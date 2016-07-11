package io.square1.tools.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by roberto on 20/11/14.
 */
public class WeakArrayList<E> implements Iterable<WeakReference<E>> {

    private ArrayList<WeakReference<E>> mList;

    public WeakArrayList(){
        mList = new ArrayList<WeakReference<E>>();
    }

    public void add(E item){
        mList.add(new WeakReference<E>(item));
    }

    public void remove(E item){
        for(WeakReference<E> current : mList){
            if(current.get() == item){
                mList.remove(current);
                break;
            }
        }
    }

    @Override
    public Iterator<WeakReference<E>> iterator() {
        return mList.iterator();
    }
}
