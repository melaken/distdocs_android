package com.example.distdocs.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.R;
import com.example.distdocs.accessories.BiblioAdapter;
import com.example.distdocs.accessories.Listeners;
import com.example.distdocs.accessories.MethodesAccessoires;
import com.example.distdocs.accessories.Startup;
import com.example.distdocs.dao.DocumentDao;
import com.example.distdocs.accessories.Constante;
import com.example.distdocs.entities.DocsAchetes;
import com.example.distdocs.accessories.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchDocsRequest  extends Activity {

    List<DocsAchetes> da= new ArrayList<>();
    DocumentDao docDao = new DocumentDao(this);
    GridView gridView;
    ImageView homeImage ;
    ImageView libraryImage;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading... Please Wait");
        progressDialog.show();
        setContentView(R.layout.bibliotek);
        setListeners();

        getNewBoughtDocs(new ResponseCallback() {
            @Override
            public void onLoginSuccess(Object result) {
                fillGridView(context);
            }
            @Override
            public void onLoginError(Object result){
                fillGridView(context);
            }
        },this);
    }
    public void getNewBoughtDocs(final ResponseCallback responseCallback, Context context){
        final String lastDate =  docDao.lastInsertDatetime();
        Log.i("getNewBoughtDocs","lastUpdate "+lastDate);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.newBoughtDocs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("docs");
                            Log.i("getNewBoughtDocs","jsonArray size "+jsonArray.length());

                            for(int i=0;i<jsonArray.length();i++){

                                //Declaring a json object corresponding to every Document object in our json Array
                                JSONArray array = jsonArray.getJSONArray(i);
                                //Declaring a Document object to add it to the ArrayList  listeDocs
                               DocsAchetes doc = new DocsAchetes();
                                int doc_id = Integer.parseInt(array.get(1).toString());
                                String premiere_couverture = array.get(0).toString();
                                Timestamp lastUpdate = Timestamp.valueOf(array.get(2).toString());
                                InputStream cover_stream = new ByteArrayInputStream(Base64.decode(array.get(3).toString(),Base64.DEFAULT));

                              doc.setDocId(doc_id);
                              doc.setPremiere_couverture(premiere_couverture);
                              doc.setLastUpdate(lastUpdate);
                              doc.setCover(cover_stream);

                              da.add(doc);

                            }

                            docDao.updateDbWithDocs(da);
                            Log.i("getNewBoughtDocs","size da "+da.size());
                            if (responseCallback != null) {
                                responseCallback.onLoginSuccess(response);
                            }

                        } catch (JSONException e) {
                            System.out.println("JSONException");
                            e.printStackTrace();
                        }

                    }
                }
                            ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (responseCallback != null) {
                            responseCallback.onLoginError(error);
                        }
                        System.out.println("ErrorListener");
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                if(lastDate != null && !lastDate.isEmpty())
                    hashMap.put("lastDate",lastDate);
                hashMap.put("email",Constante.email);
                return hashMap;
            }
            @Override
            public Request.Priority getPriority() {
                return Priority.HIGH;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constante.requestTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        RequestQueue request = Volley.newRequestQueue(context);
        request.add(stringRequest);

    }

    private void setListeners(){
        homeImage = findViewById(R.id.home);
        libraryImage = findViewById(R.id.library);
        homeImage.setOnClickListener(new Listeners(this));
        libraryImage.setOnClickListener(new Listeners(this));

        TextView libraryTitle =findViewById(R.id.library_tittle);
        libraryImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorTextbottomTool));
        libraryTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextbottomTool));

    }

    private void fillGridView(Context context){
        da= docDao.listAllDocs();
        BiblioAdapter docApt = new BiblioAdapter(FetchDocsRequest.this,R.layout.bibliotek_item,da,context);

        Log.i("docApt",""+docApt);
        gridView = (GridView) findViewById(R.id.biblioGridView);
        gridView.setAdapter(docApt);
        docApt.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}
