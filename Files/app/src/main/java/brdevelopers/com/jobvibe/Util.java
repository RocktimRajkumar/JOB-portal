package brdevelopers.com.jobvibe;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Util {

    private static NetworkInfo wifiInfo,mobileInfo;

    public static boolean isNetworkConnected(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        try{
            wifiInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }
        catch (Exception ex){
            Toast.makeText(context, ""+ex, Toast.LENGTH_SHORT).show();
        }
        if(wifiInfo!=null && wifiInfo.isAvailable() && wifiInfo.isConnected())
            return true;
       else if(mobileInfo!=null && mobileInfo.isAvailable() && mobileInfo.isConnected())
            return true;


        return false;
    }
}
