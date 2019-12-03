package com.example.distdocs.entities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.distdocs.R;

import java.util.List;
public class DocumentAdapter extends ArrayAdapter<Document> {
    Activity activity;
    int layoutResourceId;
    List<Document> data;
    //    Document doc;
    int nb = 0;

    public DocumentAdapter(Activity activity, int layoutResourceId, List<Document> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
    }
    class DocumentHolder
    {
        TextView prix;
        ImageView image;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        DocumentHolder holder=null;
        if(row==null)
        {
            LayoutInflater inflater=LayoutInflater.from(activity);
            row=inflater.inflate(layoutResourceId,parent,false);
            holder=new DocumentHolder();
            holder.prix= (TextView) row.findViewById(R.id.textViewPrix);
            holder.image = (ImageView)row.findViewById(R.id.imageView);

            row.setTag(holder);

        }
        else
        {
            holder= (DocumentHolder) row.getTag();
        }
        Document doc = data.get(position);

        Bitmap bm = doc.getImage();
        Log.e("Image Display"," after call getImage "+bm+" "+doc.getId());
//        if(bm != null){
            holder.prix.setText(doc.getPrix()+"  FCFA");
//        }
        if(bm == null){
            nb++;
            bm = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.text);
//            holder.prix.setText(null);
        }

        holder.image.setImageBitmap(bm);
        Log.e("After setImage","nb erreurs "+nb);

        return row;
    }
}
