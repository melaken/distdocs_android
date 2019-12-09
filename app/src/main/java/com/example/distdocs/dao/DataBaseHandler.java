package com.example.distdocs.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.distdocs.accessories.Constante;

public class DataBaseHandler extends SQLiteOpenHelper {
    String query_user = "create table Utilisateur(id INTEGER PRIMARY KEY,email TEXT UNIQUE,mot_de_passe TEXT NOT NULL, nom TEXT NOT NULL, prenom TEXT)";
    String query = "create table DocsAchetes(doc_id INTEGER PRIMARY KEY,premiere_couverture TEXT not null ," +
            "last_update datetime NOT NULL) ";
    String query_key = "create table "+ Constante.table_cle+"(cle TEXT PRIMARY KEY)";


    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
        db.execSQL(query_user);
        db.execSQL(query_key);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion == 3)
            db.execSQL(query_key);

    }
}
