package io.square1.tools.http.post;

import android.text.TextUtils;



import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by roberto on 27/04/2016.
 */
 public class MultipartPostBody {

    public  final static String NEW_LINE = "\r\n";
    public  final static String FORMAT_ELEMENT_HEADER = "Content-Disposition: form-data; name=\"%s\"\r\n\r\n";
    public  final static String FORMAT_ELEMENT_FILE_HEADER = "Content-Disposition: attachment; name=\"%1$s\"; filename=\"%2$s\"\r\n";
    public  final static String FORMAT_ELEMENT_FILE_CONTENT_TYPE = "Content-Type: application/octet-stream\r\n\r\n";




    private final String mBoundary;
    private ArrayList<MultipartElement> mElements;

    public MultipartPostBody(ArrayList<MultipartElement> elements) {
        mBoundary = generateBoundary();
        mElements = elements;
    }


    public final void addParam(String name, String value) {
        //name has to be , value can be empty string
        if (TextUtils.isEmpty(name) == false && value != null) {
            mElements.add(new StringElement(name, value));
        }
    }


    public void addParam(String name, String fileName, byte[] content) {

        if (content != null && content.length > 0) {
            mElements.add(new ByteFileElement(name, fileName, content));
        }
    }



    public void output(OutputStream outputStream) throws Exception {

      //  try {

            outputStream.write(NEW_LINE.getBytes());

            final String boundary = String.format("--%1$s%2$s", mBoundary, NEW_LINE);
            //loop over elements and add boundary and new line afterwards
            for (MultipartElement element : mElements) {
                outputStream.write(boundary.getBytes());
                element.write(outputStream);
                outputStream.write(NEW_LINE.getBytes());
            }

            //write closing boundary
            outputStream.write("--".getBytes());
            outputStream.write(mBoundary.getBytes());
            outputStream.write("--".getBytes());


      //  } catch (Exception e) {

     //   }

    }

    public String getBodyContentType() {

        final StringBuilder buffer = new StringBuilder();
        buffer.append("multipart/form-data; boundary=");
        buffer.append(mBoundary);
        buffer.append("; charset=");
        buffer.append("UTF-8");

        return buffer.toString();
    }

    /**
     * The pool of ASCII chars to be used for generating a multipart boundary.
     */
    private final static char[] MULTIPART_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();


    private String generateBoundary() {

        final StringBuilder buffer = new StringBuilder();
        final Random rand = new Random();
        final int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }
}
