package io.square1.tools.utils;

import java.io.Closeable;
import java.util.ArrayList;

public class CleanupStack {
    private ArrayList<Closeable> mCloseables ;
	
	public CleanupStack(){
		mCloseables = new ArrayList<Closeable>();
	}
	
	public <T extends Closeable> T add(T closeable){
		
		if(closeable == null)
			return null;
		
		mCloseables.add(closeable);
		return closeable;
	}
	
	public void close(){
		for(Closeable closeable : mCloseables){
			try{closeable.close();}catch (Exception exc) {}
		}
		mCloseables.clear();
	}

}