package com.example.growapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.releasingapp.MESSAGE";
    public static final String EXTRA_ARRAY = "com.example.releasingapp.EXTRA_ARRAY";
    private static final String TAG = "HomeActivity";
    private SeedGrowerViewModel seedGrowerViewModel;

    TextView tvDeleteAll;
    Toolbar toolbar;
    Intent intent;

    SeedGrowerDatabase database;
    RecyclerView rvSeedGrowers;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.e(TAG, "onClick: "+ DebugDB.getAddressLog() );
        database = SeedGrowerDatabase.getInstance(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvDeleteAll = (TextView) findViewById(R.id.tvDeleteAll);
        rvSeedGrowers = (RecyclerView) findViewById(R.id.recyclerView1);
        rvSeedGrowers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSeedGrowers.setHasFixedSize(true);

        final SeedGrowerAdapter adapter = new SeedGrowerAdapter();
        rvSeedGrowers.setAdapter(adapter);
        //adapter.setSgClickedListener(this);
        //adapter.setSgEditClickedListener(this);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        seedGrowerViewModel.getAllSeedGrowers().observe(this, new Observer<List<SeedGrower>>() {
            @Override
            public void onChanged(List<SeedGrower> seedGrowers) {
                //update recyclerview
                adapter.setSeedGrowers(seedGrowers);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                seedGrowerViewModel.delete(adapter.getSeedGrowerAt(viewHolder.getAdapterPosition()));
                Toast.makeText(HomeActivity.this, "Form deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvSeedGrowers);

        //This will delete all the forms from the phone database
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Are you sure you want to delete all forms?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                seedGrowerViewModel.deleteAllSeedGrower();
                                Toast.makeText(HomeActivity.this, "Deleted All Forms.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No",null).show();

            }
        });

        adapter.setSgFormClickedListener(new SeedGrowerAdapter.sgFormClicked() {
            @Override
            public void editSGDetails(SeedGrower seedGrower) {
                intent = new Intent(HomeActivity.this,EditSeedProductionActivity.class);

                intent.putExtra(EXTRA_MESSAGE, Integer.toString(seedGrower.getSgId()));
                startActivity(intent);
            }
        });

        adapter.setSendBtnClickedListener(new SeedGrowerAdapter.sendBtnClicked() {
            @Override
            public void sendToServer(final SeedGrower seedGrower) {
                new AlertDialog.Builder(HomeActivity. this )
                        .setTitle("Send Form?")
                        .setMessage( "Data will be sent to server." )
                        .setPositiveButton( "Send" , new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                        if(isOnline()){
                                            queue = Volley.newRequestQueue(HomeActivity.this);
                                        String url = "https://stagingdev.philrice.gov.ph/rsis/growApp/postData.php";

                                        StringRequest sr = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        seedGrower.setIsSent(true);
                                                        seedGrowerViewModel.update(seedGrower);
                                                        new AlertDialog.Builder(HomeActivity.this)
                                                                .setMessage("Successfully sent form data")
                                                                .setNegativeButton("Ok",null).show();
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("HttpClient", "error: " + error.toString());
                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String,String> getParams(){
                                                Map<String,String> params = new HashMap<>();
                                                params.put("formId","0");
                                                params.put("accredNo",seedGrower.getAccredno());
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
                                                return params;
                                            }
                                        };
                                        queue.add(sr);
                                        }
                                        else{
                                            new AlertDialog.Builder(HomeActivity.this)
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
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);

        return super.onCreateOptionsMenu(menu);
    }

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
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

    public void editSGDetails(String sgId){
        Log.e(TAG, "editSGDetails: "+sgId );
        intent = new Intent(this, EditSeedProductionActivity.class);
        intent.putExtra(EXTRA_MESSAGE, sgId);
        startActivity(intent);

    }

}
