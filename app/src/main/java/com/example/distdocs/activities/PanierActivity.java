package com.example.distdocs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.distdocs.R;
import com.example.distdocs.accessories.Listeners;
import com.example.distdocs.accessories.PanierAdapter;
import com.example.distdocs.accessories.Startup;

public class PanierActivity extends AppCompatActivity {
    private GridView gridView;
    private ImageView shoppingImage;
    private ImageView homeImage;
    private ImageView libraryImage;
    private TextView shoppingTitle;
    private Button payerButton;
    private TextView totalRecap;

    @Override
    public void onBackPressed(){
        shoppingImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        shoppingTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recap_achat);
        shoppingListener();
        setListeners();
        setTotalPanier();


    }
    private  void setTotalPanier(){
        totalRecap = findViewById(R.id.totalRecap);
        totalRecap.setText(Startup.totalPanier()+"  FCFA");
    }
    private void shoppingListener(){
        gridView = findViewById(R.id.recapGridView);
        PanierAdapter docApt = new PanierAdapter(PanierActivity.this,R.layout.recap_achat_item, Startup.panier);
        Log.i("docApt",""+docApt);
        gridView.setAdapter(docApt);
        docApt.notifyDataSetChanged();
    }
    private void setListeners(){
        homeImage = findViewById(R.id.home);
        libraryImage = findViewById(R.id.library);
        shoppingImage = findViewById(R.id.shopping);
        shoppingTitle =findViewById(R.id.shopping_tittle);

        shoppingImage.setVisibility(View.VISIBLE);
        shoppingTitle.setVisibility(View.VISIBLE);

        homeImage.setOnClickListener(new Listeners(this));
        libraryImage.setOnClickListener(new Listeners(this));
        shoppingImage.setOnClickListener(new Listeners(this));


        shoppingImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorTextbottomTool));
        shoppingTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextbottomTool));


        payerButton = findViewById(R.id.button_payer);
        payerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent =  new Intent();
                intent.setClass(getBaseContext(),PaiementActivity.class);
                startActivity(intent);
            }
        });

    }
}
