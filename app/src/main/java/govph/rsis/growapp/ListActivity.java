package govph.rsis.growapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtViewModel;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserViewModel;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.releasingapp.MESSAGE";
    public static final String EXTRA_ARRAY = "com.example.releasingapp.EXTRA_ARRAY";
    private static final String TAG = "ListActivity";

    private SeedGrowerViewModel seedGrowerViewModel;
    private SeedBoughtViewModel seedBoughtViewModel;
    private UserViewModel userViewModel;
    private int CAMERA_PERMISSION_CODE = 1;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean mPermissionGranted;
    public static final int REQUEST_CODE = 0x9988;
     ;
    TextView tvDeleteAll,tvEmptyView,tvVersion,tvFinalizeAccredNo,titleFinalize,tvFinalizeSeedSource,tvFinalizeVariety,tvFinalizeSeedClass,tvFinalizeDatePlanted,
            tvFinalizeAreaPlanted, tvFinalizeQuantity,tvFinalizeSeedbedArea,tvFinalizeSeedlingAge,tvFinalizeSeedLot,tvFinalizeLabNo,tvFinalizeBarangay,tvFinalizeLatitude,tvFinalizeLongitude,
            tvFinalizeCoop,tvFinalizeProgram,tvWelcomeName,tvWelcomeSerial,tvCollected,listTvVersion,tvList,tvSent,tvDeleted,listTvCollected,listBtnAdd;
    MenuItem mList,mSent,mDeleted;
    Toolbar toolbar;
    Intent intent;
    SeedGrowerDatabase database;
    RecyclerView rvSeedGrowers;
    GlobalFunction globalFunction;
    RequestQueue queue;
    View headerView;
    User user;
    int activityId,countCollected,countSent,countDeleted;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        globalFunction = new GlobalFunction(getApplicationContext());
        globalFunction.isOnline();
        requestCameraPermission();
        activityId = getIntent().getIntExtra("activityId",0);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        seedBoughtViewModel = ViewModelProviders.of(this).get(SeedBoughtViewModel.class);

        user = userViewModel.getLoggedInUser();
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());
        database = SeedGrowerDatabase.getInstance(this);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.list_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        mList = navigationView.getMenu().findItem(R.id.listBtn);
        mSent = navigationView.getMenu().findItem(R.id.sentItemBtn);
        mDeleted = navigationView.getMenu().findItem(R.id.deletedBtn);

        /*Populate the navigation drawer badge*/
        tvList = (TextView) mList.getActionView();
        tvSent = (TextView) mSent.getActionView();
        tvDeleted = (TextView) mDeleted.getActionView();

        tvList.setText(String.valueOf(countCollected));
        tvSent.setText(String.valueOf(countSent));
        tvDeleted.setText(String.valueOf(countDeleted));
        /*end badge*/

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Collected List: " +String.valueOf(countCollected));
        tvVersion = headerView.findViewById(R.id.headerVersion);
        tvWelcomeName = (TextView) headerView.findViewById(R.id.headerName);
        tvWelcomeSerial = (TextView) headerView.findViewById(R.id.headerSerial);
        listTvVersion = findViewById(R.id.listTvVersion);
        listTvCollected = findViewById(R.id.listTvCollected);
        listTvCollected.setText("Collected List: " +String.valueOf(countCollected));
        //tvDeleteAll = (TextView) findViewById(R.id.tvDeleteAll);
        listBtnAdd = (TextView) findViewById(R.id.listBtnAdd);
        rvSeedGrowers = (RecyclerView) findViewById(R.id.recyclerView1);
        tvEmptyView = findViewById(R.id.empty_view);
        rvSeedGrowers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSeedGrowers.setHasFixedSize(true);

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
            listTvVersion.setText("Version: "+pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvWelcomeName.setText(user.getFullname());
        tvWelcomeSerial.setText(user.getSerialNum());

        int checkUser = database.userDao().isEmpty();

        /*if(checkUser < 1){
            LayoutInflater inflater = getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
            View dialogView = inflater.inflate(R.layout.scan_dialog,null);
            builder.setCancelable(false);
            builder.setView(dialogView);
            MaterialButton scanBtn = (MaterialButton) dialogView.findViewById(R.id.dialogBtnScan);
            final AlertDialog progressDialog = builder.create();
            progressDialog.show();

            scanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(ListActivity.this, ScannerActivity.class);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            });
        }*/

        final SeedGrowerAdapter adapter = new SeedGrowerAdapter();
        rvSeedGrowers.setAdapter(adapter);
        //adapter.setSgClickedListener(this);
        //adapter.setSgEditClickedListener(this);
        listBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ListActivity.this,SeedProductionDetailActivity.class);
                startActivity(intent);
            }
        });

        seedGrowerViewModel.getAllSeedGrowers(user.getSerialNum()).observe(this, new Observer<List<SeedGrower>>() {
            @Override
            public void onChanged(List<SeedGrower> seedGrowers) {
                //update recyclerview
                countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
                //tvCollected.setText("Collected: "+countCollected);
                if(seedGrowers.isEmpty()){
                    tvEmptyView.setVisibility(View.VISIBLE);
                    rvSeedGrowers.setVisibility(View.GONE);
                    //tvDeleteAll.setClickable(false);
                    //tvDeleteAll.setTextColor(Color.GRAY);
                }else{
                    rvSeedGrowers.setVisibility(View.VISIBLE);
                    tvEmptyView.setVisibility(View.GONE);
                    adapter.setSeedGrowers(seedGrowers);
                    //tvDeleteAll.setClickable(true);
                    //tvDeleteAll.setTextColor(Color.WHITE);
                }


            }
        });

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final @NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(ListActivity.this )
                        .setTitle("Delete Form?")
                        .setMessage( "Are you sure you want to delete this?" )
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SeedGrower seedGrower;
                                SeedBought seedBought;
                                seedGrower = adapter.getSeedGrowerAt(viewHolder.getAdapterPosition());
                                String bought_id = null;
                                Log.e(TAG, "onClick: "+seedGrower.getBought_id() );
                                if(!seedGrower.getBought_id().isEmpty()){
                                    bought_id =seedGrower.getBought_id();
                                }

                                seedBought = seedBoughtViewModel.seedBought(Integer.parseInt(bought_id));
                                int quantity =seedBought.getUsedQuantity() - Integer.parseInt(seedGrower.getQuantity());

                                int updatedQuantity =seedBoughtViewModel.getReturnQuantity(seedGrower.getAccredno(),Integer.parseInt(bought_id),quantity);

                                if(updatedQuantity > 0){
                                    seedGrowerViewModel.softDelete(seedGrower.getAccredno(),2,seedGrower.getSgId());
                                    //seedGrowerViewModel.delete(adapter.getSeedGrowerAt(viewHolder.getAdapterPosition()));
                                    Toast.makeText(ListActivity.this, "Form deleted.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ListActivity.this, "Error deleting form.", Toast.LENGTH_SHORT).show();
                                }


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.notifyDataSetChanged();
                            }
                        }).show();

            }
        }).attachToRecyclerView(rvSeedGrowers)*/;

        //This will delete all the forms from the phone database
        /*tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ListActivity.this)
                        .setTitle("Are you sure you want to delete all forms?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                seedGrowerViewModel.deleteAllSeedGrower();
                                Toast.makeText(ListActivity.this, "Deleted All Forms.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No",null).show();

            }
        });*/

        adapter.setSgFormClickedListener(new SeedGrowerAdapter.sgFormClicked() {
            @Override
            public void editSGDetails(SeedGrower seedGrower) {
                intent = new Intent(ListActivity.this,EditSeedProductionActivity.class);

                intent.putExtra(EXTRA_MESSAGE, Integer.toString(seedGrower.getSgId()));
                startActivity(intent);
            }
        });

        adapter.setRemoveBtnClickedListener(new SeedGrowerAdapter.removeBtnClicked() {
            @Override
            public void removeSeedGrower(final SeedGrower seedGrower) {
                new AlertDialog.Builder(ListActivity.this )
                        .setTitle("Delete Form?")
                        .setMessage( "Are you sure you want to delete this?" )
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SeedBought seedBought;
                                String bought_id = null;
                                Log.e(TAG, "onClick: "+seedGrower.getBought_id() );
                                if(seedGrower.getBought_id() != null){
                                    bought_id =seedGrower.getBought_id();
                                    seedBought = seedBoughtViewModel.seedBought(Integer.parseInt(bought_id));
                                    int quantity =seedBought.getUsedQuantity() - Integer.parseInt(seedGrower.getQuantity());

                                    int updatedQuantity =seedBoughtViewModel.getReturnQuantity(seedGrower.getAccredno(),Integer.parseInt(bought_id),quantity);
                                    if(updatedQuantity > 0){
                                        //seedGrowerViewModel.delete(adapter.getSeedGrowerAt(viewHolder.getAdapterPosition()));
                                        seedGrowerViewModel.softDelete(seedGrower.getAccredno(),2,seedGrower.getSgId());
                                        Toast.makeText(ListActivity.this, "Form deleted.", Toast.LENGTH_SHORT).show();
                                        updateCounter();

                                    }else{
                                        Toast.makeText(ListActivity.this, "Error deleting form.", Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    seedGrowerViewModel.softDelete(seedGrower.getAccredno(),2,seedGrower.getSgId());
                                    updateCounter();
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
        //display all the fields before sending to server
        adapter.setFinalizedBtnClickedListener(new SeedGrowerAdapter.finalizedBtnClicked() {
            @Override
            public void finalizeBeforeSending(final SeedGrower seedGrower) {
                LayoutInflater inflater = getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                View dialogView = inflater.inflate(R.layout.finalize_layout,null);
                builder.setCancelable(true);
                builder.setView(dialogView);

                titleFinalize = (TextView) dialogView.findViewById(R.id.titleFinalize);
                tvFinalizeAccredNo = (TextView) dialogView.findViewById(R.id.tvFinalizeAccredNo);
                tvFinalizeSeedSource = (TextView) dialogView.findViewById(R.id.tvFinalizeSeedSource);
                tvFinalizeVariety = (TextView) dialogView.findViewById(R.id.tvFinalizeVariety);
                tvFinalizeSeedClass = (TextView) dialogView.findViewById(R.id.tvFinalizeSeedClass);
                tvFinalizeDatePlanted = (TextView) dialogView.findViewById(R.id.tvFinalizeDatePlanted);
                tvFinalizeAreaPlanted = (TextView) dialogView.findViewById(R.id.tvFinalizeAreaPlanted);
                tvFinalizeQuantity = (TextView) dialogView.findViewById(R.id.tvFinalizeQuantity);
                tvFinalizeSeedbedArea = (TextView) dialogView.findViewById(R.id.tvFinalizeSeedbedArea);
                tvFinalizeSeedlingAge = (TextView) dialogView.findViewById(R.id.tvFinalizeSeedlingAge);
                tvFinalizeSeedLot = (TextView) dialogView.findViewById(R.id.tvFinalizeSeedLot);
                tvFinalizeLabNo = (TextView) dialogView.findViewById(R.id.tvFinalizeLabNo);
                tvFinalizeBarangay = (TextView) dialogView.findViewById(R.id.tvFinalizeBarangay);
                tvFinalizeLatitude = (TextView) dialogView.findViewById(R.id.tvFinalizeLatitude);
                tvFinalizeLongitude = (TextView) dialogView.findViewById(R.id.tvFinalizeLongitude);
                tvFinalizeCoop = (TextView) dialogView.findViewById(R.id.tvFinalizeCoop);
                tvFinalizeProgram =  (TextView) dialogView.findViewById(R.id.tvFinalizeProgram);

                titleFinalize.setText(seedGrower.getVariety());
                tvFinalizeAccredNo.setText(seedGrower.getAccredno());
                tvFinalizeSeedSource.setText(seedGrower.getSeedsource());
                tvFinalizeVariety.setText(seedGrower.getVariety());
                tvFinalizeSeedClass.setText(seedGrower.getSeedclass());
                tvFinalizeDatePlanted.setText(seedGrower.getDateplanted());
                tvFinalizeAreaPlanted.setText(seedGrower.getAreaplanted());
                tvFinalizeQuantity.setText(seedGrower.getQuantity());
                tvFinalizeSeedbedArea.setText(seedGrower.getSeedbedarea());
                tvFinalizeSeedlingAge.setText(seedGrower.getSeedlingage());
                tvFinalizeSeedLot.setText(seedGrower.getSeedlot());
                tvFinalizeLabNo.setText(seedGrower.getControlno());
                tvFinalizeBarangay.setText(seedGrower.getBarangay());
                tvFinalizeLatitude.setText(seedGrower.getLatitude());
                tvFinalizeLongitude.setText(seedGrower.getLongitude());
                tvFinalizeCoop.setText(seedGrower.getCoop());
                tvFinalizeProgram.setText(seedGrower.getRiceProgram());

                builder.setNegativeButton("Back", null);
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;
                        new AlertDialog.Builder(ListActivity.this )
                                .setTitle("Finish finalizing the form?")
                                .setMessage( "Please check the data before sending." )
                                .setPositiveButton( "Send" , new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                                LayoutInflater inflater = getLayoutInflater();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                                                View dialogView = inflater.inflate(R.layout.loading_template,null);
                                                builder.setCancelable(false);
                                                builder.setView(dialogView);

                                                final AlertDialog progressDialog = builder.create();
                                                progressDialog.show();

                                                if(DecVar.isNetworkConnected){

                                                    if(seedGrower.getVariety().matches("Select Variety") || seedGrower.getSeedsource().matches("Select Seed Source")
                                                            || seedGrower.getSeedclass().matches("Select Seed Class") || seedGrower.getDateplanted().matches("0002-11-30")
                                                            || seedGrower.getDateplanted().matches("-0001-11-30") || seedGrower.getDateplanted().matches("0000-00-00")
                                                            || seedGrower.getAreaplanted().matches("") || seedGrower.getQuantity().matches("")
                                                            || seedGrower.getSeedbedarea().matches("") || seedGrower.getSeedlingage().matches("")
                                                            || seedGrower.getSeedlot().matches("") || seedGrower.getControlno().matches("")
                                                            || seedGrower.getBarangay().matches("") || seedGrower.getRiceProgram().matches("Select Rice Program") || seedGrower.getCoop().matches("")){
                                                        progressDialog.hide();
                                                        new AlertDialog.Builder(ListActivity.this)
                                                                .setMessage("Please fill out all the fields first before sending data.")
                                                                .setNegativeButton("Try Again",null).show();
                                                    }else{
                                                        queue = Volley.newRequestQueue(ListActivity.this);
                                                        String url = DecVar.receiver()+"/postData.php";

                                                        StringRequest sr = new StringRequest(Request.Method.POST, url,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        if(!response.equals("Success")){
                                                                            progressDialog.hide();
                                                                            new AlertDialog.Builder(ListActivity.this)
                                                                                    .setMessage("Error while sending data to server.")
                                                                                    .setNegativeButton("Try Again",null).show();
                                                                        }
                                                                        else{
                                                                            progressDialog.hide();
                                                                            seedGrower.setIsSent(true);
                                                                            seedGrowerViewModel.update(seedGrower);
                                                                            new AlertDialog.Builder(ListActivity.this)
                                                                                    .setMessage("Successfully sent form data")
                                                                                    .setNegativeButton("Ok",null).show();
                                                                            dialog.dismiss();
                                                                            updateCounter();
                                                                        }
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        progressDialog.hide();
                                                                        new AlertDialog.Builder(ListActivity.this)
                                                                                .setMessage("Error while sending data to server.")
                                                                                .setNegativeButton("Try Again",null).show();
                                                                        Log.e("HttpClient", "error: " + error.toString());
                                                                    }
                                                                })
                                                        {
                                                            @Override
                                                            protected Map<String,String> getParams(){
                                                                Map<String,String> params = new HashMap<>();
                                                                params.put("formId","0");
                                                                params.put("accredNo","");
                                                                params.put("serial_number",seedGrower.getAccredno());
                                                                params.put("seedSource",seedGrower.getSeedsource());
                                                                params.put("otherSource","0");
                                                                params.put("variety",seedGrower.getVariety());
                                                                params.put("seedClass",seedGrower.getSeedclass());
                                                                params.put("datePlanted",seedGrower.getDateplanted());
                                                                params.put("areaPlanted",seedGrower.getAreaplanted());
                                                                params.put("quantity",seedGrower.getQuantity());
                                                                params.put("seedbedArea",seedGrower.getSeedbedarea());
                                                                params.put("seedlingAge",seedGrower.getSeedlingage());
                                                                params.put("seedlot",seedGrower.getSeedlot());
                                                                params.put("controlNo",seedGrower.getControlno());
                                                                params.put("barangay",seedGrower.getBarangay());
                                                                params.put("latitude",seedGrower.getLatitude());
                                                                params.put("longitude",seedGrower.getLongitude());
                                                                params.put("dateCollected",seedGrower.getDatecollected());
                                                                params.put("androidid",seedGrower.getMacaddress());
                                                                params.put("program",seedGrower.getRiceProgram());
                                                                params.put("coop",seedGrower.getCoop());
                                                                params.put("previouscrop",seedGrower.getPreviousCrop());
                                                                params.put("previousvariety",seedGrower.getPreviousVariety());
                                                                return params;
                                                            }
                                                        };
                                                        queue.add(sr);
                                                    }

                                                }
                                                else{
                                                    progressDialog.hide();
                                                    new AlertDialog.Builder(ListActivity.this)
                                                            .setTitle("No Internet Connection")
                                                            .setMessage("Internet connection is required to send form data.")
                                                            .setNegativeButton("Ok", null)
                                                            .show();
                                                }

                                            }
                                        })
                                .setNegativeButton( "Cancel" , null )
                                .show() ;

                        if(wantToCloseDialog)
                            dialog.dismiss();
                    }
                });
            }
        });

        /*adapter.setSendBtnClickedListener(new SeedGrowerAdapter.sendBtnClicked() {
            @Override
            public void sendToServer(final SeedGrower seedGrower) {
                 new AlertDialog.Builder(HomeActivity. this )
                        .setTitle("Send Form?")
                        .setMessage( "Data willbe sent to server." )
                        .setPositiveButton( "Send" , new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                            LayoutInflater inflater = getLayoutInflater();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                                            View dialogView = inflater.inflate(R.layout.loading_template,null);
                                            builder.setCancelable(false);
                                            builder.setView(dialogView);

                                            final AlertDialog progressDialog = builder.create();
                                            progressDialog.show();

                                        if(isOnline()){

                                            if(seedGrower.getVariety().matches("Select Variety") || seedGrower.getSeedsource().matches("Select Seed Source")
                                                    || seedGrower.getSeedclass().matches("Select Seed Class") || seedGrower.getDateplanted().matches("0002-11-30")
                                                    || seedGrower.getDateplanted().matches("-0001-11-30") || seedGrower.getDateplanted().matches("0000-00-00")
                                                    || seedGrower.getAreaplanted().matches("") || seedGrower.getQuantity().matches("")
                                                    || seedGrower.getSeedbedarea().matches("") || seedGrower.getSeedlingage().matches("")
                                                    || seedGrower.getSeedlot().matches("") || seedGrower.getControlno().matches("")
                                                    || seedGrower.getBarangay().matches("") || seedGrower.getRiceProgram().matches("Select Rice Program") || seedGrower.getCoop().matches("")){
                                                progressDialog.hide();
                                                new AlertDialog.Builder(ListActivity.this)
                                                    .setMessage("Please fill out all the fields first before sending data.")
                                                    .setNegativeButton("Try Again",null).show();
                                            }else{
                                                queue = Volley.newRequestQueue(ListActivity.this);
                                                String url = DecVar.receiver()+"/postData.php";

                                                StringRequest sr = new StringRequest(Request.Method.POST, url,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                if(!response.equals("Success")){
                                                                    progressDialog.hide();
                                                                    new AlertDialog.Builder(ListActivity.this)
                                                                            .setMessage("Error while sending data to server.")
                                                                            .setNegativeButton("Try Again",null).show();
                                                                }
                                                                else{
                                                                    progressDialog.hide();
                                                                    seedGrower.setIsSent(true);
                                                                    seedGrowerViewModel.update(seedGrower);
                                                                    new AlertDialog.Builder(ListActivity.this)
                                                                            .setMessage("Successfully sent form data")
                                                                            .setNegativeButton("Ok",null).show();
                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                progressDialog.hide();
                                                                new AlertDialog.Builder(ListActivity.this)
                                                                        .setMessage("Error while sending data to server.")
                                                                        .setNegativeButton("Try Again",null).show();
                                                                Log.e("HttpClient", "error: " + error.toString());
                                                            }
                                                        })
                                                {
                                                    @Override
                                                    protected Map<String,String> getParams(){
                                                        Map<String,String> params = new HashMap<>();
                                                        params.put("formId","0");
                                                        params.put("accredNo","");
                                                        params.put("serial_number",seedGrower.getAccredno());
                                                        params.put("seedSource",seedGrower.getSeedsource());
                                                        params.put("otherSource",seedGrower.getOtherseedsource());
                                                        params.put("variety",seedGrower.getVariety());
                                                        params.put("seedClass",seedGrower.getSeedclass());
                                                        params.put("datePlanted",seedGrower.getDateplanted());
                                                        params.put("areaPlanted",seedGrower.getAreaplanted());
                                                        params.put("quantity",seedGrower.getQuantity());
                                                        params.put("seedbedArea",seedGrower.getSeedbedarea());
                                                        params.put("seedlingAge",seedGrower.getSeedlingage());
                                                        params.put("seedlot",seedGrower.getSeedlot());
                                                        params.put("controlNo",seedGrower.getControlno());
                                                        params.put("barangay",seedGrower.getBarangay());
                                                        params.put("latitude",seedGrower.getLatitude());
                                                        params.put("longitude",seedGrower.getLongitude());
                                                        params.put("dateCollected",seedGrower.getDatecollected());
                                                        params.put("androidid",seedGrower.getMacaddress());
                                                        params.put("program",seedGrower.getRiceProgram());
                                                        params.put("coop",seedGrower.getCoop());
                                                        params.put("previouscrop",seedGrower.getPreviousCrop());
                                                        params.put("previousvariety",seedGrower.getPreviousVariety());
                                                        return params;
                                                    }
                                                };
                                                queue.add(sr);
                                            }

                                        }
                                        else{
                                            progressDialog.hide();
                                            new AlertDialog.Builder(ListActivity.this)
                                                    .setTitle("No Internet Connection")
                                                    .setMessage("Internet connection is required to send form data.")
                                                    .setNegativeButton("Ok", null)
                                                    .show();
                                        }

                                    }
                                })
                        .setNegativeButton( "Cancel" , null )
                        .show() ;
            }
        });*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);

        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addBtn:
                intent = new Intent(this, SeedProductionDetailActivity.class);
                startActivity(intent);
                return true;
            case R.id.sentItemBtn:
                intent = new Intent(this, SentItemActivity.class);
                startActivity(intent);
                return true;
            case R.id.aboutBtn:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
           /* case R.id.logoutBtn:
                logout();*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void logout(){
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        user.setLoggedIn("LoggedOut");
        userViewModel.update(user);

        if(userViewModel.getCheckLoggedIn() > 0){
            intent = new Intent(ListActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void editSGDetails(String sgId){
        Log.e(TAG, "editSGDetails: "+sgId );
        intent = new Intent(this, EditSeedProductionActivity.class);
        intent.putExtra(EXTRA_MESSAGE, sgId);
        startActivity(intent);

    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Allow this app to access camera to scan your QR CODE.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            mPermissionGranted = true;

                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            mPermissionGranted = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCounter();
        Log.e(TAG, "onResume: " );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e(TAG, "onResume: " );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setCheckable(false);
        switch (item.getItemId()) {
            case R.id.homeBtn:
                intent = new Intent(this, HomeActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                return true;
            case R.id.listBtn:
                intent = new Intent(this, ListActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                return true;
            case R.id.addBtn:
                intent = new Intent(this, SeedProductionDetailActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.sentItemBtn:
                intent = new Intent(this, SentItemActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                return true;
            case R.id.aboutBtn:
                intent = new Intent(this, AboutActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
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

    public void updateCounter(){
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());
        listTvCollected.setText("Collected List: "+String.valueOf(countCollected));
        tvList.setText(String.valueOf(countCollected));
        tvSent.setText(String.valueOf(countSent));
        tvDeleted.setText(String.valueOf(countDeleted));
    }
}