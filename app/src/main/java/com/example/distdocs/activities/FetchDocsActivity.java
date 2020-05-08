package com.example.distdocs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.distdocs.R;
import com.example.distdocs.accessories.DocumentAdapter;
import com.example.distdocs.accessories.Listeners;
import com.example.distdocs.accessories.MethodesAccessoires;
import com.example.distdocs.accessories.ResponseCallback;
import com.example.distdocs.accessories.Startup;
import com.example.distdocs.dao.UtilisateurDao;
import com.example.distdocs.entities.Utilisateur;
import com.google.android.material.navigation.NavigationView;

public class FetchDocsActivity extends AppCompatActivity {

    //ListView to show the fetched Pdfs from the server
    private GridView gridView;
    private ProgressDialog progressDialog;
    private TextView noInternet;
    private View v;


    Context context;
    ImageView homeImage ;
    ImageView libraryImage;
    ImageView shoppingImage;
//    ImageView addToShoppingCart;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Log.i("FetchDocsActivity","zero");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);
        setListeners();

        MethodesAccessoires.notification(this);

        setNavigationView();
        gridView = findViewById(R.id.accueilGridView);


        Log.i("FetchDocsActivity","first");
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

        Startup.fetchDocsActivity = this;
    }
    private void displayDocs(){
        DocumentAdapter docApt = new DocumentAdapter(FetchDocsActivity.this,R.layout.doc_layout, Startup.docList);
        Log.i("docApt",""+docApt);
        gridView.setAdapter(docApt);
        docApt.notifyDataSetChanged();
//        ImageView addToShoppingCart = findViewById(R.id.addToShoppingCart);
//        addToShoppingCart.setOnClickListener(new Listeners(this));
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

//        UtilisateurDao dao =  new UtilisateurDao(this);
//        Utilisateur user =  dao.selectUser();
//        if(user != null){
//            MenuItem item = menu.findItem(R.id.signup);
//            item.setVisible(false);
//            MenuItem item1 = menu.findItem(R.id.login);
//            item1.setVisible(false);
//        }

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
                        intent.setClass(this, FetchDocsActivity.class);
                        Startup.isgetDocsCalled = false;
                        startActivity(intent);
                        return true;
//                    case R.id.signup: intent.setClass(this, SignupActivity.class);
//                        startActivity(intent);
//                        return true;
//                    case R.id.login: intent.setClass(this, LoginActivity.class);
//                        startActivity(intent);
//                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
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

        shoppingImage = findViewById(R.id.shopping);
        shoppingImage.setOnClickListener(new Listeners(this));
        if(Startup.panier.size()!=0)
            shoppingImage.setVisibility(View.VISIBLE);

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
                        Toast.makeText(FetchDocsActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mydocs:
                        intent.setClass(context, FetchDocsRequest.class);
                        startActivity(intent);
                        break;
                    case R.id.myhome:
                        intent.setClass(context, FetchDocsActivity.class);
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
