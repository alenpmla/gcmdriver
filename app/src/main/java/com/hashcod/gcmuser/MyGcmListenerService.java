package com.hashcod.gcmuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MyGcmListenerService extends GcmListenerService
{
    private static final String TAG = "MyGcmListenerService";

    Handler handler;
    @Override
    public void onMessageReceived(String from, final Bundle data) {

      final   String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "lat" + data.getString("lat"));
        Log.d(TAG, "lng" + data.getString("lng"));
        Log.d(TAG, "Title: " + data.getString("title"));


        if(data.getString("status").equals("accepted"))
        {

            handler.post(new Runnable() {
                public void run()
                {
                    MainActivity.progressDialog.dismiss();
                    Toast.makeText(MainActivity.context,message,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.context,DriverLocation.class);
                    startActivity(intent);

                }
            });


        }

        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                LatLng sydney = new LatLng(Double.parseDouble(data.getString("lat")), Double.parseDouble(data.getString("lng")));

if(DriverLocation.mMap!=null){
    if(DriverLocation.markerName!=null){
        DriverLocation. markerName.remove();
    }
   DriverLocation. markerName = DriverLocation.mMap.addMarker(new MarkerOptions().position(sydney).title(data.getString("message")).icon(DriverLocation.icon).rotation(Float.parseFloat(data.getString("rotation"))));

    // DriverLocation. mMap.addMarker(new MarkerOptions().position(sydney).title(data.getString("message")));
    DriverLocation. mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    DriverLocation. mMap.animateCamera( CameraUpdateFactory.zoomTo( 15f ) );
    DriverLocation. driverinfoview.setText(data.getString("title"));
}

            }
        });


    }



    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }




}
