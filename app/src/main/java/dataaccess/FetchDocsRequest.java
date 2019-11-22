package dataaccess;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.DownloadTask;
import entities.Constante;
import entities.DocsAchetes;
import entities.Document;

public class FetchDocsRequest  extends Activity {

    void getDocsFromServer(){
        DocumentDao docDao = new DocumentDao(this);
        final String lastDate =  docDao.lastInsertDatetime();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constante.SERVER+Constante.nom_base,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("docs");
                            List<DocsAchetes> listeDocs= new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++){

                                //Declaring a json object corresponding to every Document object in our json Array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //Declaring a Document object to add it to the ArrayList  listeDocs
                               DocsAchetes doc = new DocsAchetes();
                                Long doc_id = jsonObject.getLong("doc_id");
                                String premiere_couverture = jsonObject.getString("premiere_couverture");
                                Timestamp lastUpdate = Timestamp.valueOf(jsonObject.getString("last_update"));

                              doc.setDocId(doc_id);
                              doc.setPremiere_couverture(premiere_couverture);
                              doc.setLastUpdate(lastUpdate);

                              listeDocs.add(doc);

                            }
                            //enregistrement de la liste récupérée du serveur dans la bd
                            List<DocsAchetes> listetoStore= new ArrayList<>();
                            for(DocsAchetes d: listeDocs){
                                try {
                                   byte[] byte_doc = DownloadTask.readFile(new URL(Constante.BOOKS+"/"+d.getDocId()));
                                   byte[] byte_cover = DownloadTask.readFile(new URL(Constante.COVER+"/"+d.getPremiere_couverture()));

                                   DocsAchetes doc = new DocsAchetes();
                                   doc.setCover(byte_cover);
                                   doc.setDocument(byte_doc);
                                   doc.setDocId(d.getDocId());
                                   doc.setPremiere_couverture(d.getPremiere_couverture());
                                   doc.setLastUpdate(d.getLastUpdate());

                                    listetoStore.add(doc);

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("lastDate",lastDate);
                return hashMap;
            }
        };

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);
    }
    void getByteValues(String name){

    }
}
