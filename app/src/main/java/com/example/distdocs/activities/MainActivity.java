package com.example.distdocs.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.R;
import com.example.distdocs.accessories.Listeners;
import com.example.distdocs.dao.DocumentDao;
import com.example.distdocs.accessories.Constante;
import com.example.distdocs.entities.Document;
import com.example.distdocs.accessories.DocumentAdapter;
import com.example.distdocs.accessories.ResponseCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //ListView to show the fetched Pdfs from the server
    GridView gridView;
    //an array to hold the different pdf objects
    ArrayList<Document> docList= new ArrayList<>();
    private ProgressDialog progressDialog;
    Context context;
    ImageView homeImage ;
    ImageView libraryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Log.i("MainActivity","zero");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);
        setListeners();
        Log.i("MainActivity","first");


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading... Please Wait");
        progressDialog.show();
        getDocs(new ResponseCallback() {
            @Override
            public void onLoginSuccess(Object result) {
                progressDialog.dismiss();
                DocumentAdapter docApt = new DocumentAdapter(MainActivity.this,R.layout.doc_layout,docList);
                gridView = (GridView) findViewById(R.id.accueilGridView);
                Log.i("docApt",""+docApt);
                gridView.setAdapter(docApt);
                docApt.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        try{
            switch (item.getItemId())
            {
                case R.id.menu_check:
                    intent = new Intent();
                    intent.setClass(this, FetchDocsRequest.class);
                    startActivity(intent);
                    return true;
                case R.id.open:
                    intent = new Intent();
                    intent.setClass(this, DocumentActivity.class);
                    startActivity(intent);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }
    private void getDocs(final ResponseCallback responseCallback){
        System.out.println("In doInBackground downloadTask");
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
                            e.printStackTrace();
                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getDocs ErrorListener","");
                        error.printStackTrace();
                    }
                }
        ){

            @Override
            public Request.Priority getPriority() {
                return Priority.HIGH;
            }
        };

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

        System.out.println("finally size  "+docList.size());

    }

    public void setListeners(){
        homeImage = findViewById(R.id.home);
        libraryImage = findViewById(R.id.library);
        homeImage.setOnClickListener(new Listeners(this));
        libraryImage.setOnClickListener(new Listeners(this));

        TextView homeTitle = findViewById(R.id.home_tittle);
        homeImage.setColorFilter(ContextCompat.getColor(context, R.color.colorTextbottomTool));
        homeTitle.setTextColor(ContextCompat.getColor(context, R.color.colorTextbottomTool));

    }
}
