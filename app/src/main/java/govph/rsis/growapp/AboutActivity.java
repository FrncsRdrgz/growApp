package govph.rsis.growapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserViewModel;

public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int activityId;
    private Toolbar toolbar;
    private UserViewModel userViewModel;
    private SeedGrowerViewModel seedGrowerViewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private TextView headerVersion,headerName,headerSerial,aboutTvVersion,tvList,tvSent,tvDeleted;
    MenuItem mList,mSent,mDeleted;
    private User user;
    private Intent intent;
    int countCollected,countSent,countDeleted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        user = userViewModel.getLoggedInUser();
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());
        //get the activityId set from other activity;
        activityId = getIntent().getIntExtra("activityId",0);
        drawerLayout = findViewById(R.id.about_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);
        aboutTvVersion = findViewById(R.id.aboutTvVersion);
        headerVersion = headerView.findViewById(R.id.headerVersion);
        headerName = headerView.findViewById(R.id.headerName);
        headerSerial = headerView.findViewById(R.id.headerSerial);


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

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            headerVersion.setText("Version: "+ pInfo.versionName);
            aboutTvVersion.setText("Version: "+ pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        headerName.setText(user.getFullname());
        headerSerial.setText(user.getSerialNum());
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
        navigationView.setCheckedItem(R.id.aboutBtn);
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
                finish();
                return true;
            case R.id.listBtn:
                intent = new Intent(this, ListActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
                finish();
                return true;
            case R.id.addBtn:
                intent = new Intent(this, SeedProductionDetailActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
                finish();
                return true;
            case R.id.sentItemBtn:
                intent = new Intent(this, SentItemActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
                finish();
                return true;
            case R.id.aboutBtn:
                intent = new Intent(this, AboutActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                intent.putExtra("activityId",id);
                startActivity(intent);
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