package com.maya.pqvalidator.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.maya.pqvalidator.R;
import com.maya.pqvalidator.application.PQValidatorApplication;
import com.maya.pqvalidator.constants.Constants;

import org.json.JSONObject;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Utility
{
    public static boolean isNetworkAvailable(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected())
        {
            /* show a toast, we cannot use the internet right now */
            //showToast(activity, "network_unavailable_message", Constants.NO_INTERNET_CONNECTION, true);
            /*if(HomeScreenActivity.mHomeScreenActivity != null){
                HomeScreenActivity.mHomeScreenActivity.tvInternetStatus.setText(Constants.NO_INTERNET_CONNECTION);
            }*/
            return false;
            /* aka, do nothing */
        }
        return true;
    }


    public static boolean isValidEmail(CharSequence paramCharSequence)
    {
        if (paramCharSequence == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(paramCharSequence).matches();
    }

    public static boolean isValidLicenseNo(String paramString)
    {
        String str = paramString;
        if (paramString != null) {
            str = paramString.replaceAll(" ", "");
        }
        return str.matches("[a-zA-Z]{2,2}-[0-9]{1,2}-[a-zA-Z]{1,2}-[0-9]{1,4}");
    }

    public static boolean isValidMobile(String paramString)
    {
        return paramString.matches("[0-9]{10}");
    }

    public static void deleteSharedPreferences()
    {
        String fcmToken = "";
        if(getSharedPreferences().contains(Constants.USER_FCM_TOKEN))
        {
            fcmToken = getString(getSharedPreferences(), Constants.USER_FCM_TOKEN);
        }

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear().commit();

        if(fcmToken.length()>0)
            setString(getSharedPreferences(),Constants.USER_FCM_TOKEN,fcmToken);
    }


    public static SharedPreferences getSharedPreferences()
    {
        return PQValidatorApplication.getInstance().sharedPreferences;
    }

    public static void setString(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBoolen(SharedPreferences sharedPreferences, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setInt(SharedPreferences sharedPreferences,String key, int value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(SharedPreferences sharedPreferences,String key)
    {
        return sharedPreferences.getString(key,null);
    }

    public static boolean getBoolen(SharedPreferences sharedPreferences,String key)
    {
        return sharedPreferences.getBoolean(key,false);
    }

    public static int getInt(SharedPreferences sharedPreferences,String key)
    {
        return sharedPreferences.getInt(key,0);
    }

    public static void del(SharedPreferences sharedPreferences, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getCamelCase(String name)
    {
        if(name==null)
            return null;

        int flag=1439;
        name=name.trim();
        name=name.toLowerCase();

        while(flag==1439)
        {
            if(name.contains("  "))
            {
                name=name.replaceAll("  "," ");
            }
            else
            {
                flag=3914;
            }


        }

        StringBuilder sb = new StringBuilder();
        try{
            String[] words = name.toString().split(" ");
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString());
                }
            }
        }
        catch(Exception e){
            //System.out.println(e.toString());
            e.printStackTrace();
            return name;
        }
        return sb.toString();

    }

    public static String getShortName(String name) {
        int flag=1439;
        name=name.trim();
        name=name.toLowerCase();
        while(flag==1439)
        {

            if(name.contains("  "))
            {

                name=name.replaceAll("  "," ");

            }
            else
            {
                flag=3914;
            }


        }

        StringBuilder sb = new StringBuilder();
        try{
            String[] words = name.toString().split(" ");
            if (words[0].length() > 0)
            {
                sb.append(Character.toUpperCase(words[0].charAt(0)));
                for (int i = 1; i < words.length; i++)
                {
                    sb.append(Character.toLowerCase(words[i].charAt(0)));
                }
            }
        }
        catch(Exception e){
            //System.out.println(e.toString());
            e.printStackTrace();
            return (""+name.charAt(0)).toUpperCase();
        }

        if(sb.length()>2)
        {
            return (sb.toString()).substring(0,2);
        }
        else
        {
            return sb.toString();
        }
    }

    public static boolean isSuccessful(String s)
    {
        try {
            JSONObject jsonObject = new JSONObject(s);
            return jsonObject.getBoolean("status");
        } catch (Exception e)
        {
            return false;
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static ProgressDialog generateProgressDialog(Context activity)
    {
        try {

            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            progressDialog.setContentView(R.layout.progressdialog);
            return progressDialog;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeProgressDialog(ProgressDialog progressDialog)
    {
        try
        {
            if(progressDialog!=null&&progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void showToast(Context c, String s, boolean duration) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, s, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        tst.show();
    }
    public static void showToast(Context c, String t, String s, boolean duration) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, t, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        tst.setText(s);
        tst.show();
    }

    public static void showToast(Context c, String t, String s, boolean duration, String response) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, t, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("error")) {
                tst.setText(jsonObject.getString("error").toString());
            }
            else if(jsonObject.has("message")){
                tst.setText(jsonObject.getString("message").toString());
            }
            else {
                tst.setText(s);
            }
            tst.show();
        } catch (Exception e) {
            Logger.d("[Exception]", e.toString());
        }
    }

    public static Typeface getTypeface(int value, Context context)
    {
        String path = "fonts/WorkSansSemiBold.ttf";
        switch (value)
        {
            case 3:
                path = "fonts/WorkSansBold.ttf";
                break;
            case 2:
                path = "fonts/WorkSansSemiBold.ttf";
                break;
            case 1:
                path = "fonts/WorkSansMedium.ttf";
                break;
            case 0:
                path = "fonts/WorkSansRegular.ttf";
                break;
        }
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    public static void displayError(JSONObject jsonObject,Context context)
    {
        try {
            Utility.showToast(context,"Error",jsonObject.getString("error"),true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static String makeJSDateReadable(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("-");
            ret = new DateFormatSymbols().getMonths()[Integer.parseInt(arr[1]) - 1] + " " + arr[0] + ret;
            ret = arr[2].split("T")[0] + " " + ret;
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String makeJSDateReadableOther(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("/");
            ret = arr[1] + " " + new DateFormatSymbols().getMonths()[Integer.parseInt(arr[0]) - 1]  + ret;
            ret = ret + " " + arr[2].split(" ")[0];
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return s;
        }
    }

    public static String makeDateToAgo(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("T");

            try {
                String[] time = arr[1].replace(".", "Z").split("Z");
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss");
                Date parsedTimeStamp = dateFormat.parse(arr[0] + " " + time[0]);

                Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
                return getTimeAgo(timestamp.getTime());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return arr[0];
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Yesterday";
        }
    }







    @Nullable
    public static Bitmap urlToBitMap(String urlLink){
        try {
            URL url = new URL(urlLink);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getTimeAgo(long time)
    {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < Constants.MINUTE_MILLIS)
        {
            return "just now";
        }
        else if (diff < 2 * Constants.MINUTE_MILLIS)
        {
            return "a minute ago";
        }
        else if (diff < 50 * Constants.MINUTE_MILLIS)
        {
            return diff / Constants.MINUTE_MILLIS + " minutes ago";
        }
        else if (diff < 90 * Constants.MINUTE_MILLIS) {
            return "an hour ago";
        }
        else if (diff < 24 * Constants.HOUR_MILLIS) {
            return diff / Constants.HOUR_MILLIS + " hours ago";
        }
        else if (diff < 48 * Constants.HOUR_MILLIS) {
            return "yesterday";
        }
        else
        {
            return diff / Constants.DAY_MILLIS + " days ago";
        }
    }






    public static void showSnackBar(Context activity, CoordinatorLayout coordinatorLayout, String message, int type)
    {
        if(coordinatorLayout==null|| message==null || activity==null)
        {
            return;
        }
        Snackbar snackBar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        TextView txtMessage = (TextView) snackBar.getView().findViewById(R.id.snackbar_text);
        txtMessage.setTextColor(ContextCompat.getColor(activity,R.color.white));
        if (type==2)
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.black));
        else if(type==1)
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity,R.color.app_snack_bar_true));
        else
        {
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity,R.color.mainColorPrimary));
        }
        txtMessage.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/medium.ttf"));

        snackBar.show();
    }


    public static int dpSize(Context context, int sizeInDp)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

    public static HashMap<String,Integer> generateRequestCodes()
    {
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("REQUEST_CAMERA",18000);
        return hashMap;
    }
}
