package govph.rsis.growapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtViewModel;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    GlobalFunction globalFunction;
    private static final String TAG = "Login";
    private int LOCATION_PERMISSION_CODE = 1;
    private int CAMERA_PERMISSION_CODE = 1;
    private boolean mPermissionGranted;
    public static final int REQUEST_CODE = 1;
    private UserViewModel userViewModel;
    private SeedBoughtViewModel seedBoughtViewModel;
    TextView loginTvVersion;
    LayoutInflater inflater;
    Button scanBtn;
    Intent intent;

    RequestQueue queue;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    View dialogView;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestCameraPermission();
        inflater = getLayoutInflater();
        globalFunction = new GlobalFunction(getApplicationContext());
        globalFunction.isOnline();
        userViewModel = ViewModelProviders.of(LoginActivity.this).get(UserViewModel.class);
        seedBoughtViewModel = ViewModelProviders.of(LoginActivity.this).get(SeedBoughtViewModel.class);
        builder = new AlertDialog.Builder(this);
        scanBtn = findViewById(R.id.scanIdBtn);
        loginTvVersion = findViewById(R.id.loginTvVersion);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            loginTvVersion.setText("Version: "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DecVar.isNetworkConnected){
                    intent = new Intent(LoginActivity.this,ScannerActivity.class);
                    startActivityForResult(intent,REQUEST_CODE);

                }else{
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("No Internet Connection")
                            .setMessage("Please make sure you have an internet connection before scanning.")
                            .setPositiveButton("Okay",null)
                            .show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if (resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                dialogView = inflater.inflate(R.layout.loading_template,null);
                builder.setCancelable(false);
                builder.setView(dialogView);

                dialog = builder.create();
                dialog.show();
                final String result = data.getStringExtra(ScannerActivity.Scanned_value);
                getUserLoginDetails(result);
                // Use the data - in this case, display it in a Toast.
            } else {
                // AnotherActivity was not successful. No data to retrieve.
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestLocationPermission();
        }
    }
    private void getUserLoginDetails(String scannedValue){
        queue = Volley.newRequestQueue(LoginActivity.this);
        String url = DecVar.userCredentialApiUrl()+'/'+scannedValue;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = new User();

                SeedGrowerDatabase database = SeedGrowerDatabase.getInstance(LoginActivity.this);
                if(response.equals("204")){
                    Toast.makeText(LoginActivity.this, "Make sure that your Serial Number is correct.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else{
                    try {
                        JSONObject json = new JSONObject(response);
                        Log.e(TAG, "onResponse: "+json );
                        if(json != null || json.length() > 0){
                            String fullName = json.getString("fullName");
                            String accredArea = json.getString("accredArea");
                            String serialNum = json.getString("serialNum");
                            String data = json.getString("data");
                            int isExisting = userViewModel.isExisting(serialNum);

                            JSONArray jsonArray = json.getJSONArray("data");
                            if(isExisting > 0 ){
                                userViewModel.getUpdateUserStatus("LoggedIn",serialNum);
                            }else{
                                user.setSerialNum(serialNum);
                                user.setFullname(fullName);
                                user.setAccredArea(accredArea);
                                user.setLoggedIn("LoggedIn");
                                userViewModel.insert(user);
                            }

                            if(jsonArray.length() != 0){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    SeedBought seedBought = new SeedBought();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String palletCode = jsonObject.getString("palletCode");
                                    String variety = jsonObject.getString("variety");
                                    String seedClass = jsonObject.getString("seedClass");
                                    String riceProgram = jsonObject.getString("riceProgram");
                                    String table_name = jsonObject.getString("table_name");
                                    String station_name = jsonObject.getString("station_name");
                                    int quantity = jsonObject.getInt("quantity");
                                    double area = jsonObject.getDouble("area");

                                    seedBought.setSerialNum(serialNum);
                                    seedBought.setVariety(variety);
                                    seedBought.setSeedClass(seedClass);
                                    seedBought.setPalletCode(palletCode);
                                    seedBought.setQuantity(quantity);
                                    seedBought.setUsedQuantity(0);
                                    seedBought.setTableName(table_name);
                                    seedBought.setStation_name(station_name);
                                    seedBought.setArea(area);
                                    seedBought.setRiceProgram(riceProgram);

                                    seedBoughtViewModel.insert(seedBought);
                                }
                            }

                            /*for(int i = 0; i < json.length(); i++){
                                JSONObject jsonObject = json.getJSONObject(i);
                                String fullName = jsonObject.getString("fullName");
                                String accredArea = jsonObject.getString("accredArea");
                                String serialNum = jsonObject.getString("serialNum");
                                user.setSerialNum(serialNum);
                                int isExisting = userViewModel.isExisting(serialNum);


                                if(isExisting > 0 ){
                                    userViewModel.getUpdateUserStatus("LoggedIn",serialNum);
                                }else{
                                    user.setFullname(fullName);
                                    user.setAccredArea(accredArea);
                                    user.setLoggedIn("LoggedIn");
                                    userViewModel.insert(user);
                                }

                            }*/
                                //if(userViewModel.isExisting(user.serialNum);)
                                dialog.dismiss();
                                intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "No Seed Grower details.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e(TAG, "onErrorResponse: "+error );
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
        //Toast.makeText(this, "scannedValue: "+scannedValue, Toast.LENGTH_SHORT).show();
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Allow this app to access camera to scan your QR CODE.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            mPermissionGranted = true;

                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            mPermissionGranted = false;
        }
    }

    private void requestLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Location Permission")
                    .setMessage("Allow the GrowApp to access your location during the data collection, this is to verify the field location when sent to the server")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    }).setCancelable(false)
                    .create().show();
            /*if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("Allow this app to access location.")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            else{
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
            }*/
        }
    }
}