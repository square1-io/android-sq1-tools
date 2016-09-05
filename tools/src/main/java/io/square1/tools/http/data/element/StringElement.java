package io.square1.tools.http.data.element;

import java.io.IOException;
import java.io.OutputStream;

import io.square1.tools.http.data.MultipartPostBody;

public class StringElement implements MultipartElement {

        private String mName;
        private String mValue;


        public StringElement(String name, String value) {
            mName = name;
            mValue = value;
        }

        @Override
        public String getHeader() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(MultipartPostBody.FORMAT_ELEMENT_HEADER, mName));
            return builder.toString();
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException {
            final String header = getHeader();
            outputStream.write(header.getBytes());
            outputStream.write(mValue.getBytes());

        }
    }