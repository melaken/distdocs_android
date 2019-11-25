package com.example.distdocs.beans;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;
    private String downloadUrl = "";
    private ProgressDialog progressDialog;
    private ByteArrayOutputStream bos = new ByteArrayOutputStream();

    public DownloadTask(Context context, String downloadUrl) {
        this.context = context;
        this.downloadUrl = downloadUrl;

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are getting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()+ " " + c.getResponseMessage());
                }


                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length

                while ((len1 = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len1);//Write new file

                }
                // return bos != null ? bos.toByteArray() : null;
//                InputStream is;
//                byte[] bytes = IOUtils.toByteArray(is);

                //Close all connection after doing task
                bos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                Log.e(TAG, "Download Error Exception " + e.getMessage());

            }
            new Thread(new Runnable(){

                @Override
                public void run() {

                    try{
                        Thread.sleep(5000);

                    }catch(Exception e){

                    }

                }
            }).run();
            progressDialog.dismiss();

            return null;
        }
    }

    public ByteArrayOutputStream getBos() {
        return bos;
    }

    public void setBos(ByteArrayOutputStream bos) {
        this.bos = bos;
    }
}
