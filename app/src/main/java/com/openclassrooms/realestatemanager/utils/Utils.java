package com.openclassrooms.realestatemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Price conversion (Euros to Dollars)
     * @param dollars amount to convert
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Price conversion (Dollars to Euros)
     * @param euros amount to convert
     * @return
     */
    public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros * 1.232);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        Calendar thisDay = Calendar.getInstance();
        long today = thisDay.getTimeInMillis();

        return dateFormat.format(today);
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isAvailable();
    }

    public static String buildAddress(Integer streetNb, String street, Integer postCode, String city) {
        String address = "";
        if (streetNb != null && street != null) address = String.valueOf(streetNb) + ", " + street;
        if (streetNb == null && street != null) address = street;
        if (postCode != null && address != null) address = address + "\n" + postCode + " " + city;
        else if (postCode == null && address != null) address = address + "\n" + city;
        else address = city;
        return address;
    }

}
