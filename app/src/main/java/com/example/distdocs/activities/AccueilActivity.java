package com.example.distdocs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.distdocs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccueilActivity extends AppCompatActivity {

    @BindView(R.id.btn_connect) Button btn_conn;
    @BindView(R.id.btn_createAccount) Button btn_create_acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        ButterKnife.bind(this);

        Intent intent = new Intent();
        btn_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(AccueilActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(AccueilActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
