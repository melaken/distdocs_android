package com.example.distdocs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distdocs.R;
import com.example.distdocs.dao.DocumentDao;
import com.example.distdocs.entities.DocsAchetes;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_doc_layout);

//        String doc_name = "CondoLiving.pdf";
//        String doc_cover = "CondoLiving_cover.jpg";
//        File doc = new File(
//                Environment.getExternalStorageDirectory() + "/"
//                        + "distdocs",doc_name);
//        File cover = new File(
//                Environment.getExternalStorageDirectory() + "/"
//                        + "distdocs",doc_cover);


        String path = getIntent().getStringExtra("path");
        File doc = new File(
                Environment.getExternalStorageDirectory() + path);
        Intent intent=getIntent();
        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);

        try {

            pdfView.fromStream(new FileInputStream(doc)).enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0).password(null)
                    .load();
        } catch (FileNotFoundException e) {
            Log.e("DocumentACtivity","error "+e.getMessage());
            e.printStackTrace();
        }

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

    private byte[] readFile(File f) {
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
