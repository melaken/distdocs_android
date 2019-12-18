package com.example.distdocs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.distdocs.accessories.Constante;

public class CleDao {
    DataBaseHandler dbh;
    SQLiteDatabase db;
    private static final String TAG = "CleDao";
    public CleDao(Context ctx) {

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

    public String getKey(long docId){
        db = dbh.getReadableDatabase();
        Log.i("Dao getKey","before req");
        String cle = null;
        try {
            Cursor c = db.rawQuery("select cle from Cles where doc_id = "+docId,null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                cle = c.getString(0);
                c.close();
            }

        }catch (Throwable e){
            Log.e("Dao getKey","erro "+e.getMessage());
            e.printStackTrace();
        }
        db.close();
        Log.i("Dao getKey","cle "+cle);
        return cle;
    }
    public void insertKey(String key,long docId){
        ContentValues vals = new ContentValues();
        vals.put("cle",key);
        vals.put("doc_id",docId);
        open();
        db.insert(Constante.table_cle, null, vals);

        close();
    }
}
