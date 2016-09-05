package io.square1.tools.json;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by roberto on 20/07/2016.
 */
public class ParcelableArrayList<T extends Parcelable> implements List<T>, Parcelable {

    private ArrayList<T> mList;
    private Class<T> mClass;

    public ParcelableArrayList(Class<T> tClass){
        mClass = tClass;
        mList = new ArrayList<>();
    }

    @Override
    public void add(int location, T object) {
        mList.add(location,object);
    }

    @Override
    public boolean add(T object) {
        return  mList.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> collection) {
        return  mList.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return mList.addAll(collection);
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public boolean contains(Object object) {
        return mList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mList.containsAll(collection);
    }

    @Override
    public T get(int location) {
        return mList.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return mList.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return mList.lastIndexOf(object);
    }

    @Override
    public ListIterator<T> listIterator() {
        return mList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location) {
        return mList.listIterator(location);
    }

    @Override
    public T remove(int location) {
        return mList.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return mList.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return mList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return mList.retainAll(collection);
    }

    @Override
    public T set(int location, T object) {
        return mList.set(location, object);
    }

    @Override
    public int size() {
        return mList.size();
    }

    @NonNull
    @Override
    public ParcelableArrayList<T> subList(int start, int end) {
        ParcelableArrayList newList = new ParcelableArrayList(mClass);
        newList.addAll(mList.subList(start, end));
        return newList;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] array) {
        return mList.toArray(array);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mClass);
        dest.writeList(this.mList);
    }

    protected ParcelableArrayList(Parcel in) {
        this.mClass = (Class<T>) in.readSerializable();
        this.mList = new ArrayList();
        in.readList(this.mList, mClass.getClassLoader());
    }

    public static final Creator<ParcelableArrayList> CREATOR = new Creator<ParcelableArrayList>() {
        @Override
        public ParcelableArrayList createFromParcel(Parcel source) {
            return new ParcelableArrayList(source);
        }

        @Override
        public ParcelableArrayList[] newArray(int size) {
            return new ParcelableArrayList[size];
        }
    };
}
