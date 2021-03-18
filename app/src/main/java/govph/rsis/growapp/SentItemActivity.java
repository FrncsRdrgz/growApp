package govph.rsis.growapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserViewModel;

public class SentItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "SentItemAcitivity";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SeedGrowerViewModel seedGrowerViewModel;
    private UserViewModel userViewModel;
    private User user;
    TextView tvYourForms, textaccredno1,textseedsource1,textvariety1,textclass1,textplanted1,
            textareaplanted1,textquantity1, textseedbedarea1, textseedlingage1, textlot1, textcontrol1,
            textbarangay1, textlatitude1, textlongitude1, titlevariety, textCoop, textProgram,  sentTvVersion,
            headerVersion,headerName,headerSerial,homeTvVersion,tvEmptyView,tvList,tvSent,tvDeleted,sentActTvSent;
    MenuItem mList,mSent,mDeleted;
    Toolbar toolbar;
    Intent intent;
    RecyclerView recyclerView;
    int activityId,countCollected,countSent,countDeleted;
    View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        user = userViewModel.getLoggedInUser();
        countCollected = seedGrowerViewModel.getCountCollected(user.getSerialNum());
        countSent = seedGrowerViewModel.getCountSent(user.getSerialNum());
        countDeleted = seedGrowerViewModel.getCountDeleted(user.getSerialNum());

        activityId = getIntent().getIntExtra("activityId",0);
        setContentView(R.layout.activity_sent_item);
        toolbar = findViewById(R.id.sent_toolbar);
        drawerLayout = findViewById(R.id.sent_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        headerVersion = headerView.findViewById(R.id.headerVersion);
        headerName = headerView.findViewById(R.id.headerName);
        headerSerial = headerView.findViewById(R.id.headerSerial);
        sentTvVersion = findViewById(R.id.sentTvVersion);
        //sentActTvSent = findViewById(R.id.sentActTvSent);
        tvEmptyView = findViewById(R.id.empty_view);

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Sent List: " +String.valueOf(countSent));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        final SentAdapter adapter = new SentAdapter();
        recyclerView.setAdapter(adapter);

        headerName.setText(user.getFullname());
        headerSerial.setText(user.getSerialNum());
        //sentActTvSent.setText("Sent: " + String.valueOf(countSent));
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            headerVersion.setText("Version: "+ pInfo.versionName);
            sentTvVersion.setText("Version: "+pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onCreate: "+activityId );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.sentItemBtn);



        seedGrowerViewModel.getAllSent(user.getSerialNum()).observe(this, new Observer<List<SeedGrower>>() {
            @Override
            public void onChanged(List<SeedGrower> seedGrowers) {
                //update recyclerview
                adapter.setSeedGrowers(seedGrowers);
                if(seedGrowers.isEmpty()){
                    tvEmptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    //tvDeleteAll.setClickable(false);
                    //tvDeleteAll.setTextColor(Color.GRAY);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    tvEmptyView.setVisibility(View.GONE);
                    adapter.setSeedGrowers(seedGrowers);
                    //tvDeleteAll.setClickable(true);
                    //tvDeleteAll.setTextColor(Color.WHITE);
                }
            }
        });


        adapter.setViewBtnClickedListener(new SentAdapter.viewBtn() {
            @Override
            public void viewSelectedForm(SeedGrower seedGrower) {
                /*new AlertDialog.Builder(SentItemActivity.this)
                        .setView(R.layout.layout_view)
                        .setPositiveButton("Ok",null)
                        .show();*/
                LayoutInflater inflater = getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(SentItemActivity.this);
                View dialogView = inflater.inflate(R.layout.layout_view,null);
                builder.setCancelable(true);
                builder.setView(dialogView);

                textaccredno1 = (TextView) dialogView.findViewById(R.id.textaccredno1);
                textseedsource1 = (TextView) dialogView.findViewById(R.id.textsource1);
                textvariety1 = (TextView) dialogView.findViewById(R.id.textvariety1);
                textclass1 = (TextView) dialogView.findViewById(R.id.textclass1);
                textplanted1 = (TextView) dialogView.findViewById(R.id.textdateplanted1);
                textareaplanted1 = (TextView) dialogView.findViewById(R.id.textareaplanted1);
                textquantity1 = (TextView) dialogView.findViewById(R.id.textquantity1);
                textseedbedarea1 = (TextView) dialogView.findViewById(R.id.textseedbedarea1);
                textseedlingage1 = (TextView) dialogView.findViewById(R.id.textseedlingage1);
                textlot1 = (TextView) dialogView.findViewById(R.id.textlot1);
                textcontrol1 = (TextView) dialogView.findViewById(R.id.textcontrol1);
                textbarangay1 = (TextView) dialogView.findViewById(R.id.textbarangay1);
                textlatitude1 = (TextView) dialogView.findViewById(R.id.textlatitude1);
                textlongitude1 = (TextView) dialogView.findViewById(R.id.textlongitude1);
                titlevariety = (TextView) dialogView.findViewById(R.id.titlevariety);
                textCoop = (TextView) dialogView.findViewById(R.id.textCoop);
                textProgram = (TextView) dialogView.findViewById(R.id.textProgram);

                textaccredno1.setText(seedGrower.getAccredno());
                textseedsource1.setText(seedGrower.getSeedsource());
                textvariety1.setText(seedGrower.getVariety());
                textclass1.setText(seedGrower.getSeedclass());
                textplanted1.setText(seedGrower.getDateplanted());
                textareaplanted1.setText(seedGrower.getAreaplanted());
                textquantity1.setText(seedGrower.getQuantity());
                textseedbedarea1.setText(seedGrower.getSeedbedarea());
                textseedlingage1.setText(seedGrower.getSeedlingage());
                textlot1.setText(seedGrower.getSeedlot());
                textcontrol1.setText(seedGrower.getControlno());
                textbarangay1.setText(seedGrower.getBarangay());
                textlongitude1.setText(seedGrower.getLongitude());
                textlatitude1.setText(seedGrower.getLatitude());
                titlevariety.setText(seedGrower.getVariety());
                textCoop.setText(seedGrower.getCoop());
                textProgram.setText(seedGrower.getRiceProgram());

                builder.setNegativeButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
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
            intent = new Intent(SentItemActivity.this,SwitchAccountActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
