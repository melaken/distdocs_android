package com.example.distdocs.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.R;
import com.example.distdocs.accessories.ResponseCallback;
import com.example.distdocs.dao.DocumentDao;
import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.DocsAchetes;
import com.example.distdocs.entities.Document;
import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DocumentActivity extends AppCompatActivity {
    File doc;
    String fileName;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_doc_layout);
        progressDialog = new ProgressDialog(this);

       fileName = getIntent().getStringExtra("fileName");
         doc = new File(
                Environment.getExternalStorageDirectory()+ Constante.BOOKS+"/"+ fileName);
        if(!doc.exists()){

            progressDialog.setMessage("Downloading... Please Wait");
            progressDialog.show();
            dowloadFile(new ResponseCallback() {
                @Override
                public void onLoginSuccess(Object result) {
                    progressDialog.dismiss();
                    openDoc(doc);
                }
            },this,fileName);
        }else{
            openDoc(doc);
        }

    }

    private void openDoc(File doc){
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
    private void dowloadFile(final ResponseCallback responseCallback, final Context context, final String fileName){
        final String docName = fileName;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.sendDocStream,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    InputStream doc_stream = new ByteArrayInputStream(Base64.decode(obj.getString("doc"),Base64.DEFAULT));
                    DocumentDao.storeStream(doc_stream,Constante.BOOKS,fileName);

                    if (responseCallback != null) {
                        responseCallback.onLoginSuccess(response);
                    }

                } catch (JSONException e) {
                    Log.e("dowloadFile","JSONException"+e.getMessage());
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context, "Encountered an error", Toast.LENGTH_LONG).show();

                }catch (IOException e) {
                    Log.e("dowloadFile","IOException"+e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(context, "Encountered an error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }catch (Throwable e) {
                    Log.e("dowloadFile","Throwable error "+e.getMessage());
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context, "Encountered an error", Toast.LENGTH_LONG).show();
                }

            }
        }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ErrorListener");
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("docName",docName);

                return hashMap;
            }
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.HIGH;
            }
        };


        RequestQueue request = Volley.newRequestQueue(context);
        request.add(stringRequest);

    }

}
