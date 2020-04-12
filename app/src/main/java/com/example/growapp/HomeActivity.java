package com.example.growapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements SeedGrowerAdapter.SGClicked, SeedGrowerAdapter.SGEditClicked {
    public static final String EXTRA_MESSAGE = "com.example.releasingapp.MESSAGE";
    private static final String TAG = "HomeActivity";
    private SeedGrowerViewModel seedGrowerViewModel;

    TextView tvEdit;
    Toolbar toolbar;
    Intent intent;

    SeedGrowerDatabase database;
    RecyclerView rvSeedGrowers;
    SeedGrowerAdapter adapter;
    //ArrayList<SeedGrower> seedGrowers;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database = SeedGrowerDatabase.getInstance(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        seedGrowerViewModel.getAllSeedGrowers().observe(this, new Observer<List<SeedGrower>>() {
            @Override
            public void onChanged(List<SeedGrower> seedGrowers) {
                //update recyclerview
                Toast.makeText(HomeActivity.this, "onchanged view model", Toast.LENGTH_SHORT).show();
            }
        });


        /*rvSeedGrowers = (RecyclerView) findViewById(R.id.recyclerView1);
        //seedGrowers = getAllSeedGrower();
        rvSeedGrowers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSeedGrowers.setItemAnimator(new DefaultItemAnimator());
        //adapter = new SeedGrowerAdapter(this,seedGrowers);
        adapter.setSgClickedListener(this);
        adapter.setSgEditClickedListener(this);

        rvSeedGrowers.setAdapter(adapter);
        tvEdit = (TextView) findViewById(R.id.tbEdit);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this,EditSeedProductionActivity.class);
                startActivity(intent);
            }
        });*/

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

    public void sendToServer(String sgId) {
        final SeedGrower sg = database.seedGrowerDao().findFormById(sgId);
        Toast.makeText(this, "id"+sgId, Toast.LENGTH_SHORT).show();
        queue = Volley.newRequestQueue(this);
        String url = "https://stagingdev.philrice.gov.ph/rsis/growApp/postData.php";

        StringRequest sr = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sg.setIsSent(true);
                    database.seedGrowerDao().updateSeedGrower(sg);
                    Toast.makeText(HomeActivity.this, "Sent to server successfully", Toast.LENGTH_SHORT).show();
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
                    params.put("accredNo",sg.getAccredno());
                    params.put("seedSource",sg.getSeedsource());
                    params.put("otherSource",sg.getOtherseedsource());
                    params.put("variety",sg.getVariety());
                    params.put("seedClass",sg.getSeedclass());
                    params.put("datePlanted",sg.getDateplanted());
                    params.put("areaPlanted",sg.getAreaplanted());
                    params.put("quantity",sg.getQuantity());
                    params.put("seedbedArea",sg.getSeedbedarea());
                    params.put("seedlingAge",sg.getSeedlingage());
                    params.put("seedlot",sg.getSeedlot());
                    params.put("controlNo",sg.getControlno());
                    params.put("barangay",sg.getBarangay());
                    params.put("latitude",sg.getLatitude());
                    params.put("longitude",sg.getLongitude());
                    params.put("dateCollected",sg.getDatecollected());
                    params.put("androidid",sg.getMacaddress());
                    return params;
                }
            };
        queue.add(sr);
    }

    public void editSGDetails(String sgId){
        Log.e(TAG, "editSGDetails: "+sgId );
        intent = new Intent(this, EditSeedProductionActivity.class);
        intent.putExtra(EXTRA_MESSAGE, sgId);
        startActivity(intent);

    }

}
