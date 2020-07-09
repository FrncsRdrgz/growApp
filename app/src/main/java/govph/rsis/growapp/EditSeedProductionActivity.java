package govph.rsis.growapp;

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

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EditSeedProductionActivity extends AppCompatActivity implements LocationListener {
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String TAG = "EditSeedProduction";
    private int LOCATION_PERMISSION_CODE = 1;
    private int CAMERA_PERMISSION_CODE = 1;
    private boolean mPermissionGranted;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String uniqueId="";
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
    private SeedGrowerViewModel seedGrowerViewModel;
    Intent intent;
    TelephonyManager telephonyManager;
    SeedGrower seedGrowers;

    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_production_detail);
        toolbar = findViewById(R.id.spToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(EditSeedProductionActivity.this, scannerView);
        database = SeedGrowerDatabase.getInstance(this);
        intent = getIntent();
        String formId = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);

        id = Integer.parseInt(formId);
        seedGrowers = database.seedGrowerDao().findFormById(formId);
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        //find the view of linear layout
        l10 = (LinearLayout) findViewById(R.id.l10);
        l11 = (LinearLayout) findViewById(R.id.l11);

        //find view of text views
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvAccredNo = (TextView) findViewById(R.id.tvAccreditationNo);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);

        //find view of buttons
        scanBtn = (Button) findViewById(R.id.scanBtn);
        scanBtn.setClickable(false);
        scanBtn.setBackgroundColor(Color.LTGRAY);
        getLocationBtn = (Button) findViewById(R.id.getLocationBtn);
        getLocationBtn.setClickable(false);
        getLocationBtn.setBackgroundColor(Color.LTGRAY);

        //find view of all EditText widget
        etDatePlanted = (EditText) findViewById(R.id.etDatePlanted);
        etAreaPlanted = (EditText) findViewById(R.id.etAreaPlanted);
        etSeedQuantity = (EditText) findViewById(R.id.etSeedQuantity);
        etSeedbedArea = (EditText) findViewById(R.id.etSeedbedArea);
        etSeedlingAge = (EditText) findViewById(R.id.etSeedlingAge);
        etSeedLot = (EditText) findViewById(R.id.etSeedLot);
        etControlNo = (EditText) findViewById(R.id.etControlNo);
        etBarangay = (EditText) findViewById(R.id.etBarangay);
        etOtherSource = (EditText) findViewById(R.id.etOtherSeedSource);

        //find view of all  Spinner widget
        varietyspinner = (Spinner) findViewById(R.id.varietyPlantedSpinner);
        sourcespinner = (Spinner) findViewById(R.id.seedSourceSpinner);
        classspinner = (Spinner) findViewById(R.id.seedClassSpinner);

       // Log.e(TAG, "onClick: " + DebugDB.getAddressLog());

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");//set format of date you receive from db
        Date date = null;
        try {
            date = (Date) sdf.parse(seedGrowers.getDateplanted());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("MMMM dd, yyyy");//set format of new date

        //populate all the EditText and textview of selected form
        tvAccredNo.setText(seedGrowers.getAccredno());
        tvLatitude.setText(seedGrowers.getLatitude());
        tvLongitude.setText(seedGrowers.getLongitude());
        etDatePlanted.setText(newDate.format(date));
        etAreaPlanted.setText(seedGrowers.getAreaplanted());
        etSeedQuantity.setText(seedGrowers.getQuantity());
        etSeedbedArea.setText(seedGrowers.getSeedbedarea());
        etSeedlingAge.setText(seedGrowers.getSeedlingage());
        etSeedLot.setText(seedGrowers.getSeedlot());
        etControlNo.setText(seedGrowers.getControlno());
        etBarangay.setText(seedGrowers.getBarangay());
        etOtherSource.setText(seedGrowers.getOtherseedsource());


        tvAccredNo.addTextChangedListener(saveTextWatcher);
        tvLatitude.addTextChangedListener(saveTextWatcher);
        tvLongitude.addTextChangedListener(saveTextWatcher);

        String accredNo = tvAccredNo.getText().toString().trim();
        String latitude = tvLatitude.getText().toString().trim();
        String longitude = tvLongitude.getText().toString().trim();

        if(!accredNo.isEmpty() && !latitude.isEmpty() && !longitude.isEmpty()){
            tvSave.setEnabled(true);
            tvSave.setTextColor(Color.BLACK);
        }
        if (ContextCompat.checkSelfPermission(EditSeedProductionActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                uniqueId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                uniqueId = telephonyManager.getDeviceId();
            }


        }

        //Scanning of qr code
        /*scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFunction();
            }
        });*/

        //open datepicker
        etDatePlanted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditSeedProductionActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = MONTHS[month] + " " + dayOfMonth + ", " + year;
                etDatePlanted.setText(date);
            }
        };

        //getting location
        /*getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });*/


        List<Seeds> seeds = database.seedsDao().getSeeds();
        ArrayList varieties = new ArrayList<>();
        for(Seeds s : seeds){
            varieties.add(s.getVariety());
        }

        String[] stations = new String[]{
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

        String[] classes = new String[]{
                "Select Seed Class",
                "Foundation",
                "Registered"
        };
        //spinner for seed  class
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(
                this, R.layout.spinner_seed_class, classes
        );
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_seed_source);
        classspinner.setAdapter(spinnerArrayAdapter2);
        classspinner.setSelection(spinnerArrayAdapter2.getPosition(seedGrowers.getSeedclass()));

        //spinner for seed source
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(
                this, R.layout.spinner_seed_source, stations
        );
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_seed_source);
        sourcespinner.setAdapter(spinnerArrayAdapter1);
        sourcespinner.setSelection(spinnerArrayAdapter1.getPosition(seedGrowers.getSeedsource()));
        sourcespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Others")) {
                    l10.setVisibility(View.VISIBLE);
                    l11.setVisibility(View.VISIBLE);
                } else {
                    l10.setVisibility(View.GONE);
                    l11.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner for Seed Varieties
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_seed_variety, varieties
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_seed_variety);
        varietyspinner.setAdapter(spinnerArrayAdapter);
        varietyspinner.setSelection(spinnerArrayAdapter.getPosition(seedGrowers.getVariety()));

        //cancel Button
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFunction();
            }
        });
    }

    public void UpdateFunction(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");//set format of date you receiving from db
        Date date = null;
        String dateplanted;
        try {
            date = (Date) sdf.parse(etDatePlanted.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");//set format of new date

        String uniqueid = md5(uniqueId + getMacAddr());
        String accredno = tvAccredNo.getText().toString();
        String latitude = tvLatitude.getText().toString();
        String longitude = tvLongitude.getText().toString();
        String seedVariety = varietyspinner.getSelectedItem().toString();
        String seedSource = sourcespinner.getSelectedItem().toString();
        String otherSeedSource = etOtherSource.getText().toString();
        String seedClass = classspinner.getSelectedItem().toString();
        if (date == null) {
            dateplanted = "0000-00-00";
        } else {
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

        Log.e(TAG, "Accreditation number: "+accredno );
        //SeedGrower seedGrower = new SeedGrower();
        /*seedGrowers.setMacaddress(uniqueid);
        seedGrowers.setAccredno(accredno);
        seedGrowers.setLatitude(latitude);
        seedGrowers.setLongitude(longitude);
        seedGrowers.setVariety(seedVariety);
        seedGrowers.setSeedsource(seedSource);
        seedGrowers.setOtherseedsource(otherSeedSource);
        seedGrowers.setSeedclass(seedClass);
        seedGrowers.setDateplanted(dateplanted);
        seedGrowers.setAreaplanted(areaPlanted);
        seedGrowers.setQuantity(seedQuantity);
        seedGrowers.setSeedbedarea(seedbedArea);
        seedGrowers.setSeedlingage(seedlingAge);
        seedGrowers.setSeedlot(seedLot);
        seedGrowers.setControlno(controlNo);
        seedGrowers.setBarangay(barangay);
        seedGrowers.setDatecollected(datecollected);*/


        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        SeedGrower seedGrower = new SeedGrower(uniqueid,accredno,latitude,longitude,seedVariety,seedSource,otherSeedSource,seedClass,dateplanted,
                areaPlanted,seedQuantity,seedbedArea,seedlingAge,seedLot,controlNo,barangay,datecollected,isSent);
        seedGrower.setSgId(id);
        seedGrowerViewModel.update(seedGrower);
        finish();
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
    public void getLocation() {
        if (ContextCompat.checkSelfPermission(EditSeedProductionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(EditSeedProductionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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
                new AlertDialog.Builder(EditSeedProductionActivity. this )
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, EditSeedProductionActivity.this);
                Criteria criteria = new Criteria();
                Log.e(TAG, "onClick: "+criteria );
                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                Toast.makeText(EditSeedProductionActivity.this, "Getting your current location.", Toast.LENGTH_SHORT).show();

                if (location == null) {
                    Toast.makeText(getApplicationContext(), "GPS signal not found", Toast.LENGTH_SHORT).show();
                }
                if (location != null) {

                    String latitude = Double.toString(location.getLatitude());
                    String longitude = Double.toString(location.getLongitude());
                    tvLatitude.setText(latitude);
                    tvLongitude.setText(longitude);
                }
            }

        }else {
            requestLocationPermission();
        }
    }
    public void scanFunction() {
        if(ContextCompat.checkSelfPermission(EditSeedProductionActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = true;
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
                    EditSeedProductionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditSeedProductionActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            frameLayout.setVisibility(View.INVISIBLE);
                            scrollView.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.VISIBLE);
                            tvAccredNo.setText(result.getText());

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
        if(mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mPermissionGranted) {
            mCodeScanner.releaseResources();
        }
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
                            ActivityCompat.requestPermissions(EditSeedProductionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
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
                            ActivityCompat.requestPermissions(EditSeedProductionActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            mPermissionGranted = true;
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(EditSeedProductionActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            mPermissionGranted = false;
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
