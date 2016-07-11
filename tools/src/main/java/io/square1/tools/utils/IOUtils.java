package io.square1.tools.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by roberto on 20/11/14.
 */
public class IOUtils {


    public static byte[] getAsByteArray(InputStream in) throws Exception{

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();

    }

    public static byte[] getAsByteArray(String url) throws Exception{
        return getAsByteArray(getFromURL(url));
    }

    public static InputStream getFromURL(String url){

        try {
            URL link = new URL(url);
            URLConnection yc = link.openConnection();
            return yc.getInputStream();
        }catch (Exception exc){

        }
        return null;
    }

    public static void copyStream(InputStream istream, OutputStream ostream) throws IOException
    {
        final int buffer_size = 1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count = istream.read(bytes, 0, buffer_size);
                if(count == -1)
                    break;

                if(Thread.currentThread().isInterrupted()){
                    throw new InterruptedIOException(" thread interrupted !");
                }


                ostream.write(bytes, 0, count);
            } // end for

        } // end try

        catch(Exception ex)
        {}// end empty catch

    } // end method CopyStream

    public static String readStringFileFromAssets(Context context, String fileName){
       AssetManager mngr =  context.getAssets();
        CleanupStack clean = new CleanupStack();
        StringBuilder content = new StringBuilder();
        try {

           InputStream in = clean.add( mngr.open(fileName) );
           InputStreamReader inReader = clean.add(new InputStreamReader(in, Charset.forName("UTF-8")));
           BufferedReader reader = clean.add(new BufferedReader(inReader));
           String line = null;

           while ((line = reader.readLine()) != null){
               content.append(line);
           }


        }catch (Exception exc){

        }finally {
            clean.close();
        }

        return content.toString();
    }

    public static byte[] readFileToByteArray(File in) {

        FileInputStream fileInputStream = null;

        byte[] content = new byte[(int) in.length()];

        try {

            fileInputStream = new FileInputStream(in);
            fileInputStream.read(content);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileInputStream != null) {

                try {
                    fileInputStream.close();
                }
                catch (Exception e) {
                }
                ;
            }
        }

        return content;
    }

}
