package com.example.distdocs.beans;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.Document;
import com.example.distdocs.entities.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;
    private ProgressDialog progressDialog;
    List<Document> liste ;
//    ResponseCallback responseCallback;

    public List<Document> getListe(){
        return liste;
    }
    public DownloadTask(Context context ){
        super();
//        this.context = cont;
        liste = new ArrayList<>();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading... Please Wait");
        progressDialog.show();
    }
     public void getDocs(final ResponseCallback responseCallback){
            System.out.println("In doInBackground downloadTask");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.getDocs,
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
                                    Document doc = new Document();
                                    doc.setId(Integer.parseInt(array.get(0).toString()));
                                    doc.setPrix(Float.parseFloat(array.get(1).toString()));
                                   // doc.setImage((array.get(2).toString().getBytes()));

                                    liste.add(doc);
                                }

                                System.out.println("size da "+liste.size());
                                if (responseCallback != null) {
                                    responseCallback.onLoginSuccess(response);
                                    progressDialog.dismiss();
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
                public Request.Priority getPriority() {
                    return Priority.HIGH;
                }
            };

         RequestQueue request = Volley.newRequestQueue(context);
         request.add(stringRequest);
        }
}
