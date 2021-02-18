package govph.rsis.growapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class GlobalFunction {
    static Context mContext;
    AlertDialog.Builder builder;
    GlobalFunction(Context context){
        this.mContext = context;
    }

    public boolean isOnline() {
        ConnectivityManager connMngr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean openDialog(AlertDialog.Builder builder){

    }
}
