package com.example.distdocs.activities;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.distdocs.R;
import com.example.distdocs.accessories.Listeners;
import com.example.distdocs.accessories.MethodesAccessoires;
import com.example.distdocs.accessories.ResponseCallback;
import com.example.distdocs.accessories.Startup;
import com.example.distdocs.accessories.DocumentAdapter;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    //ListView to show the fetched Pdfs from the server
    private GridView gridView;
    private ProgressDialog progressDialog;
    private TextView noInternet;
    private View v;


    Context context;
    ImageView homeImage ;
    ImageView libraryImage;
    ImageView shoppingImage;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Log.i("MainActivity","zero");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);
        setListeners();

        setNavigationView();
        gridView = findViewById(R.id.accueilGridView);


        Log.i("MainActivity","first");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading... Please Wait");

        if(!Startup.isgetDocsCalled){
            progressDialog.show();
            Startup.getDocs(new ResponseCallback() {
                @Override
                public void onLoginError(Object results){
                    progressDialog.dismiss();
                    displayNoConnectionTOServer();
                }
                @Override
                public void onLoginSuccess(Object result) {
                    progressDialog.dismiss();
                        displayDocs();
                        Startup.isgetDocsCalled = true;
                }
            },context);
        }else
            displayDocs();

    }
    private void displayDocs(){
        DocumentAdapter docApt = new DocumentAdapter(MainActivity.this,R.layout.doc_layout, Startup.docList);
        Log.i("docApt",""+docApt);
        gridView.setAdapter(docApt);
        docApt.notifyDataSetChanged();
    }
    private void displayNoConnectionTOServer(){
        noInternet = findViewById(R.id.textViewNoIternet);
        gridView.setVisibility(View.GONE);
        noInternet.setVisibility(View.VISIBLE);
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
        if (t.onOptionsItemSelected(item)) {
            return true;
        }else {
            try {
                switch (item.getItemId()) {
                    case R.id.reload:
                        intent.setClass(this, MainActivity.class);
                        Startup.isgetDocsCalled = false;
                        startActivity(intent);
                        return true;
                    case R.id.signup: intent.setClass(this, SignupActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.login: intent.setClass(this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
        }
    }

    private void setListeners(){
        homeImage = findViewById(R.id.home);
        libraryImage = findViewById(R.id.library);
        homeImage.setOnClickListener(new Listeners(this));
        libraryImage.setOnClickListener(new Listeners(this));

        TextView homeTitle = findViewById(R.id.home_tittle);
        homeImage.setColorFilter(ContextCompat.getColor(context, R.color.colorTextbottomTool));
        homeTitle.setTextColor(ContextCompat.getColor(context, R.color.colorTextbottomTool));

//        shoppingImage = findViewById(R.id.shopping_cart);
//        shoppingImage.setOnClickListener(new Listeners(this));

    }
    private void setNavigationView(){

        dl = (DrawerLayout)findViewById(R.id.main_drawer);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close) ;

        dl.addDrawerListener(t);

        t.setDrawerIndicatorEnabled(true);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        nav = (NavigationView)findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent();
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mydocs:
                        intent.setClass(context, FetchDocsRequest.class);
                        startActivity(intent);
                        break;
                    case R.id.myhome:
                        intent.setClass(context, MainActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        return true;
                }


                return true;

            }
        });

    }

}
