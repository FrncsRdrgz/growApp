package govph.rsis.growapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    GlobalFunction globalFunction;
    private static final String TAG = "Login";
    Button scanBtn;
    Intent intent;
    public static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        scanBtn = findViewById(R.id.scanIdBtn);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,ScannerActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if (resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                final String result = data.getStringExtra(ScannerActivity.Scanned_value);
                getUserLoginDetails(result);
                // Use the data - in this case, display it in a Toast.
            } else {
                // AnotherActivity was not successful. No data to retrieve.
            }
        }
    }

    private void getUserLoginDetails(String scannedValue){
        Toast.makeText(this, "scannedValue: "+scannedValue, Toast.LENGTH_SHORT).show();
    }
}