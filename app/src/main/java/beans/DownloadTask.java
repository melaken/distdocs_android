package beans;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;
    private String downloadUrl = "", downloadFileName = "";

    public DownloadTask(Context context, String downloadUrl) {
        this.context = context;
        this.downloadUrl = downloadUrl;
       // downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf( '/' ),downloadUrl
              //  .length());//Create file name by picking download file name from URL
       // Log.e(TAG, downloadFileName);

        //Start Downloading Task
    }
    public static byte[] readFile(URL url) {
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


}
