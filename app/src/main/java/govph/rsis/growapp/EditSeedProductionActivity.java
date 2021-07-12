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
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
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

import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtViewModel;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserViewModel;

import static android.graphics.Color.BLUE;

public class EditSeedProductionActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sepr", "Oct", "Nov", "Dec"};
    private static final String TAG = "EditSeedProduction";
    private int LOCATION_PERMISSION_CODE = 1;
    private int CAMERA_PERMISSION_CODE = 1;
    private boolean mPermissionGranted;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String uniqueId="";
    private SeedGrowerViewModel seedGrowerViewModel;
    private UserViewModel userViewModel;
    private SeedBoughtViewModel seedBoughtViewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    int activityId,countCollected,countSent,countDeleted;
    LocationManager locationManager;
    LocationListener locationListener;
    Toolbar toolbar;
    LinearLayout l10, l11,preFilledForm,hideLinear,linearOtherVariety;
    TextView tvLatitude, tvLongitude, tvCancel,tvSave,tvAccredNo,tvVersion,tvWelcomeName,tvWelcomeSerial,tvList,tvSent,tvDeleted,titleForm,backBtn,
            preDataClass,preDataProgram,preDataQuantity,preDataAreaPlanted,preDataSource;
    MenuItem mList,mSent,mDeleted;
    Button getLocationBtn,btnSave;
    AutoCompleteTextView actVariety,actOtherVariety,actSeedClass, actSeedSource,actRiceProgram,actPreviousVariety,actTransplantingMethod;
    TextInputLayout tilVariety,tilOtherVariety,tilSeedClass,tilSeedSource, tilRiceProgram,tilDatePlanted,til_other_seed_source,tilSeedClass2,tillSeedSource2,tilRiceProgram2,tilAreaPlanted,tilSeedQuantity,tilSeedBedArea,tilSeedlingAge,tilSeedLotNo,tilLabNo,tilCooperative,tilBarangay;
    ArrayAdapter<String> arrayAdapterVariety,arrayAdapterOtherVariety,arrayAdapterSeedClass,arrayAdapterSeedSource,arrayAdapterRiceProgram,arrayAdapterPreviousVariety,arrayAdapterTransplantingMethod;
    TextInputEditText tetSeedClass,tetSeedSource,tetRiceProgram,tetDatePlanted,tetAreaPlanted,tetSeedQuantity,tetSeedBedArea,tetSeedlingAge,tetSeedLotNo,tetLabNo,
            tetCoop,tetBarangay,tetPreviousCrop,tetOtherSeedSource;
    ArrayList arrayListVarieties,arrayListSeedClass,arrayListSeedSource,arrayListRiceProgram,arrayListTransplantingMethod,arrayListVarieties1;
    FrameLayout frameLayout;
    ScrollView scrollView;

    MaterialDatePicker.Builder materialBuilder;
    MaterialDatePicker materialDatePicker;
    CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    SeedGrowerDatabase database;
    Intent intent;
    TelephonyManager telephonyManager;
    SeedGrower seedGrowers;
    User user;
    int id;
    View headerView;
    List<SeedBought> seedBought;
    SeedBought selectedSeed;
    HashMap<Integer, SeedBought> spinnerMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_production_detail);

        activityId = getIntent().getIntExtra("activityId",0);


        //scannerView = findViewById(R.id.scanner_view);
        //mCodeScanner = new CodeScanner(EditSeedProductionActivity.this, scannerView);
        database = SeedGrowerDatabase.getInstance(this);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        seedBoughtViewModel = ViewModelProviders.of(this).get(SeedBoughtViewModel.class);
        user = userViewModel.getLoggedInUser();
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());
        intent = getIntent();
        String formId = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);

        id = Integer.parseInt(formId);
        seedGrowers = database.seedGrowerDao().findFormById(formId);
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        toolbar = findViewById(R.id.add_toolbar);

        //spTvWelcomeName.setText("Welcome, "+ user.getFullname());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Editing Form");

        drawerLayout = findViewById(R.id.add_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvVersion = headerView.findViewById(R.id.headerVersion);
        tvWelcomeName = (TextView) headerView.findViewById(R.id.headerName);
        tvWelcomeSerial = (TextView) headerView.findViewById(R.id.headerSerial);
        titleForm = findViewById(R.id.titleForm);
        titleForm.setText("Edit Form");
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
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.listBtn);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText("Version: "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvWelcomeName.setText(user.getFullname());
        tvWelcomeSerial.setText(user.getSerialNum());

        //find the view of linear layout
        l10 = (LinearLayout) findViewById(R.id.l10);
        l11 = (LinearLayout) findViewById(R.id.l11);
        //preFilledForm = (LinearLayout) findViewById(R.id.preFilledForm);
        //hideLinear = (LinearLayout) findViewById(R.id.hideLinear);
        linearOtherVariety = (LinearLayout) findViewById(R.id.linearOtherVariety);

        getLocationBtn = (Button) findViewById(R.id.getLocationBtn);
        getLocationBtn.setClickable(false);
        getLocationBtn.setVisibility(View.GONE);
        getLocationBtn.setBackgroundColor(Color.LTGRAY);
        //find view of text views
        //spTvWelcomeName = (TextView) findViewById(R.id.spTvWelcomeName);
        //spTvWelcomeName.setText("Welcome, "+ user.getFullname());
        //tvCancel = (TextView) findViewById(R.id.tvCancel);
        btnSave = findViewById(R.id.btnSave);
        backBtn = findViewById(R.id.backBtn);
        tvAccredNo = (TextView) findViewById(R.id.tvAccreditationNo);
        tvAccredNo.setText(user.getSerialNum());
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);

        tilVariety = (TextInputLayout) findViewById(R.id.tilVariety);
        tilSeedClass = (TextInputLayout) findViewById(R.id.tilSeedClass);
        tilSeedSource = (TextInputLayout) findViewById(R.id.tilSeedSource);
        tilRiceProgram = (TextInputLayout) findViewById(R.id.tilRiceProgram);
        tilDatePlanted = (TextInputLayout) findViewById(R.id.tilDatePlanted);
        tilOtherVariety = (TextInputLayout) findViewById(R.id.tilOtherVariety);
        til_other_seed_source = findViewById(R.id.til_other_seed_source);

        actSeedClass = (AutoCompleteTextView) findViewById(R.id.actSeedClass);
        actVariety = (AutoCompleteTextView) findViewById(R.id.actVariety);
        actSeedSource = (AutoCompleteTextView) findViewById(R.id.actSeedSource);
        actRiceProgram = (AutoCompleteTextView) findViewById(R.id.actRiceProgram);
        actPreviousVariety = (AutoCompleteTextView) findViewById(R.id.actPreviousVariety);
        actOtherVariety = (AutoCompleteTextView) findViewById(R.id.actOtherVariety);
        actTransplantingMethod = findViewById(R.id.actTransplantingMethod);

        preDataAreaPlanted = findViewById(R.id.preDataAreaPlanted);
        preDataClass = findViewById(R.id.preDataClass);
        preDataProgram = findViewById(R.id.preDataProgram);
        preDataQuantity = findViewById(R.id.preDataQuantity);
        preDataSource = findViewById(R.id.preDataSource);

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
        tetOtherSeedSource = findViewById(R.id.tetOtherSeedSource);

        materialBuilder = MaterialDatePicker.Builder.datePicker();
        materialBuilder.setTitleText("Select Date Planted");
        materialDatePicker = materialBuilder.build();

        database = SeedGrowerDatabase.getInstance(this);

        tvAccredNo.addTextChangedListener(saveTextWatcher);
        tvLatitude.addTextChangedListener(saveTextWatcher);
        tvLongitude.addTextChangedListener(saveTextWatcher);
        if(ContextCompat.checkSelfPermission(EditSeedProductionActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                uniqueId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                uniqueId = telephonyManager.getDeviceId();
            }


        }

        /*seedBought = seedBoughtViewModel.getAllSeedBought(user.getSerialNum(),"ces");
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
        actVariety.setThreshold(1);*/

        //Searchable Spinners here
        //Populate the ArrayListVarieties;
        /*List<Seeds> seeds = database.seedsDao().getSeeds();
        ArrayList arrayListOtherVariety = new ArrayList<>();
        for(Seeds s : seeds){
            arrayListOtherVariety.add(s.getVariety());
        }
        //set layout of the variety
        arrayAdapterOtherVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_variety,arrayListOtherVariety);
        arrayAdapterPreviousVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_variety,arrayListOtherVariety);
        actOtherVariety.setAdapter(arrayAdapterOtherVariety);
        actOtherVariety.setThreshold(1);
        actPreviousVariety.setAdapter(arrayAdapterPreviousVariety);
        actPreviousVariety.setThreshold(1);*/
        List<Seeds> seeds1 = database.seedsDao().getSeeds();
        arrayListVarieties1 = new ArrayList<>();
        for(Seeds s : seeds1){
            String seed = s.getVariety().replaceAll("\\s","").toLowerCase();
            arrayListVarieties.add(s.getVariety());
        }

        arrayAdapterPreviousVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_variety,arrayListVarieties1);
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

        arrayListTransplantingMethod = new ArrayList<>();
        arrayListTransplantingMethod.add("Manual Transplanting");
        arrayListTransplantingMethod.add("Mechanical Transplanting");

        arrayAdapterTransplantingMethod = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_rice_program,arrayListTransplantingMethod);
        actTransplantingMethod.setAdapter(arrayAdapterTransplantingMethod);

        //set layout of the riceProgram
        arrayAdapterRiceProgram = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_rice_program,arrayListRiceProgram);
        actRiceProgram.setAdapter(arrayAdapterRiceProgram);
        actRiceProgram.setThreshold(1);

        //set the data to text fields generated from database
        /*if(seedGrowers.getOtherseedsource().equals("Others")){

            //preFilledForm.setVisibility(View.GONE);
            //hideLinear.setVisibility(View.VISIBLE);
            linearOtherVariety.setVisibility(View.VISIBLE);

            actVariety.setText(seedGrowers.getOtherseedsource());
            actOtherVariety.setText(seedGrowers.getVariety());
            actRiceProgram.setText(seedGrowers.getRiceProgram());
            actSeedClass.setText(seedGrowers.getSeedclass());
            actSeedSource.setText(seedGrowers.getSeedsource());
        }
        else{
            //preFilledForm.setVisibility(View.VISIBLE);
            //hideLinear.setVisibility(View.GONE);
            linearOtherVariety.setVisibility(View.GONE);

            actVariety.setText(seedGrowers.getVariety());
            preDataProgram.setText(seedGrowers.getRiceProgram());
            preDataClass.setText(seedGrowers.getSeedclass());
            preDataSource.setText(seedGrowers.getSeedsource());
        }*/

        if(seedGrowers.getSeedsource().equals("Others")){
            til_other_seed_source.setVisibility(View.VISIBLE);
            tetOtherSeedSource.setText(seedGrowers.getOtherseedsource());

            List<Seeds> seeds = database.seedsDao().getSeeds();
            arrayListVarieties = new ArrayList<>();
            for(Seeds s : seeds){
                String seed = s.getVariety().replaceAll("\\s","").toLowerCase();
                arrayListVarieties.add(s.getVariety());
            }
            //set layout of the variety
            arrayAdapterOtherVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_source,arrayListVarieties);
            actVariety.setAdapter(arrayAdapterOtherVariety);
            actVariety.setThreshold(2);
            til_other_seed_source.setVisibility(View.VISIBLE);
        }

        actSeedSource.setText(seedGrowers.getSeedsource(),false);
        actVariety.setText(seedGrowers.getVariety(),false);
        actRiceProgram.setText(seedGrowers.getRiceProgram(),false);
        actSeedClass.setText(seedGrowers.getSeedclass(),false);
        actTransplantingMethod.setText(seedGrowers.getTransplanting_method(),false);

        tvLongitude.setText(seedGrowers.getLongitude());
        tvLatitude.setText(seedGrowers.getLatitude());

        actPreviousVariety.setText(seedGrowers.getPreviousVariety(),false);
        tetAreaPlanted.setText(seedGrowers.getAreaplanted());
        tetBarangay.setText(seedGrowers.getBarangay());
        tetCoop.setText(seedGrowers.getCoop());
        tetLabNo.setText(seedGrowers.getControlno());
        tetSeedBedArea.setText(seedGrowers.getSeedbedarea());
        tetSeedlingAge.setText(seedGrowers.getSeedlingage());
        tetSeedLotNo.setText(seedGrowers.getSeedlot());
        tetSeedQuantity.setText(seedGrowers.getQuantity());
        tetPreviousCrop.setText(seedGrowers.getPreviousCrop());
        if(!tvAccredNo.getText().toString().trim().isEmpty() && !tvLatitude.getText().toString().trim().isEmpty() && !tvLongitude.getText().toString().trim().isEmpty()){
            btnSave.setEnabled(true);
            btnSave.setBackgroundColor(Color.parseColor("#0070e8"));
        }

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");//set format of date you receive from db
        Date date = null;
        try {
            date = (Date) sdf.parse(seedGrowers.getDateplanted());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("d MMM yyyy");
        tetDatePlanted.setText(newDate.format(date));
        //button to cancel the activity
        /*tvCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        //set the Save text clickable
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tetDatePlanted.setOnClickListener(new View.OnClickListener() {
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
                String date = dayOfMonth +" "+MONTHS[month] +" "+year;
                tetDatePlanted.setText(date);
            }
        };

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
                    Toast.makeText(EditSeedProductionActivity.this, "Please select Seed Variety.", Toast.LENGTH_SHORT).show();
                }else{
                    UpdateFunction();
                }
            }
        });

        actSeedSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actVariety.clearListSelection();
                actVariety.setText("");
                actSeedClass.setText("");
                actRiceProgram.setText("");
                if(arrayListSeedSource.get(position).equals("Others")){
                    List<Seeds> seeds = database.seedsDao().getSeeds();
                    arrayListVarieties = new ArrayList<>();
                    for(Seeds s : seeds){
                        String seed = s.getVariety().replaceAll("\\s","").toLowerCase();
                        arrayListVarieties.add(s.getVariety());
                    }
                    //set layout of the variety
                    arrayAdapterOtherVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_source,arrayListVarieties);
                    actVariety.setAdapter(arrayAdapterOtherVariety);
                    actVariety.setThreshold(2);
                    til_other_seed_source.setVisibility(View.VISIBLE);

                }else{
                    String station_name = checkStationName(arrayListSeedSource.get(position).toString());

                    seedBought = seedBoughtViewModel.getAllSeedBought(user.getSerialNum(),station_name);
                    final String[] spinnerArraySeeds = new String[seedBought.size()];
                    spinnerMap = new HashMap<Integer, SeedBought>();
                    arrayListVarieties = new ArrayList<>();
                    for (int i = 0; i< seedBought.size(); i++){
                        spinnerMap.put(i,seedBought.get(i));
                        spinnerArraySeeds[i] = seedBought.get(i).getVariety();
                    }
                    arrayAdapterVariety = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_seed_variety,spinnerArraySeeds);
                    actVariety.setAdapter(arrayAdapterVariety);
                    actVariety.setThreshold(2);
                    til_other_seed_source.setVisibility(View.GONE);
                }
            }
        });

        actVariety.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actSeedClass.setText("");
                if(actSeedSource.getText().toString().equals("Others")){
                    //preFilledForm.setVisibility(View.GONE);
                    //hideLinear.setVisibility(View.VISIBLE);
                    //linearOtherVariety.setVisibility(View.VISIBLE);
                    selectedSeed = null;
                }else{
                    //preFilledForm.setVisibility(View.VISIBLE);
                    //hideLinear.setVisibility(View.GONE);
                    //linearOtherVariety.setVisibility(View.GONE);
                    selectedSeed = spinnerMap.get(position);
                    Log.e(TAG, "variety: "+selectedSeed.getVariety() );
                    actRiceProgram.setText(selectedSeed.getRiceProgram(),false);
                    if(selectedSeed.getSeedClass().equals("RS")){
                        actSeedClass.setText("Registered",false);
                        //preDataClass.setText("Registered");
                    }
                    if(selectedSeed.getSeedClass().equals("FS")){
                        actSeedClass.setText("Registered",false);
                        //preDataClass.setText("Foundation");
                    }

                    /*int quantity = spinnerMap.get(position).getQuantity() - spinnerMap.get(position).getUsedQuantity();
                    preDataProgram.setText(spinnerMap.get(position).getRiceProgram());
                    double area =(double) quantity/40;

                    preDataAreaPlanted.setText(String.valueOf(area)+"ha");
                    preDataQuantity.setText(String.valueOf(quantity)+"kg(s)");*/
                }

            }
        });

        /*actVariety.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(spinnerArraySeeds[position].equals("Others")){
                    //preFilledForm.setVisibility(View.GONE);
                    //hideLinear.setVisibility(View.VISIBLE);
                    //linearOtherVariety.setVisibility(View.VISIBLE);
                    selectedSeed = null;
                }else{
                    //preFilledForm.setVisibility(View.VISIBLE);
                    //hideLinear.setVisibility(View.GONE);
                    linearOtherVariety.setVisibility(View.GONE);

                    selectedSeed = spinnerMap.get(position);
                    String source = spinnerMap.get(position).getTableName().toLowerCase();
                    Log.e(TAG, "onItemClick: "+selectedSeed );
                    //Toast.makeText(SeedProductionDetailActivity.this, "quantity" + spinnerMap.get(position).getQuantity(), Toast.LENGTH_SHORT).show();
                    if(spinnerMap.get(position).getSeedClass().equals("RS")){
                        preDataClass.setText("Registered");
                    }else if(spinnerMap.get(position).getSeedClass().equals("FS")){
                        preDataClass.setText("Foundation");
                    }

                    if(source.contains("ces")){
                        preDataSource.setText("PhilRice - Science City of Muñoz");
                    }
                    else if(source.contains("bes")){
                        preDataSource.setText("PhilRice - Batac");
                    }
                    else if(source.contains("mes")){
                        preDataSource.setText("PhilRice - Midsayap");
                    }
                    else if(source.contains("lbs")){
                        preDataSource.setText("PhilRice - Los Baños");
                    }
                    else if(source.contains("aes")){
                        preDataSource.setText("PhilRice - Agusan");
                    }
                    else if(source.contains("cves")){
                        preDataSource.setText("PhilRice - Isabela");
                    }
                    else if(source.contains("prn")){
                        preDataSource.setText("PhilRice - Negros");
                    }
                    else if(source.contains("bies")){
                        preDataSource.setText("PhilRice - Bicol");
                    }
                    else if(source.contains("cmu")){
                        preDataSource.setText("PhilRice - CMU Satellite Station");
                    }
                    else if(source.contains("zss")){
                        preDataSource.setText("PhilRice - Zamboanga Satellite Station");
                    }
                    //wip
                    int quantity = spinnerMap.get(position).getQuantity() - spinnerMap.get(position).getUsedQuantity();
                    preDataProgram.setText(spinnerMap.get(position).getRiceProgram());
                    double area =(double) quantity/40;

                    preDataAreaPlanted.setText(String.valueOf(area)+"ha");
                    preDataQuantity.setText(String.valueOf(quantity)+"kg(s)");
                }

            }
        });*/
        //find view of buttons
        /*scanBtn = (Button) findViewById(R.id.scanBtn);
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
        etCoop = (EditText) findViewById(R.id.etCoop);
        //find view of all  Spinner widget
        varietyspinner = (Spinner) findViewById(R.id.varietyPlantedSpinner);
        sourcespinner = (Spinner) findViewById(R.id.seedSourceSpinner);
        classspinner = (Spinner) findViewById(R.id.seedClassSpinner);
        commitmentSpinner = (Spinner) findViewById(R.id.commitmentSpinner);
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
        etCoop.setText(seedGrowers.getCoop());

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
        *//*scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFunction();
            }
        });*//*

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
        *//*getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });*//*


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

        String[] commitmentPrograms = new String[]{
                "Select Rice Program",
                "National Rice Program",
                "RCEF",
                "None"
        };

        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<>(
                this, R.layout.spinner_rice_program,commitmentPrograms
        );
        //spinner for seed  class
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(
                this, R.layout.spinner_seed_class, classes
        );
        spinnerArrayAdapter3.setDropDownViewResource(R.layout.spinner_rice_program);
        commitmentSpinner.setAdapter(spinnerArrayAdapter3);
        commitmentSpinner.setSelection(spinnerArrayAdapter3.getPosition(seedGrowers.getRiceProgram()));

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
        });*/
    }

    public void UpdateFunction(){
        Date date = null;
        String dateplanted;
        //SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
        //SimpleDateFormat otherFormat = new SimpleDateFormat("MMM d, yyyy");
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
        String seedVariety = actVariety.getText().toString().trim();
        String seedSource = actSeedSource.getText().toString().trim();
        String otherSeedSource = tetOtherSeedSource.getText().toString().trim();
        String riceProgram = actRiceProgram.getText().toString().trim();
        String seedClass = actSeedClass.getText().toString().trim();
        /*String seedVariety = "";
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
            seedSource = preDataSource.getText().toString();
            otherSeedSource = "";
            seedClass = preDataClass.getText().toString();
            riceProgram = preDataProgram.getText().toString();
        }*/
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
        String transplanting_method = actTransplantingMethod.getText().toString().trim();

            seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
            SeedGrower seedGrower = new SeedGrower(uniqueid,accredno,latitude,longitude,seedVariety,seedSource,otherSeedSource,seedClass,dateplanted,
                    areaPlanted,seedQuantity,seedbedArea,seedlingAge,seedLot,controlNo,barangay,datecollected,isSent,riceProgram,coop,previousCrop,previousVariety,transplanting_method);
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
                //tvSave.setEnabled(true);
                //tvSave.setTextColor(Color.BLACK);
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
                finish();
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
            case R.id.logoutBtn:
                logout();
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

    public void logout(){
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        user.setLoggedIn("LoggedOut");
        userViewModel.update(user);

        if(userViewModel.getCheckLoggedIn() > 0){
            intent = new Intent(EditSeedProductionActivity.this,SwitchAccountActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public String checkStationName(String station_name){
        switch (station_name){
            case "PhilRice CES - Muñoz":
                return "ces";
            case "PhilRice - Batac":
                return "bes";
            case "PhilRice - Midsayap":
                return "mes";
            case "PhilRice - Los Baños":
                return "lbs";
            case "PhilRice - Agusan":
                return "aes";
            case "PhilRice - Isabela":
                return "cves";
            case "PhilRice - Negros":
                return "prn";
            case "PhilRice - Bicol":
                return "bies";
            case "PhilRice - CMU":
                return "cmu";
            case "PhilRice - Zamboanga":
                return "zss";
        }
        return null;
    }
}
