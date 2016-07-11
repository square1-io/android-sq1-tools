package io.square1.tools.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by roberto on 21/11/14.
 */
public class ContentProviderUtils {


    public static boolean insertOrUpdate(ContentResolver resolver,
                                         Uri uri,
                                         ContentValues values,
                                         String columnIdName){

        boolean result = false;
        Uri resultURI = null;
        try {

            resultURI =  resolver.insert(uri,values);
            result = (resultURI != null);

        }catch (Exception exc){

        }

        if(resultURI == null){
            //assume unique constraint was violated
            Integer id = values.getAsInteger(columnIdName);
            values.remove(columnIdName);
            int updateCount = resolver.update(uri,values,columnIdName +" = "+id,null);
            result = updateCount > 0;
        }

        return result;
    }

}
