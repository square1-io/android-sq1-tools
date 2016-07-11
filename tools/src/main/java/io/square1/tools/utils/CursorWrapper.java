package io.square1.tools.utils;

import android.database.Cursor;

public class CursorWrapper {


    public static boolean isEqual(CursorWrapper wrapper, Cursor cursor){

        if(wrapper == null && cursor != null) return false;
       if( wrapper != null && wrapper.mCursor == cursor ) return true;

       return false;

    }
	
	private Cursor mCursor;
	
	public CursorWrapper(Cursor cursor , boolean moveToFirst){
		mCursor = cursor;
		
		if(moveToFirst && mCursor != null){
			mCursor.moveToFirst();
		}
		
	}

    public boolean swapCursor(Cursor cursor){

        boolean changed = (mCursor != cursor);

        if(mCursor != null &&
                mCursor != cursor){
            mCursor.close();
            mCursor = cursor;
        }

        return changed;
    }

	public String getString(String columnId){
		int index = mCursor.getColumnIndex(columnId);
		return mCursor.getString(index);
	}
	
	public long getLong(String columnId){
		int index = mCursor.getColumnIndex(columnId);
		return mCursor.getLong(index);
	}
    public long getLongAtColumn(int columnIndex){
        return mCursor.getLong(columnIndex);
    }
	
	public int getInt(String columnId){
		int index = mCursor.getColumnIndex(columnId);
		return mCursor.getInt(index);
	}
	
	public boolean getBoolean(String columnId){
		int index = mCursor.getColumnIndex(columnId);
		return mCursor.getInt(index) == 1;
	}
	
	public void close(){
		mCursor.close();
	}

	public boolean moveToPosition(int index) {
		return mCursor.moveToPosition(index);
		
	}

	public int getCount() {
		
		return mCursor == null ? 0 : mCursor.getCount();
	}

    public boolean isClosed() {
        return mCursor != null ? mCursor.isClosed() : true;
    }

    public int getCurrentPosition() {
        return mCursor.getPosition();
    }

    public boolean move2Next() {
        return mCursor.moveToNext();
    }
}
