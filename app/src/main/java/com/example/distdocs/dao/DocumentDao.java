package com.example.distdocs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.DocsAchetes;

import java.util.List;

public class DocumentDao {
    DataBaseHandler dbh;
    SQLiteDatabase db;


    public DocumentDao(Context ctx) {
        dbh = new DataBaseHandler(ctx, Constante.nom_base,null,1);
    }
    public void open(){
        db = dbh.getWritableDatabase();
    }

    public void close(){
        db.close();
    }
    public void fetchNewDocs(){

    }
    public void updateDbWithDocs(List<DocsAchetes> liste){
        for(DocsAchetes doc : liste)
            addDocument(doc);
    }
    public long addDocument(DocsAchetes doc){
        ContentValues vals = new ContentValues();

        vals.put("doc_id",doc.getDocId());
        vals.put("premiere_couverture",doc.getPremiere_couverture());
        vals.put("last_update",doc.getLastUpdate().toString());
        vals.put("cover",doc.getCover());
        vals.put("document",doc.getDocument());

        return db.insert(Constante.table_docs,null,vals);
    }
    public String lastInsertDatetime(){
        String lastDate = null;
        db = dbh.getReadableDatabase();
        try {
            Cursor c = db.rawQuery("select max(last_update) from "+Constante.table_docs, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                lastDate = c.getString(0);
            }
            c.close();
        }catch (Throwable e){
            e.printStackTrace();
        }
        db.close();
         return lastDate;
    }
}
