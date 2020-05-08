package com.example.distdocs.accessories;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.R;
import com.example.distdocs.dao.UtilisateurDao;
import com.example.distdocs.entities.Document;
import com.example.distdocs.entities.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Startup extends Application {
    public static ArrayList<Document> docList= new ArrayList<>();
    public static boolean isgetDocsCalled = false;
    public static ArrayList<Document> panier= new ArrayList<>();
    public static Activity fetchDocsActivity;
    private static  float total=0;

    @Override
    public void onCreate(){
        super.onCreate();
        createDirectories();
    }
    private void createDirectories(){
        String books_dir = Environment.getExternalStorageDirectory()+Constante.BOOKS;
        String cover_dir = Environment.getExternalStorageDirectory()+Constante.COVER;
        

        File books_file = new File(books_dir);
        File cover_file = new File(cover_dir);

        if(!books_file.exists()){
            books_file.mkdirs();
        }
        if(!cover_file.exists()){
            cover_file.mkdirs();
        }
    }
    public static void getDocs(final ResponseCallback responseCallback, Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.getDocs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("docs");
                            Log.i("getDocs","jsonArray size "+jsonArray.length());

                            for(int i=0;i<jsonArray.length();i++){

                                //Declaring a json object corresponding to every Document object in our json Array
                                JSONArray array = jsonArray.getJSONArray(i);
                                //Declaring a Document object to add it to the ArrayList  listeDocs
                                Document doc = new Document();
                                doc.setId(Integer.parseInt(array.get(0).toString()));
                                doc.setPrix(Float.parseFloat(array.get(1).toString()));

                                String str = array.get(2).toString();
                                InputStream is = new ByteArrayInputStream(Base64.decode(str,Base64.DEFAULT));
                                Bitmap bitmap =  BitmapFactory.decodeStream(is);
                                doc.setImage(bitmap);
                                doc.setPremiereCouverture(str);
                                doc.setDateParution(java.sql.Date.valueOf(array.getString(3)));

                                docList.add(doc);
                            }

                            System.out.println("size da "+docList.size());
                            if (responseCallback != null) {
                                responseCallback.onLoginSuccess(response);
                            }

                        } catch (JSONException e) {
                            Log.e("getDocs JSONException",e.getMessage());
                            e.printStackTrace();
                        }catch (Throwable e) {
                            Log.e("getDocs ErrorListener 0",""+e.getMessage());
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
                        Log.e("getDocs ErrorListener",""+error.getMessage());
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constante.requestTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );


        request.add(stringRequest);

        System.out.println("finally size  "+docList.size());

    }
    public static Utilisateur getUser(Context context){
        UtilisateurDao dao = new UtilisateurDao(context);
        Utilisateur user = dao.selectUser();
        return user;
    }

    public static void addDocToShoppingCart(long docId){
        Document doc = findDocWithId(docId,docList);
        panier.add(doc);
        total = total + doc.getPrix();
//        if(panier.size() == 1)
//            displayShoppingCart(mainActivity);
    }
    public static void removeDocToShoppingCart(long docId){
        Document doc = findDocWithId(docId,panier);
        panier.remove(doc);
        total = total - doc.getPrix();
//        if(panier.size()==0)
//            hideShoppingCart(mainActivity);
    }
    //retrouve un doc par son id
    private static Document findDocWithId(long id,ArrayList<Document> liste) {
        Document doc_recherche = null;
        for(Document d: liste) {
            if(d.getId() == id) {
                doc_recherche = d;
                break;
            }
        }
        return doc_recherche;
    }
    public static void hideShoppingCart(Activity act){
        ((ImageView)act.findViewById(R.id.shopping)).setVisibility(View.GONE);
        ((TextView)act.findViewById(R.id.shopping_tittle)).setVisibility(View.GONE);
    }
    public static void displayShoppingCart(Activity act){
        ((ImageView)act.findViewById(R.id.shopping)).setVisibility(View.VISIBLE);
        ((TextView)act.findViewById(R.id.shopping_tittle)).setVisibility(View.VISIBLE);
    }
    public static boolean ifPanierContainsDoc(long docId){
        boolean result =  false;
        for(Document d : panier){
            if(d.getId() == docId) {
                result = true;
                break;
            }
        }
        return result;
    }
    public static float totalPanier(){
        return total;
    }

    public static String panierAsString(){
         long[] tab = new long[panier.size()];
         int i =0;
        for(Document d : panier){
            tab[i] = d.getId();
            i++;
        }
        Log.i("panierAsString",Arrays.toString(tab)+"");
        return Arrays.toString(tab);
    }
}
