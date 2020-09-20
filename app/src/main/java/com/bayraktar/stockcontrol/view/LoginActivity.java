package com.bayraktar.stockcontrol.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bayraktar.stockcontrol.App;
import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.database.AppDatabase;
import com.bayraktar.stockcontrol.database.dao.UserDao;
import com.bayraktar.stockcontrol.database.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    AppDatabase appDatabase;

    TextInputLayout tilUsername, tilPassword;
    EditText etUsername, etPassword;
    UserDao userDao;

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

        AppDatabase database = AppDatabase.getInstance(this);
        userDao = database.userDao();

        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);

        findViewById(R.id.tvRegister).setOnClickListener(this);
        findViewById(R.id.cvLogin).setOnClickListener(this);

        appDatabase = Room.databaseBuilder(getBaseContext(),
                AppDatabase.class, "stock_control").build();
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
            String password = App.md5(etPassword.getText().toString());

            new LoginAsyncTask(this, userDao).execute(etUsername.getText().toString(), password);
        }
    }

    public void success(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    public void failed() {
        Toast.makeText(LoginActivity.this, "Kullanıcı adı/parola hatalı", Toast.LENGTH_SHORT).show();
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
                finish();
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

    private static class LoginAsyncTask extends AsyncTask<String, Void, User> {

        private WeakReference<LoginActivity> activity;
        UserDao userDao;

        public LoginAsyncTask(LoginActivity activity, UserDao userDao) {
            this.activity = new WeakReference<>(activity);
            this.userDao = userDao;
        }


        @Override
        protected User doInBackground(String... strings) {
            return userDao.login(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                activity.get().success(user);
            } else {
                activity.get().failed();
            }
            super.onPostExecute(user);
        }
    }
}