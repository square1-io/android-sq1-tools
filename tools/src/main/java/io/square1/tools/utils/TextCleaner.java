package io.square1.tools.utils;

/**
 * Created by roberto on 20/12/14.
 */
public class TextCleaner {


    public static String clean(String in){

      int start = -1;

        while(in.indexOf("[/caption]")  >= 0){
            in = in.replace("[/caption]","</div>");
        }

       while( (start = in.indexOf("[caption") ) >= 0){
           int close = in.indexOf(']',start);
           String sub = in.substring(start,close + 1);
           in = in.replace(sub,"<div class=\"caption\">");
       }

        return in;
    }

}
