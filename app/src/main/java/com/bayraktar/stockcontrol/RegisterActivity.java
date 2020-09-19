package com.bayraktar.stockcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}