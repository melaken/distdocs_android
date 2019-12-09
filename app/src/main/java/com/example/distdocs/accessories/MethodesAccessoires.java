package com.example.distdocs.accessories;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

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

    private  void getMACAdress(){
//        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return  address;
    }
    //else{
    ////            String key = getKey();
    ////            Log.i("MAC",key+"size "+key.length());
    ////            File encryptedFile =  new File(
    ////                    Environment.getExternalStorageDirectory()+ Constante.BOOKS+"/"+ "10"+"_encrypt");
    ////
    ////            try {
    ////                if(!encryptedFile.exists())
    ////                    encryptedFile.createNewFile();
    ////                FileInputStream fis = new FileInputStream(doc);
    ////                FileOutputStream fos = new FileOutputStream(encryptedFile);
    ////
    ////               byte[] outputBytes = CryptoUtils.encrypt(key, fis);
    ////               fos.write(outputBytes);
    ////
    ////                FileInputStream fis = new FileInputStream(encryptedFile);
    ////                byte[] outputBytes = CryptoUtils.decrypt(key, fis);
    ////
    ////                openDoc(outputBytes);
    ////
    ////            } catch (CryptoException e) {
    ////                e.printStackTrace();
    ////            } catch (IOException e) {
    ////                e.printStackTrace();
    ////            }
    //////            openDoc(encryptedFile);
    ////
}
