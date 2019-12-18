package com.example.distdocs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.distdocs.accessories.Constante;
import com.example.distdocs.entities.DocsAchetes;
import com.example.distdocs.entities.Utilisateur;

public class UtilisateurDao {
    DataBaseHandler dbh;
    SQLiteDatabase db;
    private static final String TAG = "UtilisateurDao";


    public UtilisateurDao(Context ctx) {

        dbh = new DataBaseHandler(ctx, Constante.nom_base,null,1);
    }
    public void open(){
        db = dbh.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public Utilisateur selectUser(){
        Utilisateur user =  null;
        db = dbh.getReadableDatabase();
        try {
            Cursor c = db.rawQuery("select id, email, nom,prenom from Utilisateur", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                user = new Utilisateur();
                user.setId(c.getInt(0));
                user.setEmail(c.getString(1));
                user.setNom(c.getString(2));
                user.setPrenom(c.getString(3));

            }
            c.close();
        }catch (Throwable e){
            e.printStackTrace();
        }
        db.close();
        return user;
    }
    public void storeUser(Utilisateur user){
        ContentValues vals = new ContentValues();
        vals.put("id",user.getId());
        vals.put("email",user.getEmail());
        vals.put("nom",user.getNom());
        vals.put("prenom",user.getPrenom());
        try {
           open();
            long res = db.insert("Utilisateur", null, vals);
        }catch(Throwable e){
            Log.e("storeUser","error "+e.getMessage());
            e.printStackTrace();
        }
        close();
    }
}
