package com.bayraktar.stockcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bayraktar.stockcontrol.database.AppDatabase;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    TextInputLayout tilUsername, tilPassword;
    EditText etUsername, etPassword;
    AppDatabase appDatabase;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.click_back_to_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);

        findViewById(R.id.tvRegister).setOnClickListener(this);
        findViewById(R.id.cvLogin).setOnClickListener(this);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "stock").build();
    }

    private void checkInput(View v, TextInputLayout til) {
        if (v instanceof EditText) {
            EditText editText = (EditText) v;
            if (TextUtils.isEmpty(editText.getText().toString())) {
                til.setError("Boş olamaz!");
            } else {
                til.setError("");
            }
        }
    }

    void login() {
        if (validateForm()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        }
    }

    boolean validateForm() {
        boolean isValid = true;
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            isValid = false;
            tilUsername.setError("Boş olamaz!");
        } else {
            tilUsername.setError("");
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            isValid = false;
            tilPassword.setError("Boş olamaz!");
        } else {
            tilPassword.setError("");
        }

        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cvLogin:
                login();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etUsername:
                if (!hasFocus)
                    checkInput(v, tilUsername);
                break;
            case R.id.etPassword:
                if (!hasFocus)
                    checkInput(v, tilPassword);
                break;
            default:
                break;
        }
    }
}