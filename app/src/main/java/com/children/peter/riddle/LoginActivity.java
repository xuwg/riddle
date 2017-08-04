package com.children.peter.riddle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        String data = intent.getStringExtra("extra_data");
        Log.d("extra data", data);


        Button btn = (Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data_return", "LoginActivity result ok");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (!backPressed) {
            backPressed = true;
            Toast.makeText(this, "press back again will exit LoginActivity.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean backPressed = false;
}
