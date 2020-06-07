package govph.rsis.growapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.example.releasingapp.MESSAGE";
    private static int SPLASH_TIME_OUT = 2000;
    Intent intent;
    TextView tvVersion;
    SeedGrowerDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        readCSV();
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText("Version: "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    private void readCSV() {
        database = SeedGrowerDatabase.getInstance(this);
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try{
            while( (line = reader.readLine()) != null){
                String[] tokens = line.split(",");

                Seeds seedss = new Seeds(tokens[1]);
                database.seedsDao().insertSeeds(seedss);
            }
        } catch (IOException e) {
            Log.e(TAG, "readCSV: " + e );
        }
    }
}
