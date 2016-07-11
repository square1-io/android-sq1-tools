package io.square1.tools.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


/**
 * Created by roberto on 24/11/14.
 */
public class AppUtils {

    /**
     * Prints out the Hash needed by the Facebook SDK
     * @param ctx
     * @return
     */
    public static ArrayList<String> getCertificateHash(Context ctx){

        final ArrayList<String> result = new ArrayList<String>();
        try {
            final String packageName = ctx.getPackageName();

            PackageInfo info = ctx.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hash =  Base64.encodeToString(md.digest(), Base64.DEFAULT);
                result.add(hash);
              //  Log.d("SHA", "App: " + packageName + "--> " +  hash);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return result;

    }

}
