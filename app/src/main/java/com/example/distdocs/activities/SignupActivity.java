package com.example.distdocs.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.distdocs.accessories.ResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)  EditText _nameText;
    @BindView(R.id.input_firstName)  EditText _prenomText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_password_conf) EditText _passwordConf;
    @BindView(R.id.btn_signup)  Button _signupButton;
    @BindView(R.id.link_login)TextView _loginLink;
    private String name ;
    private String prenoms ;
    private String email;
    private String password;
    private String password_conf;
    private Object success;
    private String emailMessage;
    private boolean ifError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Toolbar toolbar = findViewById(R.id.topToolbarsignup);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

         name = _nameText.getText().toString();
         prenoms = _prenomText.getText().toString();
         email = _emailText.getText().toString();
         password = _passwordText.getText().toString();
         password_conf = _passwordConf.getText().toString();
        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        createAccount(new ResponseCallback() {
            @Override
            public void onLoginSuccess(Object result) {
                progressDialog.dismiss();
                System.out.println("success "+success);
                if(success != null && (boolean)success){
                    onSignupSuccess();
                }else{
                    _emailText.setError(emailMessage);
                    onSignupFailed();
                }
            }

            @Override
            public void onLoginError(Object result) {
                progressDialog.dismiss();
                onSignupFailed();
            }
        },this);
    }


    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Compte créé avec succès : vous pouvez vous connecter", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Échec Création", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Au moins 3 caractères");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(Constante.EMAIL_NOT_VALID);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(Constante.PASS_NOT_VALID);
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (password_conf.isEmpty() || !password_conf.equals(password)) {
            _passwordConf.setError("Mot de passe et confirmation différents");
            Log.i("Password : "+password,"Conf : "+password_conf);
            System.out.println("test "+password_conf == password);
            valid = false;
        } else {
            _passwordConf.setError(null);
        }

        return valid;
    }
    private void createAccount(final ResponseCallback responseCallback, Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constante.PROTOCOLE+Constante.SERVER+Constante.PORT+Constante.app_name+ Constante.createAccount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                             success = obj.get("success");
                             if(obj.has("email"))
                                emailMessage = obj.getString("email");
                             if(obj.has("error"))
                                ifError = obj.getBoolean("error");

                            if (responseCallback != null) {
                                responseCallback.onLoginSuccess(response);
                            }

                        } catch (JSONException e) {
                            System.out.println("JSONException");
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
                        System.out.println("ErrorListener");
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("noms",name);
                hashMap.put("prenoms",prenoms);
                hashMap.put("password",password);
                hashMap.put("email",email);
                return hashMap;
            }
            @Override
            public Request.Priority getPriority() {
                return Priority.HIGH;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constante.requestTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        RequestQueue request = Volley.newRequestQueue(context);
        request.add(stringRequest);
    }
}
