package com.example.distdocs.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distdocs.dao.UtilisateurDao;
import com.example.distdocs.entities.Utilisateur;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        if(userExists()){
            intent.setClass(this, FetchDocsActivity.class);
            startActivity(intent);
        }else{
            intent.setClass(this, AccueilActivity.class);
            startActivity(intent);
        }
    }
    private boolean userExists(){
        boolean res = false;

        UtilisateurDao dao =  new UtilisateurDao(this);
        Utilisateur user =  dao.selectUser();
        if(user != null)
            res = true;
        return res;
    }
}
