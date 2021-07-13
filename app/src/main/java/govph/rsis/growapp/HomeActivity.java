package govph.rsis.growapp;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import govph.rsis.growapp.Municipality.Municipality;
import govph.rsis.growapp.Municipality.MunicipalityViewModel;
import govph.rsis.growapp.Province.Province;
import govph.rsis.growapp.Province.ProvinceViewModel;
import govph.rsis.growapp.Region.Region;
import govph.rsis.growapp.Region.RegionViewModel;
import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtViewModel;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserViewModel;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.releasingapp.MESSAGE";
    private static final String TAG = "HomeActivity";
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SeedGrowerViewModel seedGrowerViewModel;
    private UserViewModel userViewModel;
    private SeedBoughtViewModel seedBoughtViewModel;
    private RegionViewModel regionViewModel;
    private ProvinceViewModel provinceViewModel;
    private MunicipalityViewModel municipalityViewModel;
    private View headerView;
    RequestQueue queue;
    TextView headerVersion,headerName,headerSerial,homeTvVersion,tvList,tvSent,tvDeleted,homeTvCollected,homeTvSent,homeTvDeleted,homeActUserName,homeActSerialNum,toolBarLogout;
    MenuItem mList,mSent,mDeleted;
    LinearLayout linearList,linearAdd,linearAbout,linearSent,linearSyncData,linearLogout;
    Intent intent;
    User user;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    View dialogView;
    LayoutInflater inflater;
    int activityId, countCollected,countSent,countDeleted;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(this);
        dialogView = inflater.inflate(R.layout.loading_template,null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        dialog = builder.create();
        Log.e(TAG, "onCreate: "+ DebugDB.getAddressLog());

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        seedBoughtViewModel = ViewModelProviders.of(this).get(SeedBoughtViewModel.class);
        regionViewModel = ViewModelProviders.of(this).get(RegionViewModel.class);
        provinceViewModel = ViewModelProviders.of(this).get(ProvinceViewModel.class);
        municipalityViewModel = ViewModelProviders.of(this).get(MunicipalityViewModel.class);


        getPSGC();
        user = userViewModel.getLoggedInUser();
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        headerVersion = headerView.findViewById(R.id.headerVersion);
        headerName = headerView.findViewById(R.id.headerName);
        headerSerial = headerView.findViewById(R.id.headerSerial);

        toolBarLogout = findViewById(R.id.toolBarLogout);
        homeTvVersion = findViewById(R.id.homeTvVersion);
        homeActUserName = findViewById(R.id.homeActUserName);
        homeActSerialNum = findViewById(R.id.homeActSerialNum);
        activityId = getIntent().getIntExtra("activityId",0);
        homeTvCollected = findViewById(R.id.homeTvCollected);
        homeTvSent = findViewById(R.id.homeTvSent);
        homeTvDeleted = findViewById(R.id.homeTvDeleted);
        linearAbout = findViewById(R.id.linearAbout);
        linearList = findViewById(R.id.linearList);
        linearAdd = findViewById(R.id.linearAdd);
        linearSent = findViewById(R.id.linearSent);
        linearSyncData = findViewById(R.id.linearSyncData);
        linearLogout = findViewById(R.id.linearLogout);

        mList = navigationView.getMenu().findItem(R.id.listBtn);
        mSent = navigationView.getMenu().findItem(R.id.sentItemBtn);
        mDeleted = navigationView.getMenu().findItem(R.id.deletedBtn);

        tvList = (TextView) mList.getActionView();
        tvList.setText(String.valueOf(countCollected));
        tvSent = (TextView) mSent.getActionView();
        tvSent.setText(String.valueOf(countSent));
        tvDeleted = (TextView) mDeleted.getActionView();
        tvDeleted.setText(String.valueOf(countDeleted));
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            headerVersion.setText("Version: "+ pInfo.versionName);
            homeTvVersion.setText("Version: "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        headerName.setText(user.getFullname());
        headerSerial.setText(user.getSerialNum());
        homeActSerialNum.setText(user.getSerialNum());
        homeActUserName.setText(user.getFullname());
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
        if(activityId == R.id.homeBtn){
            navigationView.setCheckedItem(activityId);
        }
        else{
            navigationView.setCheckedItem(R.id.homeBtn);
        }

        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> visitors = new ArrayList<>();
        visitors.add(new PieEntry(countCollected,"Collected"));
        visitors.add(new PieEntry(countSent, "Sent"));
        visitors.add(new PieEntry(countDeleted, "Deleted"));

        PieDataSet pieDataSet = new PieDataSet(visitors, null);
        pieDataSet.setColors(Color.parseColor("#3266CC"),Color.parseColor("#FE9900"),Color.parseColor("#DE3813"));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setSliceSpace(3f);
        PieData pieData = new PieData(pieDataSet);


        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getLegend().setEnabled(false);
        //Legend legend =pieChart.getLegend();
        //legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //legend.setTextSize(20f);

        pieChart.animate();
        homeTvCollected.setText(String.valueOf(countCollected));
        homeTvSent.setText(String.valueOf(countSent));
        homeTvDeleted.setText(String.valueOf(countDeleted));

        linearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, SeedProductionDetailActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        linearSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, SentItemActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        linearAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        linearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, ListActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        linearSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                getBoughtData(user.getSerialNum());
            }
        });
        toolBarLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        linearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setCheckable(false);
        switch (id) {
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
            intent = new Intent(HomeActivity.this,SwitchAccountActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    private void getBoughtData(String scannedValue){
        queue = Volley.newRequestQueue(HomeActivity.this);
        String url = DecVar.getSeedBought()+'/'+scannedValue;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e(TAG, "onResponse: "+response );
                        try{
                            if(response.length() != 0){
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    SeedBought seedBought = new SeedBought();
                                    String palletCode = jsonObject.getString("palletCode");
                                    String variety = jsonObject.getString("variety");
                                    String seedClass = jsonObject.getString("seedClass");
                                    String riceProgram = jsonObject.getString("riceProgram");
                                    String table_name = jsonObject.getString("table_name");
                                    String station_name = jsonObject.getString("station_name");
                                    int quantity = jsonObject.getInt("quantity");
                                    double area = jsonObject.getDouble("area");

                                    seedBought.setSerialNum(user.getSerialNum());
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: "+error );
                    }
                });
        queue.add(jsonArrayRequest);
        //Toast.makeText(this, "scannedValue: "+scannedValue, Toast.LENGTH_SHORT).show();
    }

    private void getPSGC(){
        regionViewModel.deleteRegions();
        provinceViewModel.deleteProvinces();
        municipalityViewModel.deleteMunicipalities();
        queue = Volley.newRequestQueue(HomeActivity.this);
        String url = DecVar.getPSGC();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.length() !=0){

                                JSONArray regions = response.getJSONArray("regions");
                                JSONArray provinces = response.getJSONArray("provinces");
                                JSONArray municipalities = response.getJSONArray("municipalities");

                                for (int i = 0; i < regions.length(); i++) {
                                    JSONObject json_region = regions.getJSONObject(i);

                                    String region_name = json_region.getString("region");
                                    int region_id = json_region.getInt("region_id");

                                    Region region = new Region();
                                    region.setRegion(region_name);
                                    region.setRegion_id(region_id);

                                    regionViewModel.insert(region);
                                }

                                for (int j = 0; j < provinces.length(); j++) {
                                    JSONObject json_province = provinces.getJSONObject(j);

                                    String province_name = json_province.getString("province");
                                    int region_id = json_province.getInt("region_id");
                                    int province_id = json_province.getInt("province_id");

                                    Province province = new Province();
                                    province.setProvince(province_name);
                                    province.setProvince_id(province_id);
                                    province.setRegion_id(region_id);

                                    provinceViewModel.insert(province);
                                }

                                for (int k = 0; k < municipalities.length(); k++) {
                                    JSONObject json_municipality = municipalities.getJSONObject(k);

                                    String municipality_name = json_municipality.getString("municipality");
                                    int province_id = json_municipality.getInt("province_id");
                                    int municipality_id = json_municipality.getInt("municipality_id");

                                    Municipality municipality = new Municipality();
                                    municipality.setMunicipality(municipality_name);
                                    municipality.setMunicipality_id(municipality_id);
                                    municipality.setProvince_id(province_id);

                                    municipalityViewModel.insert(municipality);
                                }


                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: "+error );
                    }
                });
        queue.add(jsonObjectRequest);
    }
}
