package com.example.distdocs.dao;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.beans.DownloadTask;
import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.DocsAchetes;
import com.example.distdocs.entities.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchDocsRequest  extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDocsFromServer();
    }
    public void getDocsFromServer(){
        DocumentDao docDao = new DocumentDao(this);
        final String lastDate =  docDao.lastInsertDatetime();
//        final List<DocsAchetes> listeDocs;

        RequestListener reqLis = new RequestListener<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.webService,
                    reqLis
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            JSONArray jsonArray = obj.getJSONArray("docs");
//                            System.out.println("jsonArray size "+jsonArray.length());
//                            List<DocsAchetes> da= new ArrayList<>();
//                            for(int i=0;i<jsonArray.length();i++){
//
//                                //Declaring a json object corresponding to every Document object in our json Array
//                                JSONArray array = jsonArray.getJSONArray(i);
//                                //Declaring a Document object to add it to the ArrayList  listeDocs
//                               DocsAchetes doc = new DocsAchetes();
//                                Long doc_id = Long.parseLong(array.get(1).toString());
//                                String premiere_couverture = array.get(0).toString();
//                                Timestamp lastUpdate = Timestamp.valueOf(array.get(2).toString());
//
//                              doc.setDocId(doc_id);
//                              doc.setPremiere_couverture(premiere_couverture);
//                              doc.setLastUpdate(lastUpdate);
//
//                              da.add(doc);
//
//                            }
//                            listeDocs = da;
//                            System.out.println("size listeDocs "+da.size());
//                            System.out.println("listeDocs and "+da);
//                            enregistrement de la liste récupérée du serveur dans la bd
//                            List<DocsAchetes> listetoStore= new ArrayList<>();
//                            for(DocsAchetes d: listeDocs){
//                                try {
//                                    DownloadTask dt = new DownloadTask(this,Constante.PROTOCOLE+
//                                            Constante.SERVER+Constante.BOOKS+"/"+d.getDocId());
//
//                                   byte[] byte_doc = DownloadTask.readFile(new URL());
//                                   byte[] byte_cover = DownloadTask.readFile(new URL(Constante.PROTOCOLE+
//                                           Constante.SERVER+Constante.COVER+"/"+d.getPremiere_couverture()));
//
//                                   DocsAchetes doc = new DocsAchetes();
//                                   doc.setCover(byte_cover);
//                                   doc.setDocument(byte_doc);
//                                   doc.setDocId(d.getDocId());
//                                   doc.setPremiere_couverture(d.getPremiere_couverture());
//                                   doc.setLastUpdate(d.getLastUpdate());
//
//                                    listetoStore.add(doc);
//
//                                    System.out.println("cover "+doc.getCover().length);
//                                    System.out.println("doc "+doc.getDocument().length);
//
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

//                    }
//                }
                            ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
        };

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

//        List<DocsAchetes> listeDocs = reqLis.getListeDocs();
//        System.out.println("size listeDocs "+listeDocs.size());
//        System.out.println("listeDocs and "+listeDocs);

    }
    void getByteValues(String name){

    }
}
