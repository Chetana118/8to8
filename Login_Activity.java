package com.globalwebsoft.a8to8;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.globalwebsoft.a8to8.Other.MyProgressDialog;
import com.globalwebsoft.a8to8.Other.Utills;
import com.globalwebsoft.a8to8.SharedPreference.Datastorage;
import com.globalwebsoft.a8to8.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIJN on 16-Apr-18.
 */

public class Login_Activity extends AppCompatActivity {

    EditText login_user_name, login_password;
    TextView login;
    String name, pass;
    MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logon_demo_activity);


        String id = (String) Datastorage.ReadFromPreference("t_id", Datastorage.STRING_KEY, Login_Activity.this);
        if (id != null) {
            if (!id.equalsIgnoreCase("")) {
                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        login_user_name = (EditText) this.findViewById(R.id.login_user_name);
        login_password = (EditText) this.findViewById(R.id.login_password);
        login = (TextView) this.findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = (login_user_name).getText().toString().trim();
                pass = (login_password).getText().toString().trim();

                if (name.equals("")) {
                    Toast.makeText(Login_Activity.this, "Please enter user name", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals("")) {
                        Toast.makeText(Login_Activity.this, "please enter your password", Toast.LENGTH_SHORT).show();
                    } else {
                        logintoserver();
                    }
                }
            }
        });
    }

    private void logintoserver() {

        Log.d("pvnEEEE", name);
        Log.d("pvnEEEE", pass);
        //Log.d("pvnEEEE", Datastorage.WritePreference("tech_id",Login_Activity.this);

        myProgressDialog = MyProgressDialog.show(Login_Activity.this, "", "", true, false, null);
        String url = Utills.BaseUrl + "WS_Login.php";

        final StringRequest login = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                myProgressDialog.dismiss();

                //Toast.makeText(Login_Activity.this, "" + response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject obj = new JSONObject(response);
                    String rsp = obj.getString("code");

                    if (rsp.equals("1") == true) {

                        Datastorage.WritePreference("t_id", obj.getString("Truck_id"), Login_Activity.this);
                        login_user_name.setText("");
                        login_password.setText("");
                        Intent i = new Intent(Login_Activity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(Login_Activity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    } else if (rsp.equals("0")) {
                        Toast.makeText(Login_Activity.this, "User_name  or password invalid", Toast.LENGTH_SHORT).show();

                    } else if (rsp.equals("3")) {
                        Toast.makeText(Login_Activity.this, "User_name and password invalid", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Login_Activity.this, "Oops something wrong", Toast.LENGTH_SHORT).show();
                    myProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Login_Activity.this, "Server Down or Network problem", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("user_name", name);
                map.put("password", pass);

                return map;
            }
        };
        login.setShouldCache(false);
        login.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(login);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Do You Want to exit Application?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        //finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}