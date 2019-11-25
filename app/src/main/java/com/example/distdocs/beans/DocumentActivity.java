package com.example.distdocs.beans;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class DocumentActivity extends Activity {

    private static final String TAG = "Download Task";
    /**
     * Read the file and returns the byte array
     * @param file
     * @return the bytes of the file
     */

    private byte[] readFile(URL url, String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(url.toURI());
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return bos != null ? bos.toByteArray() : null;
    }
    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Downloading...");
//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
//            try {
//                if (outputFile != null) {
//                    //progressDialog.dismiss();
//                    // Toast.makeText(context, "Download Successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.e(TAG, "Download Failed");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                //Change button text if exception occurs
//
//               /* new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 3000);*/
//                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());
//
//            }

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }
    }
}
