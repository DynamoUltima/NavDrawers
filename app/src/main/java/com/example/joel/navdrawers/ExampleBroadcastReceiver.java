package com.example.joel.navdrawers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ExampleBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity =intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
            if (noConnectivity){
                Toasty.warning(context, "Disconnected", Toast.LENGTH_SHORT).show();
            }else{
               // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                Toasty.success(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
