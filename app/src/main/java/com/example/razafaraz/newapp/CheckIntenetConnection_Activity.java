/*package com.example.razafaraz.newapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class CheckIntenetConnection_Activity {
    public static boolean isConnected(Context context){
        boolean found=false;
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network=manager.getActiveNetworkInfo();
        if (network!=null && network.isAvailable() && network.isConnected())
            found=true;
        return found;
    }
}
*/