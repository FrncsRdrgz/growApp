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

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;

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
    private View headerView;
    TextView headerVersion,headerName,headerSerial,homeTvVersion,tvList,tvSent,tvDeleted,homeTvCollected,homeTvSent,homeTvDeleted,homeActUserName,homeActSerialNum;
    MenuItem mList,mSent,mDeleted;
    LinearLayout linearList,linearAdd,linearAbout,linearSent;
    Intent intent;
    User user;
    int activityId, countCollected,countSent,countDeleted;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);

        user = userViewModel.getLoggedInUser();
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());

        //Log.e(TAG, "collected: "+countCollected );
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        headerVersion = headerView.findViewById(R.id.headerVersion);
        headerName = headerView.findViewById(R.id.headerName);
        headerSerial = headerView.findViewById(R.id.headerSerial);
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
                finish();
            }
        });
        linearSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, SentItemActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linearAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
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

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }
}
