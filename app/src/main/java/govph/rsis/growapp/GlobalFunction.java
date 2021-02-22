package govph.rsis.growapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class GlobalFunction {
    static Context mContext;
    private static final String TAG = "GlobalFunction";
    AlertDialog.Builder builder;
    GlobalFunction(Context context){
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void isOnline() {

        try {
            ConnectivityManager connMngr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

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

        }catch (Exception e){
            DecVar.isNetworkConnected = false;
        }
    }

    /*public boolean openDialog(AlertDialog.Builder builder){

    }*/


}
