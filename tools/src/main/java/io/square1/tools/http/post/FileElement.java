package io.square1.tools.http.post;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import io.square1.tools.utils.IOUtils;

public class FileElement implements MultipartElement {

        private String mName;
        private File mFile;


        public FileElement(String name, File file) {
            mName = name;
            mFile = file;
        }

        @Override
        public String getHeader() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(MultipartPostBody.FORMAT_ELEMENT_FILE_HEADER, mName, mFile.getName()));
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

            byte[] fileContent = IOUtils.readFileToByteArray(mFile);
            outputStream.write(fileContent);
        }
    }