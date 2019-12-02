package com.example.distdocs.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.DocsAchetes;
import com.example.distdocs.entities.ResponseCallback;

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
    //Progress bar to check the progress of obtaining pdfs
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing progressDialog

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading...");

        getDocsFromServer(new ResponseCallback() {
            @Override
            public void onLoginSuccess(String result) {
                List<DocsAchetes> liste = docDao.listAllDocs();
                for(DocsAchetes d : liste){
                    System.out.println("cover "+d.getPremiere_couverture()+" id "+d.getDocId());
                }
            }
        });




    }
    public void getDocsFromServer(final ResponseCallback responseCallback){
        final String lastDate =  docDao.lastInsertDatetime();
        System.out.println("lastUpdate "+lastDate);

//        RequestListener reqLis = new RequestListener<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.webService,
//                    reqLis
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("docs");
                            System.out.println("jsonArray size "+jsonArray.length());

                            for(int i=0;i<jsonArray.length();i++){

                                //Declaring a json object corresponding to every Document object in our json Array
                                JSONArray array = jsonArray.getJSONArray(i);
                                //Declaring a Document object to add it to the ArrayList  listeDocs
                               DocsAchetes doc = new DocsAchetes();
                                int doc_id = Integer.parseInt(array.get(1).toString());
                                String premiere_couverture = array.get(0).toString();
                                Timestamp lastUpdate = Timestamp.valueOf(array.get(2).toString());
                                InputStream cover_stream = new ByteArrayInputStream(array.get(3).toString().getBytes());
                                InputStream doc_stream = new ByteArrayInputStream(array.get(4).toString().getBytes());


                              doc.setDocId(doc_id);
                              doc.setPremiere_couverture(premiere_couverture);
                              doc.setLastUpdate(lastUpdate);
                              doc.setCover(cover_stream);
                              doc.setDocument(doc_stream);

                              da.add(doc);

                            }

                            docDao.updateDbWithDocs(da);
                            System.out.println("size da "+da.size());
                            System.out.println("da and "+da);
                            if (responseCallback != null) {
                                responseCallback.onLoginSuccess(response);
//                                progressDialog.dismiss();
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


//        ApplicationController.getInstance().addToRequestQueue(stringRequest);
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

    }

}
