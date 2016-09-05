package io.square1.tools.http.data.element;

import java.io.IOException;
import java.io.OutputStream;

import io.square1.tools.http.data.MultipartPostBody;

public class ByteFileElement implements MultipartElement {

        private String mName;
        private String mFileName;
        private byte[] mContent;

        public ByteFileElement(String paramName, String fileName, byte[] content) {
            mName = paramName;
            mFileName = fileName;
            mContent = content;
        }

        @Override
        public String getHeader() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(MultipartPostBody.FORMAT_ELEMENT_FILE_HEADER, mName, mFileName));
            builder.append(MultipartPostBody.FORMAT_ELEMENT_FILE_CONTENT_TYPE);
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
            outputStream.write(mContent);

        }
    }