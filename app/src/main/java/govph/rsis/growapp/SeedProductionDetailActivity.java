package govph.rsis.growapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtViewModel;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserViewModel;

import static android.graphics.Color.*;


public class SeedProductionDetailActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sepr", "Oct", "Nov", "Dec"};
    private static final String TAG = "SeedProductionDetail";
    private int LOCATION_PERMISSION_CODE = 1;
    private int CAMERA_PERMISSION_CODE = 1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String uniqueId="";
    private SeedGrowerViewModel seedGrowerViewModel;
    private UserViewModel userViewModel;
    private SeedBoughtViewModel seedBoughtViewModel;
    private boolean mPermissionGranted;
    private List<Seeds> seedSample = new ArrayList<>();
    public static final int REQUEST_CODE_EXAMPLE = 0x9988;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    int activityId,countCollected,countSent,countDeleted;
    Intent intent;
    LocationManager locationManager;
    Toolbar toolbar;
    LinearLayout l10, l11;
    TextView tvLatitude, tvLongitude, tvCancel,tvSave,tvAccredNo, tvVersion,tvWelcomeName,tvWelcomeSerial,tvList,tvSent,tvDeleted,titleForm,backBtn;
    Button getLocationBtn,btnSave;
    AutoCompleteTextView actVariety,actOtherVariety,actSeedClass, actSeedSource,actRiceProgram,actPreviousVariety;
    TextInputLayout tilVariety,tilOtherVariety,tilSeedClass,tilSeedSource, tilRiceProgram,tilDatePlanted,tilSeedClass2,tillSeedSource2,tilRiceProgram2,tilAreaPlanted,tilSeedQuantity,tilSeedBedArea,tilSeedlingAge,tilSeedLotNo,tilLabNo,tilCooperative,tilBarangay;
    ArrayAdapter<String> arrayAdapterVariety,arrayAdapterSeedClass,arrayAdapterSeedSource,arrayAdapterRiceProgram,arrayAdapterPreviousVariety,arrayAdapterOtherVariety;
    TextInputEditText tetSeedClass,tetSeedSource,tetRiceProgram,tetDatePlanted,tetAreaPlanted,tetSeedQuantity,tetSeedBedArea,tetSeedlingAge,tetSeedLotNo,tetLabNo,tetCoop,
            tetBarangay,tetPreviousCrop;
    MenuItem mList,mSent,mDeleted;
    ArrayList arrayListVarieties,arrayListSeedClass,arrayListSeedSource,arrayListRiceProgram;

    MaterialDatePicker.Builder materialBuilder;
    MaterialDatePicker materialDatePicker;
    CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    SeedGrowerDatabase database;

    TelephonyManager telephonyManager;
    User user;
    List<SeedBought> seedBought;
    SeedBought selectedSeed;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_production_detail);

        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        activityId = getIntent().getIntExtra("activityId",0);
        scannerView = findViewById(R.id.scanner_view);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        seedBoughtViewModel = ViewModelProviders.of(this).get(SeedBoughtViewModel.class);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        user = userViewModel.getLoggedInUser();
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());
        //mCodeScanner = new CodeScanner(SeedProductionDetailActivity.this, scannerView);
        l10 = (LinearLayout) findViewById(R.id.l10);
        l11 = (LinearLayout) findViewById(R.id.l11);

        //tvCancel = (TextView) findViewById(R.id.tvCancel);
        //spTvWelcomeName = (TextView) findViewById(R.id.spTvWelcomeName);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvAccredNo = (TextView) findViewById(R.id.tvAccreditationNo);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        titleForm = findViewById(R.id.titleForm);
        toolbar = findViewById(R.id.add_toolbar);

        //spTvWelcomeName.setText("Welcome, "+ user.getFullname());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setTitle("New Form");


        drawerLayout = findViewById(R.id.add_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvVersion = headerView.findViewById(R.id.headerVersion);
        tvWelcomeName = (TextView) headerView.findViewById(R.id.headerName);
        tvWelcomeSerial = (TextView) headerView.findViewById(R.id.headerSerial);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        /*Populate the navigation drawer badge*/
        mList = navigationView.getMenu().findItem(R.id.listBtn);
        mSent = navigationView.getMenu().findItem(R.id.sentItemBtn);
        mDeleted = navigationView.getMenu().findItem(R.id.deletedBtn);
        tvList = (TextView) mList.getActionView();
        tvList.setText(String.valueOf(countCollected));
        tvSent = (TextView) mSent.getActionView();
        tvSent.setText(String.valueOf(countSent));
        tvDeleted = (TextView) mDeleted.getActionView();
        tvDeleted.setText(String.valueOf(countDeleted));
        /*end badge*/
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.addBtn);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText("Version: "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvWelcomeName.setText(user.getFullname());
        tvWelcomeSerial.setText(user.getSerialNum());

        getLocationBtn = (Button) findViewById(R.id.getLocationBtn);
        btnSave = findViewById(R.id.btnSave);
        backBtn = findViewById(R.id.backBtn);

        btnSave.setEnabled(false);
        tilVariety = (TextInputLayout) findViewById(R.id.tilVariety);
        tilOtherVariety = (TextInputLayout) findViewById(R.id.tilOtherVariety);
        tilSeedClass = (TextInputLayout) findViewById(R.id.tilSeedClass);
        tilSeedSource = (TextInputLayout) findViewById(R.id.tilSeedSource);
        tilRiceProgram = (TextInputLayout) findViewById(R.id.tilRiceProgram);
        tilDatePlanted = (TextInputLayout) findViewById(R.id.tilDatePlanted);

        tilSeedClass2 = (TextInputLayout) findViewById(R.id.tilSeedClass2);
        tillSeedSource2 = (TextInputLayout) findViewById(R.id.tilSeedSource2);
        tilRiceProgram2 = (TextInputLayout) findViewById(R.id.tilRiceProgram2);

        actSeedClass = (AutoCompleteTextView) findViewById(R.id.actSeedClass);
        actVariety = (AutoCompleteTextView) findViewById(R.id.actVariety);
        actOtherVariety = (AutoCompleteTextView) findViewById(R.id.actOtherVariety);

        actSeedSource = (AutoCompleteTextView) findViewById(R.id.actSeedSource);
        actRiceProgram = (AutoCompleteTextView) findViewById(R.id.actRiceProgram);
        actPreviousVariety = (AutoCompleteTextView) findViewById(R.id.actPreviousVariety);

        tetSeedSource = (TextInputEditText) findViewById(R.id.tetSeedSource);
        tetRiceProgram = (TextInputEditText) findViewById(R.id.tetRiceProgram);
        tetSeedClass = (TextInputEditText) findViewById(R.id.tetSeedClass);
        tetDatePlanted = (TextInputEditText) findViewById(R.id.tetDatePlanted);
        tetAreaPlanted = (TextInputEditText) findViewById(R.id.tetAreaPlanted);
        tetSeedQuantity = (TextInputEditText) findViewById(R.id.tetSeedQuantity);
        tetSeedBedArea = (TextInputEditText) findViewById(R.id.tetSeedBedArea);
        tetSeedlingAge = (TextInputEditText) findViewById(R.id.tetSeedlingAge);
        tetSeedLotNo = (TextInputEditText) findViewById(R.id.tetSeedLotNo);
        tetLabNo = (TextInputEditText) findViewById(R.id.tetLabNo);
        tetCoop = (TextInputEditText) findViewById(R.id.tetCoop);
        tetBarangay = (TextInputEditText) findViewById(R.id.tetBarangay);
        tetPreviousCrop = (TextInputEditText) findViewById(R.id.tetPreviousCrop);

        materialBuilder = MaterialDatePicker.Builder.datePicker();
        materialBuilder.setTitleText("Select Date Planted");
        materialDatePicker = materialBuilder.build();

        database = SeedGrowerDatabase.getInstance(this);
        //String serial = "<b>"+user.getSerialNum()+"</b>";
        tvAccredNo.setText(user.getSerialNum());
        tvAccredNo.addTextChangedListener(saveTextWatcher);
        tvLatitude.addTextChangedListener(saveTextWatcher);
        tvLongitude.addTextChangedListener(saveTextWatcher);
        tetSeedQuantity.addTextChangedListener(quantityTextWatcher);


        if(ContextCompat.checkSelfPermission(SeedProductionDetailActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
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
        seedBought = seedBoughtViewModel.getAllSeedBought(user.getSerialNum());
        //int counter = seedBought.size() + 1;
        //Log.e(TAG, "Counter: "+counter );
        final String[] spinnerArraySeeds = new String[seedBought.size()+1];
        final HashMap<Integer, SeedBought> spinnerMap = new HashMap<Integer, SeedBought>();
        arrayListVarieties = new ArrayList<>();
        for (int i = 0; i< seedBought.size(); i++){
            spinnerMap.put(i,seedBought.get(i));
            spinnerArraySeeds[i] = seedBought.get(i).getVariety();
        }
        spinnerArraySeeds[seedBought.size()] = "Others";
        arrayAdapterVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_variety,spinnerArraySeeds);
        actVariety.setAdapter(arrayAdapterVariety);
        actVariety.setThreshold(1);

        //Searchable Spinners here
        //Populate the ArrayListVarieties;
        List<Seeds> seeds = database.seedsDao().getSeeds();
        arrayListVarieties = new ArrayList<>();
        for(Seeds s : seeds){
            String seed = s.getVariety().replaceAll("\\s","").toLowerCase();
            /*for(SeedBought sb :  seedBought){
                String bought = sb.getVariety().replaceAll("\\s","").toLowerCase();

                if(bought == seed){
                    Log.e(TAG, "bought: "+bought );
                    Log.e(TAG, "seed: " +bought );
                }
            }*/
            arrayListVarieties.add(s.getVariety());
        }
        //set layout of the variety
        arrayAdapterOtherVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_source,arrayListVarieties);
        actOtherVariety.setAdapter(arrayAdapterOtherVariety);
        actOtherVariety.setThreshold(1);

        arrayAdapterPreviousVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_variety,arrayListVarieties);
        actPreviousVariety.setAdapter(arrayAdapterPreviousVariety);
        actPreviousVariety.setThreshold(1);

        //populate the Arraylist of seed class
        arrayListSeedClass = new ArrayList<>();
        arrayListSeedClass.add("Breeder");
        arrayListSeedClass.add("Foundation");
        arrayListSeedClass.add("Registered");

        //set layout of the seed class
        arrayAdapterSeedClass = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_class,arrayListSeedClass);
        actSeedClass.setAdapter(arrayAdapterSeedClass);
        actSeedClass.setThreshold(1);

        //populate the array list of seed source
        arrayListSeedSource = new ArrayList<>();
        arrayListSeedSource.add("PhilRice Maligaya");
        arrayListSeedSource.add("PhilRice Midsayap");
        arrayListSeedSource.add("PhilRice Los Baños");
        arrayListSeedSource.add("PhilRice Agusan");
        arrayListSeedSource.add("PhilRice Batac");
        arrayListSeedSource.add("PhilRice Isabela");
        arrayListSeedSource.add("PhilRice Negros");
        arrayListSeedSource.add("PhilRice Bicol");
        arrayListSeedSource.add("PhilRice CMU");
        arrayListSeedSource.add("PhilRice Zamboanga");
        arrayListSeedSource.add("PhilRice Samar");
        arrayListSeedSource.add("PhilRice Mindoro");

        //set layout of the seed source
        arrayAdapterSeedSource = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_source,arrayListSeedSource);
        actSeedSource.setAdapter(arrayAdapterSeedSource);
        actSeedSource.setThreshold(1);

        //populate the arraylist of rice program
        arrayListRiceProgram = new ArrayList<>();
        arrayListRiceProgram.add("National Rice Program");
        arrayListRiceProgram.add("RCEF");
        arrayListRiceProgram.add("NONE");

        //set layout of the riceProgram
        arrayAdapterRiceProgram = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_rice_program,arrayListRiceProgram);
        actRiceProgram.setAdapter(arrayAdapterRiceProgram);
        actRiceProgram.setThreshold(1);

        //get the location
        getLocationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        //button to cancel the activity
        /*tvCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        //set the Save text clickable
        backBtn.setVisibility(View.GONE);
        /*backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        tetDatePlanted.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SeedProductionDetailActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth +" "+MONTHS[month] +" "+year;
                tetDatePlanted.setText(date);
            }
        };

        actVariety.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(spinnerArraySeeds[position].equals("Others")){
                    tilSeedClass2.setVisibility(View.GONE);
                    tillSeedSource2.setVisibility(View.GONE);
                    tilRiceProgram2.setVisibility(View.GONE);

                    tilRiceProgram.setVisibility(View.VISIBLE);
                    tilSeedClass.setVisibility(View.VISIBLE);
                    tilSeedSource.setVisibility(View.VISIBLE);
                    tilOtherVariety.setVisibility(View.VISIBLE);
                    tetSeedQuantity.setText("");
                    tetAreaPlanted.setText("");
                    selectedSeed = null;
                }else{
                    tilSeedClass2.setVisibility(View.VISIBLE);
                    tillSeedSource2.setVisibility(View.VISIBLE);
                    tilRiceProgram2.setVisibility(View.VISIBLE);

                    tilRiceProgram.setVisibility(View.GONE);
                    tilSeedClass.setVisibility(View.GONE);
                    tilSeedSource.setVisibility(View.GONE);
                    tilOtherVariety.setVisibility(View.GONE);

                    selectedSeed = spinnerMap.get(position);
                    String source = spinnerMap.get(position).getTableName().toLowerCase();
                    Log.e(TAG, "onItemClick: "+selectedSeed );
                    //Toast.makeText(SeedProductionDetailActivity.this, "quantity" + spinnerMap.get(position).getQuantity(), Toast.LENGTH_SHORT).show();
                    if(spinnerMap.get(position).getSeedClass().equals("RS")){
                        tetSeedClass.setText("Registered");
                    }else if(spinnerMap.get(position).getSeedClass().equals("FS")){
                        tetSeedClass.setText("Foundation");
                    }

                    if(source.contains("ces")){
                        tetSeedSource.setText("PhilRice - Science City of Muñoz");
                    }
                    else if(source.contains("bes")){
                        tetSeedSource.setText("PhilRice - Batac");
                    }
                    else if(source.contains("mes")){
                        tetSeedSource.setText("PhilRice - Midsayap");
                    }
                    else if(source.contains("lbs")){
                        tetSeedSource.setText("PhilRice - Los Baños");
                    }
                    else if(source.contains("aes")){
                        tetSeedSource.setText("PhilRice - Agusan");
                    }
                    else if(source.contains("cves")){
                        tetSeedSource.setText("PhilRice - Isabela");
                    }
                    else if(source.contains("prn")){
                        tetSeedSource.setText("PhilRice - Negros");
                    }
                    else if(source.contains("bies")){
                        tetSeedSource.setText("PhilRice - Bicol");
                    }
                    else if(source.contains("cmu")){
                        tetSeedSource.setText("PhilRice - CMU Satellite Station");
                    }
                    else if(source.contains("zss")){
                        tetSeedSource.setText("PhilRice - Zamboanga Satellite Station");
                    }
                    //wip
                    int quantity = spinnerMap.get(position).getQuantity() - spinnerMap.get(position).getUsedQuantity();
                    tetRiceProgram.setText(spinnerMap.get(position).getRiceProgram());
                    double area =(double) quantity/40;

                    tetAreaPlanted.setText(String.valueOf(area));
                    tetSeedQuantity.setText(String.valueOf(quantity));
                }

            }
        });
        /*tetDatePlanted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                tetDatePlanted.setText(materialDatePicker.getHeaderText());
            }
        });*/
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(actVariety.getText().toString().equals("")){
                    Toast.makeText(SeedProductionDetailActivity.this, "Please select Seed Variety.", Toast.LENGTH_SHORT).show();
                }else{
                    saveForm();
                }
            }
        });
        //open datepicker
        /*etDatePlanted.setOnClickListener(new View.OnClickListener(){
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
                "Select Seed Class",
                "Foundation",
                "Registered"
        };

        String[] commitmentPrograms = new String[]{
                "Select Rice Program",
                "National Rice Program",
                "RCEF",
                "None"
        };

        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<>(
          this, R.layout.spinner_rice_program,commitmentPrograms
        );
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(
                this,R.layout.spinner_seed_class,classes
        );

        spinnerArrayAdapter3.setDropDownViewResource(R.layout.spinner_rice_program);
        commitmentSpinner.setAdapter(spinnerArrayAdapter3);

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
        //cancel Button
        tvCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ;*/


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

                btnSave.setEnabled(true);
                btnSave.setBackgroundColor(Color.parseColor("#0070e8"));
                //tvSave.setEnabled(true);
                //tvSave.setTextColor(Color.WHITE);
            }

        }
    };

    private TextWatcher quantityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String quantity = tetSeedQuantity.getText().toString().trim();
            int boughtQuantity = 0; //selectedSeed.getQuantity() - selectedSeed.getUsedQuantity();
            if(quantity.isEmpty() || quantity.equals("0") ){
                tetAreaPlanted.setText("");
            }else{
                if(selectedSeed != null){
                    boughtQuantity = selectedSeed.getQuantity() - selectedSeed.getUsedQuantity();

                    if(Integer.parseInt(quantity) > boughtQuantity){
                        Toast.makeText(SeedProductionDetailActivity.this, "you cannot insert more than "+boughtQuantity, Toast.LENGTH_SHORT).show();
                        double area =(double) boughtQuantity/40;
                        tetSeedQuantity.setText(String.valueOf(boughtQuantity));
                        tetAreaPlanted.setText(String.valueOf(area));
                    }
                }
                else{
                    double area =(double) Integer.parseInt(quantity)/40;
                    tetAreaPlanted.setText(String.valueOf(area));
                }

            }
        }
    };
    public void saveForm() {
        Date date = null;
        String dateplanted;

        //SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
        //SimpleDateFormat otherFormat = new SimpleDateFormat("MMM d, yyyy");
        Log.e(TAG, "saveForm: "+ tetDatePlanted.getText().toString() );
        try {
            date = (Date) sdf.parse(tetDatePlanted.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "error parsing date");
        }

        /*try {
            date = (Date) otherFormat.parse(tetDatePlanted.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "error parsing date");
        }*/

        SimpleDateFormat timestamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
        //Log.e(TAG, "onCreate: "+format);
        Log.e(TAG, "date: "+ date );

        if(date == null){
            dateplanted = "0000-00-00";
        }else {
            dateplanted = newDate.format(date);// here is your new date !
        }

        String uniqueid = md5(uniqueId+"-"+ getMacAddr()+"-"+ timestamp);
        String accredno = user.getSerialNum();
        String latitude = tvLatitude.getText().toString();
        String longitude = tvLongitude.getText().toString();
        String seedVariety = "";
        String seedSource = "";
        String otherSeedSource = "";
        String seedClass = "";
        String riceProgram = "";

        if(actVariety.getText().toString().equals("Others")){
            Log.e(TAG, "if variety equal to other: " );
            seedVariety = actOtherVariety.getText().toString();
            seedSource = actSeedSource.getText().toString();
            otherSeedSource = actVariety.getText().toString();
            seedClass = actSeedClass.getText().toString();
            riceProgram = actRiceProgram.getText().toString();
        }else{
            Log.e(TAG, "if variety is not equal to other: " );
            seedVariety = actVariety.getText().toString();
            seedSource = tetSeedSource.getText().toString();
            otherSeedSource = "";
            seedClass = tetSeedClass.getText().toString();
            riceProgram = tetRiceProgram.getText().toString();
        }
        String areaPlanted = tetAreaPlanted.getText().toString();
        String seedQuantity = tetSeedQuantity.getText().toString();
        String seedbedArea = tetSeedBedArea.getText().toString();
        String seedlingAge = tetSeedlingAge.getText().toString();
        String seedLot = tetSeedLotNo.getText().toString();
        String controlNo = tetLabNo.getText().toString();
        String barangay = tetBarangay.getText().toString();
        String coop = tetCoop.getText().toString();
        String datecollected = newDate.format(new Date());
        Boolean isSent = false;
        String previousCrop = tetPreviousCrop.getText().toString();
        String previousVariety = actPreviousVariety.getText().toString();

        Log.e(TAG, "variety: "+seedVariety );
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        SeedGrower seedGrower = new SeedGrower(uniqueid,accredno,latitude,longitude,seedVariety,seedSource,otherSeedSource,seedClass,dateplanted,
                areaPlanted,seedQuantity,seedbedArea,seedlingAge,seedLot,controlNo,barangay,datecollected,isSent,riceProgram,coop,previousCrop,previousVariety);
        if(selectedSeed != null){
            seedGrower.setBought_id(String.valueOf(selectedSeed.getId()));
            int usedQuantity = Integer.parseInt(seedQuantity) + selectedSeed.getUsedQuantity();
            seedBoughtViewModel.getUpdateUsedQuantity(user.getSerialNum(),usedQuantity, selectedSeed.getId());
        }

        seedGrowerViewModel.insert(seedGrower);

        finish();
        /*Date date = null;
        String dateplanted;
        try {
            date = (Date) sdf.parse(etDatePlanted.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timestamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = timestamp.format(new Date());
        Log.e(TAG, "onCreate: "+format);

        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");//set format of new date

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        //String timestamp = dateFormat.format(calendar.getTime());

        String uniqueid = md5(uniqueId+"-"+ getMacAddr()+"-"+ timestamp);
        String accredno = tvAccredNo.getText().toString();
        String latitude = tvLatitude.getText().toString();
        String longitude = tvLongitude.getText().toString();
        String seedVariety = varietyspinner.getSelectedItem().toString();
        String seedSource = sourcespinner.getSelectedItem().toString();
        String otherSeedSource = etOtherSource.getText().toString();
        String seedClass = classspinner.getSelectedItem().toString();
        String riceProgram = commitmentSpinner.getSelectedItem().toString();


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
        String coop = etCoop.getText().toString();
        String datecollected = newDate.format(new Date());
        Boolean isSent = false;

        if(seedVariety.matches("Select Variety")){
            Toast.makeText(this, "Please select Seed variety.", Toast.LENGTH_SHORT).show();
        }else{
            seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        SeedGrower seedGrower = new SeedGrower(uniqueid,accredno,latitude,longitude,seedVariety,seedSource,otherSeedSource,seedClass,dateplanted,
                areaPlanted,seedQuantity,seedbedArea,seedlingAge,seedLot,controlNo,barangay,datecollected,isSent,riceProgram,coop);
        seedGrowerViewModel.insert(seedGrower);
        finish();
        }*/

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
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER ) ;
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
        intent = new Intent(SeedProductionDetailActivity.this, ScannerActivity.class);
        startActivityForResult(intent,REQUEST_CODE_EXAMPLE);
        /*if(ContextCompat.checkSelfPermission(SeedProductionDetailActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
        }*/
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if (requestCode == REQUEST_CODE_EXAMPLE) {

            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if (resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                final String result = data.getStringExtra(ScannerActivity.Scanned_value);

                // Use the data - in this case, display it in a Toast.
                tvAccredNo.setText(result);
            } else {
                // AnotherActivity was not successful. No data to retrieve.
            }
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
                    .setMessage("Allow this app to access location.")
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
                            mPermissionGranted = true;
                            mCodeScanner.startPreview();
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(SeedProductionDetailActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setCheckable(false);
        switch (item.getItemId()) {
            case R.id.homeBtn:
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                return true;
            case R.id.listBtn:
                intent = new Intent(this, ListActivity.class);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                return true;
            case R.id.addBtn:
                intent = new Intent(this, SeedProductionDetailActivity.class);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.sentItemBtn:
                intent = new Intent(this, SentItemActivity.class);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                return true;
            case R.id.aboutBtn:
                intent = new Intent(this, AboutActivity.class);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
