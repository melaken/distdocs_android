package com.example.distdocs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.DocsAchetes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DocumentDao {
    DataBaseHandler dbh;
    SQLiteDatabase db;
    private static final String TAG = "Download Task";


    public DocumentDao(Context ctx) {
        dbh = new DataBaseHandler(ctx, Constante.nom_base,null,1);
    }
    public void open(){
        db = dbh.getWritableDatabase();
    }

    public void close(){
        db.close();
    }
    public void updateDbWithDocs(List<DocsAchetes> liste){
        for(DocsAchetes doc : liste)
            addDocument(doc);
    }
    public void addDocument(DocsAchetes doc){
        ContentValues vals = new ContentValues();
        System.out.println("stream_doc "+doc.getDocument().toString());
        vals.put("doc_id",doc.getDocId());
        vals.put("premiere_couverture",doc.getPremiere_couverture());
        vals.put("last_update",doc.getLastUpdate().toString());
//        vals.put("cover",doc.getCover());
//        vals.put("document",doc.getDocument());
        try {
            System.out.println("try in addDoc ");
            storeStream(doc.getCover(), Constante.COVER, doc.getPremiere_couverture());
            storeStream(doc.getDocument(), Constante.BOOKS, doc.getDocId() + "");
            db = dbh.getWritableDatabase();
            long res = db.insert(Constante.table_docs, null, vals);
            db.close();
        }catch(Throwable e){
            e.printStackTrace();
        }
    }
    private void storeStream(InputStream is,String dir,String fileName) throws IOException {

        File apkStorage =  new File(Environment.getExternalStorageDirectory() +dir);
        if (!apkStorage.exists()) {
            apkStorage.mkdir();
            Log.e(TAG, "Directory Created.");
        }
        System.out.println("path" +apkStorage.getAbsoluteFile());

        File outputFile = new File(apkStorage ,fileName);
        //Create New File if not present
        if (!outputFile.exists()) {
            apkStorage.createNewFile();
            Log.e(TAG, "file Created");
            System.out.println("in if storeStream");
        }

        //écritures des fichiers sur le disque
        FileOutputStream fos = new FileOutputStream(outputFile);
        System.out.println("after if storeStream");
        byte[] buffer = new byte[1024];//Set buffer type
        int len1 = 0;//init length
        while ((len1 = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len1);//Write new file
        }
        //Close all connection after doing task
        fos.close();
        is.close();

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
    public DocsAchetes lastInsertDoc(){
        DocsAchetes doc = new DocsAchetes();
        db = dbh.getReadableDatabase();
        try {
            Cursor c = db.rawQuery("select doc_id, premiere_couverture, cover, document from "+Constante.table_docs, null);
            if (c.getCount() > 0) {
                c.moveToLast();
                doc.setDocId( c.getInt(0));
                doc.setPremiere_couverture(c.getString(1));
//                doc.setCover(c.getBlob(2));
//                doc.setDocument(c.getBlob(3));

            }
            c.close();
        }catch (Throwable e){
            e.printStackTrace();
        }
        db.close();
        System.out.println("lastInsertDoc id "+doc.getDocId());
        return doc;
    }

    public List<DocsAchetes> listAllDocs() {
        db = dbh.getReadableDatabase();
        List<DocsAchetes> liste = new ArrayList<>();
        try {
            Cursor c = db.rawQuery("select doc_id, premiere_couverture, last_update from " + Constante.table_docs, null);
            while(c.moveToNext()){
                DocsAchetes doc = new DocsAchetes();
                doc.setPremiere_couverture(c.getString(0));
                doc.setDocId(c.getInt(1));
                liste.add(doc);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        db.close();
        return liste;
    }
}
