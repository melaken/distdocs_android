package com.example.distdocs.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.distdocs.R;
import com.example.distdocs.accessories.Constante;
import com.example.distdocs.accessories.MethodesAccessoires;
import com.example.distdocs.accessories.ResponseCallback;
import com.example.distdocs.accessories.Startup;
import com.example.distdocs.dao.TransactionDao;
import com.example.distdocs.entities.Document;
import com.example.distdocs.entities.Etat;
import com.example.distdocs.entities.Transaction;
import com.example.distdocs.entities.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class PaiementActivity extends AppCompatActivity {
    private TextView montant;
    private Button button_valider;
   EditText _numTel;
    private Context context;
    Utilisateur user;
    private String numTel;
    private ProgressDialog progressDialog;
    private TransactionDao transDao;
    private boolean success = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        transDao = new TransactionDao(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending request");

        MethodesAccessoires.notification(this);

        setContentView(R.layout.payer_layout);
        user = Startup.getUser(this);

        montant = findViewById(R.id.montant);
        _numTel = findViewById(R.id.numtel);

        montant.setText(Startup.totalPanier()+" FCFA");

        button_valider = findViewById(R.id.button_valider);
        button_valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                payer();
            }
        });

    }

    private void payer(){
        numTel = _numTel.getText().toString();
        if(validate()){
            progressDialog.show();
            buyDocs(new ResponseCallback() {
                @Override
                public void onLoginSuccess(Object result) {
                    if(success){
                        Startup.panier = new ArrayList<>();
                        Intent intent = new Intent();
                        intent.setClass(context,FetchDocsActivity.class);
                        startActivity(intent);
                        Toast.makeText(context,"Transaction initiée avec succèss !!!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,"Erreur lors de la transaction",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onLoginError(Object result) {
                    progressDialog.dismiss();
                    Toast.makeText(context,"Une erreur est survenue",Toast.LENGTH_LONG).show();
                }
            },context,user.getEmail());
        }

    }
    private boolean validate(){
        boolean valid = true;
        if(numTel.isEmpty()){
            valid = false;
            _numTel.setError("Le numéro ne peut être vide");
        }else {
            _numTel.setError(null);
        }
        return valid;
    }
    private void buyDocs(final ResponseCallback responseCallback, Context context,String email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.sendPanier,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject obj = new JSONObject(response);
                            Log.i("response ",""+response);
                            if(obj.has("status") && obj.getString("status").equals("SUCCESS")){
//                                Toast.makeText(context,obj.getString("message"),Toast.LENGTH_LONG).show();
                                Transaction trans = new Transaction();
                                trans.setReference(obj.getString("transaction_reference"));
                                trans.setEtat(Etat.INITIE.name());
                                transDao.creer(trans);

                            }else{
                                success=false;
//                                Toast.makeText(context,"Erreur lors de la transaction",Toast.LENGTH_LONG).show();
                            }
                            if (responseCallback != null) {
                                responseCallback.onLoginSuccess(response);
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Une erreur est survenue",Toast.LENGTH_LONG).show();
                            Log.e("buyDocs JSONException",e.getMessage());
                            e.printStackTrace();
                        }catch (Throwable e) {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Une erreur est survenue",Toast.LENGTH_LONG).show();
                            Log.e("buyDocs ErrorListener 0",""+e.getMessage());
                            e.printStackTrace();
                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (responseCallback != null) {
                            responseCallback.onLoginError(error);
                        }
                        Log.e("buyDocs ErrorListener",""+error.getMessage());
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("montant",Startup.totalPanier()+"");
                hashMap.put("docs",Startup.panierAsString());
                hashMap.put("numtel",numTel);
                return hashMap;
            }
            @Override
            public Request.Priority getPriority() {
                return Priority.HIGH;
            }
        };

        RequestQueue request = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constante.requestTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );


        request.add(stringRequest);
    }





}
