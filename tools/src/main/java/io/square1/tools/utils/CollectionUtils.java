package io.square1.tools.utils;

/**
 * Created by roberto on 13/03/15.
 */
public class CollectionUtils {

    public static String serializeCollection(Iterable iterable, char separator){

        StringBuilder builder = new StringBuilder();

        for(Object o : iterable){
            if(builder.length() > 0){
                builder.append(separator);
            }
            builder.append(o.toString());
        }

        return builder.toString();
    }

}
