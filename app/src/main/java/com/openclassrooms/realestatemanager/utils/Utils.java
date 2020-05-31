package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.openclassrooms.realestatemanager.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils  {

    private static final DateFormat RIGHT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

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

    /**
     * This method calculates the mensuality depending on several factors
     * @param amount is the amount of money that user wants to borrow
     * @param rate is the rate of the loan
     * @param year is the number of years required by the user to reimburse the loan
     * @return the mensuality that will have to be paid
     */
    public static int calculateMensuality(int amount, double rate, int year) {
        double mensuality = 0;
        if (amount != 0) {
            double percentRate = rate/100;
            double month = year * 12;
            double mensualRate = percentRate / 12;
            double numerator = amount * mensualRate;
            double denominator = 1 - (Math.pow(1 + mensualRate, -month));
            mensuality = numerator / denominator;
        }
        return (int)mensuality;
    }

    /**
     * Allows to get data to correctly populate the spinner whatever the language
     * @param context provides a context to the method
     * @return an array of String for flattype spinner which matches the smartphone language
     */
    public static String[] createDataForFlatTypeSpinners(Context context) {
        String[] origin = context.getResources().getStringArray(R.array.flat_type);
        String[] finalData = new String[6];
        for(int i = 0; i < origin.length; i++) {
            int resId = context.getResources().getIdentifier(origin[i], "string", "com.openclassrooms.realestatemanager");
            finalData[i] = context.getString(resId);
        }
        return finalData;
    }

    /**
     * Method which allows to get the flattype string from the saved spinner position whatever the language
     * @param context provides a context to the method
     * @param position is the saved position (of the item in the spinner)
     * @return the flattype String which matches the smartphone language
     */
    public static String getStringFromFlatTypeSpinners(Context context, int position) {
        String[] origin = context.getResources().getStringArray(R.array.flat_type);
        int resId = context.getResources().getIdentifier(origin[position], "string", "com.openclassrooms.realestatemanager");
        String finalData = context.getString(resId);
        return finalData;
    }

//    /**
//     * This method is responsible for solving the rotation issue if exist. Also scale the images to
//     * 1024x1024 resolution
//     *
//     * @param context       The current context
//     * @param selectedImage The Image URI
//     * @return Bitmap image results
//     * @throws IOException
//     */
//    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
//            throws IOException {
//        int MAX_HEIGHT = 1024;
//        int MAX_WIDTH = 1024;
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
//        BitmapFactory.decodeStream(imageStream, null, options);
//        imageStream.close();
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        imageStream = context.getContentResolver().openInputStream(selectedImage);
//        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);
//
//        img = rotateImageIfRequired(context, img, selectedImage);
//        return img;
//    }
//
//    /**
//     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
//     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
//     * the closest inSampleSize that will result in the final decoded bitmap having a width and
//     * height equal to or larger than the requested width and height. This implementation does not
//     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
//     * results in a larger bitmap which isn't as useful for caching purposes.
//     *
//     * @param options   An options object with out* params already populated (run through a decode*
//     *                  method with inJustDecodeBounds==true
//     * @param reqWidth  The requested width of the resulting bitmap
//     * @param reqHeight The requested height of the resulting bitmap
//     * @return The value to be used for inSampleSize
//     */
//    private static int calculateInSampleSize(BitmapFactory.Options options,
//                                             int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            // Calculate ratios of height and width to requested height and width
//            final int heightRatio = Math.round((float) height / (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//
//            final float totalPixels = width * height;
//
//            // Anything more than 2x the requested pixels we'll sample down further
//            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
//
//            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
//                inSampleSize++;
//            }
//        }
//        return inSampleSize;
//    }
//
//    /**
//     * Rotate an image if required.
//     *
//     * @param img The image bitmap
//     * @param selectedImage Image URI
//     * @return The resulted Bitmap after manipulation
//     */
//    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
//
//        InputStream input = context.getContentResolver().openInputStream(selectedImage);
//        ExifInterface ei;
//        if (Build.VERSION.SDK_INT > 23)
//            ei = new ExifInterface(input);
//        else
//            ei = new ExifInterface(selectedImage.getPath());
//
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotateImage(img, 90);
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotateImage(img, 180);
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotateImage(img, 270);
//            default:
//                return img;
//        }
//    }
//
//    /**
//     * Rotate a bitmap image
//     * @param img is the bitmap image to rotate
//     * @param degree is the angle used to rotate the image
//     * @return a rotated bitmap image
//     */
//    private static Bitmap rotateImage(Bitmap img, int degree) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
//        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
//        img.recycle();
//        return rotatedImg;
//    }

}
