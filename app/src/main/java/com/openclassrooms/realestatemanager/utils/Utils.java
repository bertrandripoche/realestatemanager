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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private static final DateFormat RIGHT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Capitalize the first letter of a String a set the rest to lower case
     * @param original is the string on which we operate
     * @return a String with a capital as a first letter
     */
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    /**
     * Capitalize the first letter of a String a set the rest to lower case
     * @param original is the string on which we operate
     * @return a String with a capital as a first letter
     */
    public static String capitalizeFirstLetterOfASingleWord(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

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
        Calendar thisDay = Calendar.getInstance();
        long today = thisDay.getTimeInMillis();

        return RIGHT_DATE_FORMAT.format(today);
    }

    /**
     * Method to check that input date is correct
     * @param date is a String representing a date to be tested
     * @return true if the String represents a valid Date
     */
    public static boolean isCorrectDate(String date) {
        RIGHT_DATE_FORMAT.setLenient(false);
        if (!date.equals(""))  {
            try {
                RIGHT_DATE_FORMAT.parse(date);
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method compares two dates
     * @param dateString1 date to compare to the reference date
     * @param dateString2 reference date
     * @return true if dateString1 is before or the same date as the reference date
     */
    public static boolean isBeforeOrEqualTo(String dateString1, String dateString2) {
        Date date1;
        Date date2;
        try {
            date1 = RIGHT_DATE_FORMAT.parse(dateString1);
            date2 = RIGHT_DATE_FORMAT.parse(dateString2);
            if (date1.compareTo(date2) > 0) return false;
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
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

    /**
     * This method allows to build a readable address
     * @param streetNb is the street number
     * @param street is the name of the street
     * @param postCode is the post code of the city
     * @param city is the name of the city
     * @return a String which is the address of the flat in a correct format
     */
    public static String buildAddress(Integer streetNb, String street, Integer postCode, String city) {
        String address = "";
        if (streetNb != null && street != null) address = String.valueOf(streetNb) + ", " + street;
        if (streetNb == null && street != null) address = street;
        if (postCode != null && address != null) address = address + "\n" + postCode + " " + city;
        else if (postCode == null && address != null) address = address + "\n" + city;
        else address = city;
        return address;
    }

    public static int calculateMensuality(double amount, double rate, double year) {
        double mensuality = 0;
        if (amount != 0) {
            double percentRate = rate/100;
            double month = year * 12;
            double mensualRate = percentRate / 12;
            double numerator = amount * mensualRate;
            double forDenominator = 1 + mensualRate;
            double power = -month;
            double denominator = 1 - (Math.pow(forDenominator, power));
            mensuality = numerator / denominator;
        }
        return (int)mensuality;
    }

}
