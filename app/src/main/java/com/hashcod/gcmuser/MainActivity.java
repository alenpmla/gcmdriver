package com.hashcod.gcmuser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private BroadcastReceiver mRegistrationBroadcastReceiver;

   public static Context context;

    public static  ProgressDialog progressDialog;

    String drivergcmcode="f5eUz4ADat0:APA91bHUp3OTXGzVF8xK8E9o79FvYFL9sBvMVv28SZKeY5apeu9rJl27wD_uJLdLXZM1eSMbq7aBtB6KhZPvUZTWCJ5P4wb9m1388Feu_qHXcH__098BqiUQsFO_3uMjLLib2rRkigJG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button connect_button=(Button)findViewById(R.id.connect_button);
        connect_button.setOnClickListener(this);
        context=getApplicationContext();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Connecting to driver");

        if(isnetworkavailable()){
            gcmregister();
        }
else {
            Log.d("Log","Network Error!");
        }



        //  mInformationTextView = (TextView) findViewById(R.id.informationTextView);





    }

    private void gcmregister() {
        this.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        this.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));
        //  mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //  mRegistrationProgressBar.setVisibility(ProgressBar.GONE);

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                    //  Toast.makeText(getApplicationContext(),getString(R.string.gcm_send_message),Toast.LENGTH_SHORT).show();
                    System.out.println(getString(R.string.gcm_send_message));
                    //  mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //  Toast.makeText(getApplicationContext(),getString(R.string.token_error_message),Toast.LENGTH_SHORT).show();
                    System.out.println(getString(R.string.token_error_message));
                    //  mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {



            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.i("Googleplayservices", "This device is not supported.");

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                // GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                //     PLAY_SERVICES_RESOLUTION_REQUEST).show();

                //    showalertgps();
            } else
            {

                Log.i("Googleplayservices", "This device is not supported.");
                finish();

            }
            return false;
        }
        return true;
    }


    private boolean isnetworkavailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.connect_button:
                connect_fun();


        }

    }

    public void connect_fun()

    {

        progressDialog.show();
        Log.d("Log","Clicked");




        String tag_json_obj = "json_obj_req";

        String url = "https://demo.5centscdn.com/Alen/gcmtest.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(getApplicationContext(),"Error in connection!",Toast.LENGTH_SHORT).show();
                if (volleyError instanceof TimeoutError) {

                }
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> postparams = new HashMap<>();
                SharedPreferences sharedPreferences=getSharedPreferences("App_Data",MODE_PRIVATE);
                postparams.put("gcmid", sharedPreferences.getString("gcmid",""));
                postparams.put("type", "sendreq");
                return postparams;
            }

            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);







    }
}
