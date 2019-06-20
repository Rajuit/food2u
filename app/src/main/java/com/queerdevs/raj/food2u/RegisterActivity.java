package com.queerdevs.raj.food2u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {
    EditText name, email, phone, pass1, pass2;
    Button registerBut;
    TextView signin;
    String EtName;
    String EtEmail;
    String EtPass1;
    String EtPass2;
    String EtPhoneno;
    boolean valid = true;

    String URLSignup = "http://192.168.1.26:8080/Food2U/food2u/firstwebapp/signup";
//   String URLSignup = "http://192.168.0.101:8080/Food2U/food2u/firstwebapp/signup";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);


        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email_reg);
        phone = (EditText) findViewById(R.id.phone_reg);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        registerBut = (Button) findViewById(R.id.registerbut);
        signin = (TextView) findViewById(R.id.signin);
        //  signin = (Button) findViewById(R.id.signin);

        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerMe()) {

                  //  Toast.makeText(getBaseContext(), "you have successfully created account", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(RegisterActivity.this, "Sorry Dude !!! unable to create account", Toast.LENGTH_LONG).show();


                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }


        });




    }




    public boolean registerMe() {
        EtName = name.getText().toString();
        EtEmail = email.getText().toString();
        EtPhoneno = phone.getText().toString();
        EtPass1 = pass1.getText().toString();
        EtPass2 = pass2.getText().toString();

        boolean valid = isValidEmailAddress(EtEmail);
       // Toast.makeText(this, String.valueOf(valid), Toast.LENGTH_LONG).show();*/

        if (!(EtName.isEmpty() || EtEmail.isEmpty() || EtPhoneno.isEmpty() || EtPass1.isEmpty() || EtPass2.isEmpty())) {
            if (EtName.isEmpty() || EtName.length() < 3) {
                name.setError("at least 3 characters");
                valid = false;
            }


            if (isValidEmailAddress(EtEmail)) {
               // email.setError("enter a valid email address");
                //sendPostRequest(EtName, EtEmail, EtPass1, EtPhoneno);

                //valid = false;


                if (EtPass1.equals(EtPass2)) {
                    sendPostRequest(EtName, EtEmail, EtPass1, EtPhoneno);

                } else {
                    Toast.makeText(this, "Password doesn't Match", Toast.LENGTH_LONG).show();
                    pass1.setText("");
                    pass2.setText("");
                }

            } else {
                Toast.makeText(this, " !! Enter a valid email address !!", Toast.LENGTH_LONG).show();
                email.setText("");
            }


        } else{
            Toast.makeText(this, " !! It seems you forget to fill Some Details !!", Toast.LENGTH_LONG).show();
             }
        return valid;
    }

    private void sendPostRequest(String etName, String etEmail, String etPass1, String etPhoneno) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", etName);
            jsonObject.put("email", etEmail);
            jsonObject.put("phone", etPhoneno);
            jsonObject.put("password", etPass1);

            new MySignUp().execute(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static boolean isValidEmailAddress(String etEmail) {
       /* boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }

        Log.d("RESULT", String.valueOf(result));

        return result;*/



        if(etEmail.endsWith("@gmail.com")){

            return true;
        }
        return false;
    }

    class MySignUp extends AsyncTask<JSONObject, Void, String> {


        @Override
        protected String doInBackground(JSONObject... params) {

            JSONObject jsonObject = params[0];

            try {

                Log.d("CHECKING", "SUCCESSFUL");

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(BaseApplication.JSON, jsonObject.toString());

                Request request = new Request.Builder().url(URLSignup).post(body).build();

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("CHECKING", s);
            if(s.equals("success")){

                /*final ProgressDialog progressDialog= new ProgressDialog(RegisterActivity.this,R.style.Theme_AppCompat);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();*/

                Toast.makeText(RegisterActivity.this,"Welcome to food2u !!!",Toast.LENGTH_LONG).show();
                Intent intent2=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent2);
                finish();

            }else{
                Toast.makeText(RegisterActivity.this,"Email id is already registered",Toast.LENGTH_LONG).show();
            }

        }
    }


}


