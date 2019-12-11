package com.example.distdocs.activities;

import android.app.Activity;
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
import com.example.distdocs.accessories.Startup;
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
    public static GridView gridView;
    //an array to hold the different pdf objects
//    ArrayList<Document> docList= new ArrayList<>();
    public static ProgressDialog progressDialog;
    Context context;
    ImageView homeImage ;
    ImageView libraryImage;
    public static View v;
    public static Activity act;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Log.i("MainActivity","zero");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);
        setListeners();

        v = findViewById(R.id.accueilGridView);
        act = MainActivity.this;

        Log.i("MainActivity","first");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading... Please Wait");

        if(!Startup.isgetDocsCalled){
            progressDialog.show();

        }else
            display();

    }
    private void display(){
        DocumentAdapter docApt = new DocumentAdapter(act,R.layout.doc_layout, Startup.docList);
        gridView = (GridView) v;
        Log.i("docApt",""+docApt);
        gridView.setAdapter(docApt);
        docApt.notifyDataSetChanged();
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
