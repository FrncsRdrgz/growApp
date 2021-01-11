package govph.rsis.growapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SentItemActivity extends AppCompatActivity {
    public static final String TAG = "SentItemAcitivity";
    private SeedGrowerViewModel seedGrowerViewModel;

    TextView tvYourForms, textaccredno1,textseedsource1,textvariety1,textclass1,textplanted1,
            textareaplanted1,textquantity1, textseedbedarea1, textseedlingage1, textlot1, textcontrol1,
            textbarangay1, textlatitude1, textlongitude1, titlevariety, textCoop, textProgram,textStation;
    LinearLayout lStation, linear2,linear3,linear16,linear17;
    Toolbar toolbar;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sent_item);
        toolbar = findViewById(R.id.sent_toolbar);
        tvYourForms = findViewById(R.id.tvYourForms);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        final SentAdapter adapter = new SentAdapter();
        recyclerView.setAdapter(adapter);

        seedGrowerViewModel = ViewModelProviders.of(this).get(SeedGrowerViewModel.class);
        seedGrowerViewModel.getAllSent().observe(this, new Observer<List<SeedGrower>>() {
            @Override
            public void onChanged(List<SeedGrower> seedGrowers) {
                //update recyclerview
                adapter.setSeedGrowers(seedGrowers);
            }
        });

        tvYourForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                //find all the linearlayout
                lStation = (LinearLayout) dialogView.findViewById(R.id.linearStation);
                linear2 = (LinearLayout) dialogView.findViewById(R.id.linear2);
                linear3 = (LinearLayout) dialogView.findViewById(R.id.linear3);
                linear16 = (LinearLayout) dialogView.findViewById(R.id.linear16);
                linear17 = (LinearLayout) dialogView.findViewById(R.id.linear17);

                //find all the textviews
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
                textStation = (TextView) dialogView.findViewById(R.id.textStation);

                if(seedGrower.getDesignation().matches("PhilRice")){

                    //set visibility to GONE
                    linear2.setVisibility(View.GONE);
                    linear3.setVisibility(View.GONE);
                    linear16.setVisibility(View.GONE);
                    linear17.setVisibility(View.GONE);

                    //populate all the textviews according to designation
                    textStation.setText(seedGrower.getStation());
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

                }else if(seedGrower.getDesignation().matches("SeedGrower")){
                    //set visibility to GONE
                    lStation.setVisibility(View.GONE);

                    //populate all the textviews according to designation
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
                }


                builder.setNegativeButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();


            }

        });
    }
}
