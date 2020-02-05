package com.example.nasagery_application.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Status{

    public Status(){

    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


}
