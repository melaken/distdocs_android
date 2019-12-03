package com.example.distdocs.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.R;
import com.example.distdocs.accessories.BiblioAdapter;
import com.example.distdocs.dao.DocumentDao;
import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.DocsAchetes;
import com.example.distdocs.accessories.ResponseCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    DocumentDao docDao = MainActivity.docDao;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing progressDialog

        final Context context = this;
        setContentView(R.layout.bibliotek);

        getNewBoughtDocs(new ResponseCallback() {
            @Override
            public void onLoginSuccess(String result) {
                da= docDao.listAllDocs();
                BiblioAdapter docApt = new BiblioAdapter(FetchDocsRequest.this,R.layout.bibliotek_item,da,context);
                gridView = (GridView) findViewById(R.id.biblioGridView);
                Log.i("docApt",""+docApt);
                gridView.setAdapter(docApt);
                docApt.notifyDataSetChanged();
            }
        },this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bib);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_icon:
                        Toast.makeText(FetchDocsRequest.this, "home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.shopping_cart:
                        Intent intent = new Intent();
                        intent.setClass(context, FetchDocsRequest.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }

        });

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
//                                InputStream doc_stream = new ByteArrayInputStream(Base64.decode(array.get(4).toString(),Base64.DEFAULT));

                              doc.setDocId(doc_id);
                              doc.setPremiere_couverture(premiere_couverture);
                              doc.setLastUpdate(lastUpdate);
                              doc.setCover(cover_stream);
//                              doc.setDocument(doc_stream);

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


        RequestQueue request = Volley.newRequestQueue(context);
        request.add(stringRequest);

    }

}
