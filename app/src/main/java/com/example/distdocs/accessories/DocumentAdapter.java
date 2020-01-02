package com.example.distdocs.accessories;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;


import com.example.distdocs.R;
import com.example.distdocs.activities.DocumentActivity;
import com.example.distdocs.dao.UtilisateurDao;
import com.example.distdocs.entities.Document;
import com.example.distdocs.entities.Utilisateur;

import java.util.List;
public class DocumentAdapter extends ArrayAdapter<Document> {
    Activity activity;
    int layoutResourceId;
    List<Document> data;
    //    Document doc;

    public DocumentAdapter(Activity activity, int layoutResourceId, List<Document> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;

    }
    class DocumentHolder
    {
        TextView prix;
        TextView docId;
        ImageView image;
        TextView date;
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
            holder.image.setEnabled(false);
            holder.date = (TextView)row.findViewById(R.id.textViewDate);
            holder.docId = (TextView)row.findViewById(R.id.textViewDocId);

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
            holder.date.setText(doc.getDateParution()+"");
        holder.docId.setText(doc.getId()+"");
        final long docId =doc.getId();
//        }
        if(bm == null){
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.library_books);
//            holder.prix.setText(null);
        }

        holder.image.setImageBitmap(bm);

        ImageView addToShoppingCart = (ImageView)row.findViewById(R.id.addToShoppingCart);
        addToShoppingCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Doc Id",""+docId);
                UtilisateurDao dao =  new UtilisateurDao(activity.getApplicationContext());
                Utilisateur user =  dao.selectUser();
                if(user != null) {

                    if (Startup.ifPanierContainsDoc(docId))
                        Toast.makeText(activity.getBaseContext(), "Document deja dans le panier", Toast.LENGTH_SHORT).show();
                    else {
                        Startup.addDocToShoppingCart(docId);
                        Toast.makeText(activity.getApplicationContext(), Startup.panier.size() + " articles dans le panier", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity.getApplicationContext(),"Veuillez vous connecter avant de procéder aux achats",Toast.LENGTH_LONG).show();
                }
            }
        });

        return row;
    }
}
