package com.example.distdocs.accessories;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MethodesAccessoires {
    InputStream getByteValues(File file){
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            System.out.println("after read stream");


        } catch (FileNotFoundException e) {
            System.out.println("Error in DocumentActivity");
            e.printStackTrace();
        }

        return stream;
    }

    /**
     * Read the file and returns the byte array
     * @param file
     * @return the bytes of the file
     */

    public static byte[] readFile(File f) {
        ByteArrayOutputStream bos = null;
        try {
            // File f = new File(url.toURI());
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

}
