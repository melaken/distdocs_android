package com.example.distdocs.accessories;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.distdocs.R;
import com.example.distdocs.entities.Document;

import java.util.List;

public class PanierAdapter extends ArrayAdapter<Document> {

    Activity activity;
    int layoutResourceId;
    List<Document> data;
    public PanierAdapter(Activity activity, int layoutResourceId, List<Document> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;

    }
    private void update(){
        this.notifyDataSetChanged();
    }
    class PanierHolder
    {
        TextView prix;
        long docId;
        ImageView image;
//        ImageView imageDelete;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        PanierHolder holder=null;

        if(row==null)
        {
            LayoutInflater inflater=LayoutInflater.from(activity);
            row=inflater.inflate(layoutResourceId,parent,false);
            holder=new PanierHolder();
            holder.prix= (TextView) row.findViewById(R.id.prixRecap);
            holder.image = (ImageView)row.findViewById(R.id.docImage);
//            holder.imageDelete = (ImageView)row.findViewById(R.id.remove_doc);

            row.setTag(holder);
        }
        else
        {
            holder= (PanierHolder) row.getTag();
        }
        Document doc = data.get(position);
        Log.e("Doc_id "+doc.getId(),"Prix "+doc.getPrix());

        Bitmap bm = doc.getImage();
        Log.e("Image Display"," after call getImage "+bm+" "+doc.getId());
//        if(bm != null){
        holder.prix.setText(doc.getPrix()+"  FCFA");

        final long docId =doc.getId();
        holder.docId = docId;
//        }
        if(bm == null){
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.library_books);
//            holder.prix.setText(null);
        }

        holder.image.setImageBitmap(bm);
//        holder.imageDelete.setImageResource(R.drawable.delete);
        ImageView imageDelete = (ImageView)row.findViewById(R.id.remove_doc);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Doc Id",""+docId);

                    Startup.removeDocToShoppingCart(docId);
               TextView totalRecap = activity.findViewById(R.id.totalRecap);
                totalRecap.setText(Startup.totalPanier()+   "   FCFA");
                    update();
            }
        });

        return row;
    }
}
