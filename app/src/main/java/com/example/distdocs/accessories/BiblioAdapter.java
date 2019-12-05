package com.example.distdocs.accessories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.distdocs.R;
import com.example.distdocs.activities.DocumentActivity;
import com.example.distdocs.entities.Constante;
import com.example.distdocs.entities.DocsAchetes;

import java.util.List;

public class BiblioAdapter extends ArrayAdapter<DocsAchetes> {
    Activity activity;
    int layoutResourceId;
    List<DocsAchetes> data;
    Context context;

    public BiblioAdapter(Activity activity, int layoutResourceId, List<DocsAchetes> data,Context con) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
        context = con;
    }
    class BiblioHolder {
        ImageView image;
        int docId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        BiblioHolder holder=null;
        if(row==null){
            LayoutInflater inflater=LayoutInflater.from(activity);
            row=inflater.inflate(layoutResourceId,parent,false);
            holder=new BiblioHolder();
            holder.image = (ImageView)row.findViewById(R.id.biblioImage);

            row.setTag(holder);
        }
        else{
            holder= (BiblioHolder) row.getTag();
        }
        DocsAchetes doc = data.get(position);

        Bitmap bm = doc.getBitmapCover();
        Log.e("Image Display"," after call getImage "+bm+" "+doc.getDocId());
        if(bm == null){
            bm = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.text);
        }

        holder.image.setImageBitmap(bm);
        holder.docId = doc.getDocId();

//        final String path = "/distdocs/"+"CondoLiving.pdf";
        final String path = Constante.BOOKS+"/"+holder.docId;
        final String fileName = holder.docId+"";
        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("fileName",fileName);
                intent.setClass(context, DocumentActivity.class);
                activity.startActivity(intent);
            }
        });

        return row;
    }
}
