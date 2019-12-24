package com.example.distdocs.accessories;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.distdocs.R;
import com.example.distdocs.activities.FetchDocsRequest;
import com.example.distdocs.activities.MainActivity;
import com.example.distdocs.activities.NavigationActivity;
import com.example.distdocs.activities.PanierActivity;

public class Listeners implements View.OnClickListener {
   Activity activity;
    final Intent intent = new Intent();

    public Listeners(Activity activity){
        this.activity = activity;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.home: ImageView homeImage = activity.findViewById(R.id.home);
                            TextView homeTitle = activity.findViewById(R.id.home_tittle);
                            homeImage.setColorFilter(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorTextbottomTool));
                            homeTitle.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorTextbottomTool));
                            intent.setClass(activity.getApplicationContext(), MainActivity.class);
                            activity.startActivity(intent);
                            break;
            case R.id.library:  ImageView libraryImage = activity.findViewById(R.id.library);
                                TextView libraryTitle = activity.findViewById(R.id.library_tittle);
                                libraryImage.setColorFilter(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorTextbottomTool));
                                libraryTitle.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorTextbottomTool));
                                intent.setClass(activity.getApplicationContext(), FetchDocsRequest.class);
                                activity.startActivity(intent);
                                break;
            case R.id.shopping: ImageView shoppingImage = activity.findViewById(R.id.shopping);
                                TextView shoppingTitle = activity.findViewById(R.id.shopping_tittle);
                                shoppingImage.setColorFilter(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorTextbottomTool));
                                shoppingTitle.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorTextbottomTool));
                                intent.setClass(activity.getBaseContext(), PanierActivity.class);
                                activity.startActivity(intent);
                                break;
        }
    }

}
