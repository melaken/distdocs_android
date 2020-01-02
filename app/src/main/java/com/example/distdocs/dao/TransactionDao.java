package com.example.distdocs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.distdocs.accessories.Constante;
import com.example.distdocs.entities.Etat;
import com.example.distdocs.entities.Transaction;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    DataBaseHandler dbh;
    SQLiteDatabase db;
    private static final String TAG = "TransactionDao";


    public TransactionDao(Context ctx) {

        dbh = new DataBaseHandler(ctx, Constante.nom_base,null,1);
        //force dbh to create new table if exists
//        open();
//        close();
    }
    public void open(){
        db = dbh.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public void creer(Transaction trans){
        ContentValues vals = new ContentValues();
        vals.put("ref",trans.getReference());
        vals.put("etat",trans.getEtat());
        open();
        db.insert("Transaction", null, vals);


        close();
    }
    public List<Transaction> transPendantes(){
        List<Transaction> liste = new ArrayList<>();
        db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery("select ref,etat from Transactions where etat !='"+ Etat.TERMINE.name()+"'",null);

        while(c.moveToNext()){
            Transaction trans = new Transaction();
            trans.setReference(c.getString(1));
            trans.setEtat(c.getString(2));
            liste.add(trans);
        }

        return liste;
    }
    public void updateWaitingTrans(JSONArray array){
        try {
            open();
            for (int i = 0; i < array.length(); i++) {
                String ref = array.getJSONObject(i).toString();
                Log.i("request", "ref " + ref);

                db.rawQuery("update Transactions set etat = "+Etat.TERMINE.name()+" where ref = "+ref,null);
            }
            close();
        }catch (Throwable e){

        }
    }
}
