package io.square1.tools.json;

import android.content.Intent;
import android.text.TextUtils;

import org.json.JSONObject;

public class JsonDataUtils {


    public static Double getDouble(JSONObject object,
                                   final String paramName,
                                   Double fallBack){

        try {
            return object.getDouble(paramName);
        }catch (Exception e){
            return fallBack;
        }
    }

    public static Integer getInteger(JSONObject object,
                                    final String paramName,
                                    Integer fallBack){

        try {
            return object.getInt(paramName);
        }catch (Exception e){
            return fallBack;
        }
    }

    public static Long getLong(JSONObject object,
                                     final String paramName,
                                     Long fallBack){

        try {
            return object.getLong(paramName);
        }catch (Exception e){
            return fallBack;
        }
    }

    /**
     * returns a non null string for the give parameter
     * if the parameter is A string and the value is "null" the returned value will be an empty String
     * @param object
     * @param paramName
     * @return the string value or an empty string
     */
    public static String optString(JSONObject object,
                                   final String paramName) {

        return optString(object, paramName, "");
    }

    /**
     * returns a non null string for the give parameter
     * if the parameter is A string and the value is "null" the returned value will be defaultValue if
     * default value is not provided it will be an empty string
     *
     * @param object
     * @param paramName
     * @param defaultValue
     * @return
     */

    public static String optString(JSONObject object,
                                   final String paramName,
                                   String defaultValue) {

        if (TextUtils.isEmpty(defaultValue) == true) {
            defaultValue = "";
        }

        String value = object.optString(paramName, defaultValue);

        if ("null".equalsIgnoreCase(value) == true) {
            value = defaultValue;
        }

        return value;

    }

    /**
     * Gets the value and returns it as a boolean :
     * if null will return false
     * if String will return true is string == "true" or string contains a numeric value > 0
     * if Number will return true if number > 0
     *
     * @param object the json object
     * @param paramName the name of the json field
     * @param defaultValue the default value to return if the field is null
     * @return the value of the
     */
    public static boolean optBoolean(JSONObject object,
                                     final String paramName,
                                     boolean defaultValue) {

        Object obj = object.opt(paramName);

        if (obj == null) {
            return defaultValue;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof String) {

            if ("true".equalsIgnoreCase((String) obj) == true) {
                return true;
            }

            if ("false".equalsIgnoreCase((String) obj) == true) {
                return false;
            }
        } else if (obj instanceof Number) {
            return ((Integer) obj) > 0;
        }

        return false;
    }

}