package io.square1.tools.json;

import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by roberto on 14/07/2016.
 */
public interface JsonMapper extends Parcelable {


     String getJsonFieldForId();
     String getJsonFieldForCreatedAt();
     String getJsonFieldForUpdatedAt();
     String getDateFormat();

     ///some pagination fields

    /**
     * returns the name of the field in the pagination object
     * containing the total number of results
     * @return the field name.
     */
      String getJsonFieldForPaginationTotalResults();

    /**
     * returns the name of the field in the pagination object
     * containing the current page
     * @return the field name.
     */
      String getJsonFieldForPaginationCurrentPage();

    /**
     * returns the name of the field in the pagination object
     * containing the total number of pages
     * @return the field name.
     */
      String getJsonFieldForPaginationPages();


    /**
     * return true if the json has a valid pagination object
     * @param object
     * @return true / false
     */
    boolean hasValidPagination(JSONObject object);

}
