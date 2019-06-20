package com.queerdevs.raj.food2u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SigninActivity extends AppCompatActivity {

    Button Login;
    EditText email;
    EditText pass;
    TextView register;
    boolean isRem = false;
    CheckBox remChe;
    boolean some;

    String etEmail;
    String etPass;


    boolean valid = false;
    SharedPreferences sharedPref;


   // String URLLogIn = "http://192.168.0.101:8080/Food2U/food2u/firstwebapponline/signin";
   String URLLogIn = "http://192.168.1.26:8080/Food2U/food2u/firstwebapponline/signin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_signin);
        email = (EditText) findViewById(R.id.email_log);
        pass = (EditText) findViewById(R.id.passwd);
        Login = (Button) findViewById(R.id.LoginBut);
        register = (TextView) findViewById(R.id.NotRegTV);


        sharedPref = getSharedPreferences("com.sid.hello", Context.MODE_PRIVATE);
        isRem = sharedPref.getBoolean("SAVE", false);
        Log.d("VALUE", String.valueOf(isRem));

        if (isRem) {

            if (isNetworkAvailable()) {

                try {
                    String es = sharedPref.getString("EMAIL", "a");
                    String ep = sharedPref.getString("PASS", "a");

                    Log.d("VALUE", es + ep);

                    email.setText(es);
                    pass.setText(ep);
                    sendPostLogIn(es, ep);
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_LONG).show();
            }

        }


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent1=new Intent(SigninActivity.this,MainActivity.class);
//                startActivity(intent1);
                loginMe();
                  /* final ProgressDialog progressDialog= new ProgressDialog(SigninActivity.this,R.style.Theme_AppCompat);
                   progressDialog.setIndeterminate(true);
                   progressDialog.setMessage("Authenticating...");
                   progressDialog.show();*/


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SigninActivity.this, RegisterActivity.class);
                startActivity(intent2);
                finish();
            }
        });


        remChe = (CheckBox) findViewById(R.id.rememberChe);
        remChe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                // TODO Auto-generated method stub
                some = arg1;
            }
        });


    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public boolean loginMe() {
        etEmail = email.getText().toString();
        etPass = pass.getText().toString();


        if (!(etEmail.isEmpty() || etPass.isEmpty())) {

            if (etEmail.isEmpty()) {
                email.setError("");
                valid = false;
            }

            if (etPass.isEmpty()) {
                pass.setError("");
                valid = false;
            }
            sendPostLogIn(etEmail, etPass);

        } else {

            Toast.makeText(this, "please fill id and password", Toast.LENGTH_LONG).show();
        }


        return valid;

    }


    private void sendPostLogIn(String etEmail, String etPass) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", etEmail);
            jsonObject.put("password", etPass);

            new MyLogIn().execute(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class MyLogIn extends AsyncTask<JSONObject, Void, String> {


        @Override
        protected String doInBackground(JSONObject... params) {

            JSONObject jsonObject = params[0];

            try {

                Log.d("VALUE", "signining.......");

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(BaseApplication.JSON, jsonObject.toString());

                Request request = new Request.Builder().url(URLLogIn).post(body).build();

                Response response = client.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String logi) {
            super.onPostExecute(logi);

            Log.d("VALUE ", logi + String.valueOf(isRem));

            if (logi.equals("valid") && !isRem && some) {

                try {

                    SharedPreferences.Editor ed = sharedPref.edit();
                    ed.putString("EMAIL", etEmail);
                    ed.putString("PASS", etPass);
                    ed.putBoolean("SAVE", true);
                    ed.commit();

                    Intent intent1 = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (logi.equals("valid") && isRem) {

                Intent intent1 = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();

            } else {

                Toast.makeText(SigninActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            }

        }
    }

}

