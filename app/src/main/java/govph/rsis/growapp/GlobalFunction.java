package govph.rsis.growapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.navigation.NavigationView;

public class GlobalFunction {
    static Context mContext;
    private static final String TAG = "GlobalFunction";
    AlertDialog.Builder builder;
    GlobalFunction(Context context){
        this.mContext = context;
    }


    public void isOnline() {

        try {
            ConnectivityManager connMngr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connMngr.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        DecVar.isNetworkConnected = true;
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        DecVar.isNetworkConnected = false;
                    }
                });
            }else{
                Network[] networks = connMngr.getAllNetworks();
                for (Network network : networks){
                    NetworkInfo networkInfo = connMngr.getNetworkInfo(network);
                    if(networkInfo != null && networkInfo.isConnected()) DecVar.isNetworkConnected = true;
                }
            }

        }catch (Exception e){
            DecVar.isNetworkConnected = false;
        }
    }

    /*public boolean openDialog(AlertDialog.Builder builder){

    }*/
    /*public void updateNavView(NavigationView navView, int resId, String count){
        MenuItem item = navView.getMenu().findItem(resId); //ex. R.id.nav_item_friends
        //MenuItemCompat.setActionView(item, R.layout.badge);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView tv = (TextView) notifCount.findViewById(R.id.textMenuItemCount);

        if (count != null) {
            tv.setText(count);
        }else{
            tv.setText("");
            item.setEnabled(false);
        }
    }*/


}
