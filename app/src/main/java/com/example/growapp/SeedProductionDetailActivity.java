package com.example.growapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SeedProductionDetailActivity extends AppCompatActivity implements LocationListener {
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String TAG = "SeedProductionDetail";
    private int LOCATION_PERMISSION_CODE = 1;
    private int CAMERA_PERMISSION_CODE = 1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String uniqueId="";
    private SeedGrowerViewModel seedGrowerViewModel;
    LocationManager locationManager;
    LocationListener locationListener;
    Toolbar toolbar;
    LinearLayout l10, l11;
    TextView tvLatitude, tvLongitude, tvCancel,tvSave,tvAccredNo;
    Button getLocationBtn,scanBtn;
    Spinner varietyspinner, sourcespinner,classspinner;
    EditText etDatePlanted,etSeedClass,etAreaPlanted, etSeedQuantity, etSeedbedArea, etSeedlingAge, etSeedLot, etControlNo, etBarangay, etOtherSource;
    FrameLayout frameLayout;
    ScrollView scrollView;

    CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    SeedGrowerDatabase database;

    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_production_detail);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(SeedProductionDetailActivity.this, scannerView);
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        Intent intent = getIntent();

        String test = intent.getStringExtra(HomeActivity.EXTRA_ARRAY);
        Log.e(TAG, "onCreate: "+test );
        l10 = (LinearLayout) findViewById(R.id.l10);
        l11 = (LinearLayout) findViewById(R.id.l11);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvAccredNo = (TextView) findViewById(R.id.tvAccreditationNo);
        getLocationBtn = (Button) findViewById(R.id.getLocationBtn);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        toolbar = findViewById(R.id.spToolbar);

        etDatePlanted = (EditText) findViewById(R.id.etDatePlanted);

        etAreaPlanted = (EditText) findViewById(R.id.etAreaPlanted);
        etSeedQuantity = (EditText) findViewById(R.id.etSeedQuantity);
        etSeedbedArea = (EditText) findViewById(R.id.etSeedbedArea);
        etSeedlingAge = (EditText) findViewById(R.id.etSeedlingAge);
        etSeedLot = (EditText) findViewById(R.id.etSeedLot);
        etControlNo = (EditText) findViewById(R.id.etControlNo);
        etBarangay   = (EditText) findViewById(R.id.etBarangay);
        etOtherSource = (EditText) findViewById(R.id.etOtherSeedSource);

        varietyspinner = (Spinner) findViewById(R.id.varietyPlantedSpinner);
        sourcespinner = (Spinner) findViewById(R.id.seedSourceSpinner);
        classspinner = (Spinner) findViewById(R.id.seedClassSpinner);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        database = SeedGrowerDatabase.getInstance(this);


        tvAccredNo.addTextChangedListener(saveTextWatcher);
        tvLatitude.addTextChangedListener(saveTextWatcher);
        tvLongitude.addTextChangedListener(saveTextWatcher);
        if(ContextCompat.checkSelfPermission(SeedProductionDetailActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                uniqueId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                uniqueId = telephonyManager.getDeviceId();
            }


        }

        //Scanning of qr code
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFunction();
            }
        });

        //open datepicker
        etDatePlanted.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SeedProductionDetailActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = MONTHS[month] + " " + dayOfMonth + ", "+year;
                etDatePlanted.setText(date);
            }
        };

        //getting location
        getLocationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        String[] varieties = new String[]{
                "Select Seed Variety",
                "NSIC Rc 222",
                "NSIC Rc 218",
                "NSIC Rc 216",
                "NSIC Rc 300",
                "NSIC 2015 Rc 400",
                "NSIC Rc 402",
        };

        String[] stations = new String[] {
                "Select Seed Source",
                "PhilRice CES",
                "PhilRice Midsayap",
                "PhilRice Los Baños",
                "PhilRice Agusan",
                "PhilRice Batac",
                "PhilRice Isabela",
                "PhilRice Negros",
                "PhilRice Bicol",
                "PhilRice CMU",
                "PhilRice Zamboanga",
                "PhilRice Samar",
                "PhilRice Mindoro",
                "Others"
        };

        String[] classes = new String []{
                "Foundation",
                "Registered"
        };

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(
                this,R.layout.spinner_seed_class,classes
        );
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_seed_source);
        classspinner.setAdapter(spinnerArrayAdapter2);

        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(
                this,R.layout.spinner_seed_source,stations
        );
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_seed_source);
        sourcespinner.setAdapter(spinnerArrayAdapter1);

        sourcespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Others")) {
                    l10.setVisibility(View.VISIBLE);
                    l11.setVisibility(View.VISIBLE);
                }
                else {
                    l10.setVisibility(View.GONE);
                    l11.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this,R.layout.spinner_seed_variety,varieties
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_seed_variety);
        varietyspinner.setAdapter(spinnerArrayAdapter);

        //cancel Button
        tvCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveForm();
            }
        });
    }

    private TextWatcher saveTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String accredNo = tvAccredNo.getText().toString().trim();
            String latitude = tvLatitude.getText().toString().trim();
            String longitude = tvLongitude.getText().toString().trim();

            if(!accredNo.isEmpty() && !latitude.isEmpty() && !longitude.isEmpty()){
                tvSave.setEnabled(true);
                tvSave.setTextColor(Color.BLACK);
            }

        }
    };

    public void saveForm() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");//set format of date you receiving from db
        Date date = null;
        String dateplanted;
        try {
            date = (Date) sdf.parse(etDatePlanted.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");//set format of new date

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String timestamp = dateFormat.format(calendar.getTime());

        String uniqueid = md5(uniqueId + getMacAddr() + timestamp);
        String accredno = tvAccredNo.getText().toString();
        String latitude = tvLatitude.getText().toString();
        String longitude = tvLongitude.getText().toString();
        String seedVariety = varietyspinner.getSelectedItem().toString();
        String seedSource = sourcespinner.getSelectedItem().toString();
        String otherSeedSource = etOtherSource.getText().toString();
        String seedClass = classspinner.getSelectedItem().toString();
        if(date == null){
            dateplanted = "0000-00-00";
        }else{
            dateplanted = newDate.format(date);// here is your new date !
        }
        String areaPlanted = etAreaPlanted.getText().toString();
        String seedQuantity = etSeedQuantity.getText().toString();
        String seedbedArea = etSeedbedArea.getText().toString();
        String seedlingAge = etSeedlingAge.getText().toString();
        String seedLot = etSeedLot.getText().toString();
        String controlNo = etControlNo.getText().toString();
        String barangay = etBarangay.getText().toString();
        String datecollected = newDate.format(new Date());
        Boolean isSent = false;

        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        SeedGrower seedGrower = new SeedGrower(uniqueid,accredno,latitude,longitude,seedVariety,seedSource,otherSeedSource,seedClass,dateplanted,
                areaPlanted,seedQuantity,seedbedArea,seedlingAge,seedLot,controlNo,barangay,datecollected,isSent);
        seedGrowerViewModel.insert(seedGrower);
        finish();
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(SeedProductionDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(SeedProductionDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
            if (!gps_enabled && !network_enabled) {
                new AlertDialog.Builder(SeedProductionDetailActivity. this )
                        .setMessage( "Please enable your GPS to get Location." )
                        .setPositiveButton( "Okay" , new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                        startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                    }
                                })
                        .setNegativeButton( "Cancel" , null )
                        .show() ;
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, SeedProductionDetailActivity.this);
                Criteria criteria = new Criteria();
                Log.e(TAG, "onClick: "+criteria );
                String bestProvider = locationManager.getBestProvider(criteria, true);
                final Location location = locationManager.getLastKnownLocation(bestProvider);
                Toast.makeText(SeedProductionDetailActivity.this, "Getting your current location.", Toast.LENGTH_SHORT).show();

                if (location == null) {
                    Toast.makeText(getApplicationContext(), "GPS signal not found", Toast.LENGTH_SHORT).show();
                }
                if (location != null) {

                    new AlertDialog.Builder(SeedProductionDetailActivity.this)
                            .setMessage("Location Found")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String latitude = Double.toString(location.getLatitude());
                                    String longitude = Double.toString(location.getLongitude());
                                    tvLatitude.setText(latitude);
                                    tvLongitude.setText(longitude);
                                }
                            }).show();
                }
            }

        }else {
            requestLocationPermission();
        }
    }

    public void scanFunction() {
        if(ContextCompat.checkSelfPermission(SeedProductionDetailActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            frameLayout = (FrameLayout) findViewById(R.id.scannerLayout);
            frameLayout.setVisibility(View.VISIBLE);
            tvAccredNo.setText("");
            scrollView = (ScrollView) findViewById(R.id.scrollView1);
            scrollView.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.INVISIBLE);

            mCodeScanner.startPreview();
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    SeedProductionDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SeedProductionDetailActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder( SeedProductionDetailActivity.this)
                                    .setMessage("QR CODE SCANNED")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            frameLayout.setVisibility(View.INVISIBLE);
                                            scrollView.setVisibility(View.VISIBLE);
                                            toolbar.setVisibility(View.VISIBLE);
                                            tvAccredNo.setText(result.getText());
                                        }
                                    }).show();
                        }
                    });

                }
            });
        }
        else{
            requestCameraPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1)
                        hex = "0".concat(hex);
                    res1.append(hex.concat(":"));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SeedProductionDetailActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Allow this app to access camera to scan your QR CODE.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SeedProductionDetailActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(SeedProductionDetailActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
