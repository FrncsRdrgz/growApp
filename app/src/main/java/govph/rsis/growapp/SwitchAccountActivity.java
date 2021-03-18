package govph.rsis.growapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtViewModel;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserAdapter;
import govph.rsis.growapp.User.UserViewModel;

public class SwitchAccountActivity extends AppCompatActivity {
    public static final String TAG = "SwitchAccountActivity";
    private UserViewModel userViewModel;
    private SeedBoughtViewModel seedBoughtViewModel;
    private RecyclerView recyclerView;
    private TextView tvLogin;
    private Intent intent;
    GlobalFunction globalFunction;
    LayoutInflater inflater;
    RequestQueue queue;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    View dialogView;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_account);
        inflater = getLayoutInflater();
        globalFunction = new GlobalFunction(getApplicationContext());
        globalFunction.isOnline();
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        seedBoughtViewModel = ViewModelProviders.of(this).get(SeedBoughtViewModel.class);

        recyclerView = (RecyclerView) findViewById(R.id.rvUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        final UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });
        builder = new AlertDialog.Builder(this);
        dialogView = inflater.inflate(R.layout.loading_template,null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        dialog = builder.create();

        /*Adapter clickables*/
        adapter.setUserLoginClickedListener(new UserAdapter.userLogin() {
            @Override
            public void userLogin(User user) {
                getUserLoginDetails(user.getSerialNum());
                dialog.show();
            }
        });
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               intent = new Intent(SwitchAccountActivity.this, LoginActivity.class);
               startActivity(intent);

            }
        });
    }

    private void getUserLoginDetails(String scannedValue){
        queue = Volley.newRequestQueue(this);
        String url = DecVar.userCredentialApiUrl()+'/'+scannedValue;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = new User();

                if(response.equals("204")){
                    Toast.makeText(SwitchAccountActivity.this, "Make sure that your Serial Number is correct.", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject json = new JSONObject(response);
                        Log.e(TAG, "onResponse: "+json );
                        if(json != null || json.length() > 0){
                            String fullName = json.getString("fullName");
                            String accredArea = json.getString("accredArea");
                            String serialNum = json.getString("serialNum");
                            String data = json.getString("data");
                            int isExisting = userViewModel.isExisting(serialNum);

                            JSONArray jsonArray = json.getJSONArray("data");
                            if(isExisting > 0 ){
                                userViewModel.getUpdateUserStatus("LoggedIn",serialNum);
                            }else{
                                user.setSerialNum(serialNum);
                                user.setFullname(fullName);
                                user.setAccredArea(accredArea);
                                user.setLoggedIn("LoggedIn");
                                userViewModel.insert(user);
                            }

                            if(jsonArray.length() != 0){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    SeedBought seedBought = new SeedBought();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String palletCode = jsonObject.getString("palletCode");
                                    String variety = jsonObject.getString("variety");
                                    String seedClass = jsonObject.getString("seedClass");
                                    String riceProgram = jsonObject.getString("riceProgram");
                                    String table_name = jsonObject.getString("table_name");
                                    int quantity = jsonObject.getInt("quantity");
                                    double area = jsonObject.getDouble("area");


                                    seedBought.setSerialNum(serialNum);
                                    seedBought.setVariety(variety);
                                    seedBought.setSeedClass(seedClass);
                                    seedBought.setPalletCode(palletCode);
                                    seedBought.setQuantity(quantity);
                                    seedBought.setUsedQuantity(0);
                                    seedBought.setTableName(table_name);
                                    seedBought.setArea(area);
                                    seedBought.setRiceProgram(riceProgram);

                                    seedBoughtViewModel.insert(seedBought);
                                }
                            }

                            /*for(int i = 0; i < json.length(); i++){
                                JSONObject jsonObject = json.getJSONObject(i);
                                String fullName = jsonObject.getString("fullName");
                                String accredArea = jsonObject.getString("accredArea");
                                String serialNum = jsonObject.getString("serialNum");
                                user.setSerialNum(serialNum);
                                int isExisting = userViewModel.isExisting(serialNum);


                                if(isExisting > 0 ){
                                    userViewModel.getUpdateUserStatus("LoggedIn",serialNum);
                                }else{
                                    user.setFullname(fullName);
                                    user.setAccredArea(accredArea);
                                    user.setLoggedIn("LoggedIn");
                                    userViewModel.insert(user);
                                }

                            }*/
                            //if(userViewModel.isExisting(user.serialNum);)
                            dialog.dismiss();
                            intent = new Intent(SwitchAccountActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(SwitchAccountActivity.this, "No Seed Grower details.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e(TAG, "onErrorResponse: "+error );
                Toast.makeText(SwitchAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
        //Toast.makeText(this, "scannedValue: "+scannedValue, Toast.LENGTH_SHORT).show();
    }
}