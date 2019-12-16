package com.example.distdocs.accessories;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MethodesAccessoires extends Activity {

    public static boolean isServerReachabable(URL url) {
        int code = 0;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            code = connection.getResponseCode();
        } catch (Throwable e) {
            e.printStackTrace();
        }


        return code == 200;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
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
