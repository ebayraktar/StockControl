package com.bayraktar.stockcontrol.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    TextInputLayout tilName, tilUsername, tilPassword, tilPasswordConfirm;
    EditText etName, etUsername, etPassword, etPasswordConfirm;
    UserDao userDao;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppDatabase database = AppDatabase.getInstance(this);
        userDao = database.userDao();

        tilName = findViewById(R.id.tilName);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        tilPasswordConfirm = findViewById(R.id.tilPasswordConfirm);

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);

        etName.setOnFocusChangeListener(this);
        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        etPasswordConfirm.setOnFocusChangeListener(this);

        findViewById(R.id.tvLogin).setOnClickListener(this);
        findViewById(R.id.cvSignUp).setOnClickListener(this);
    }

    void clearText() {
        etName.setText("");
        etUsername.setText("");
        etPassword.setText("");
        etPasswordConfirm.setText("");
    }

    void signUp() {
        if (validateForm()) {
            String password = App.md5(etPassword.getText().toString());
            User tempUser = new User(etUsername.getText().toString(), etName.getText().toString(), password);
            new SignUpAsyncTask(this, userDao).execute(tempUser);

        }
    }

    public void success() {
        Toast.makeText(RegisterActivity.this, "Kayıt başarılı.", Toast.LENGTH_SHORT).show();
        clearText();
    }

    public void failed() {
        tilUsername.setError("Bu kullanıcı adı zaten alınmış");
        Toast.makeText(RegisterActivity.this, "Bu kullanıcı adı zaten alınmış.", Toast.LENGTH_SHORT).show();
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

    boolean validateForm() {
        boolean isValid = true;
        if (TextUtils.isEmpty(etName.getText().toString())) {
            isValid = false;
            tilName.setError("Boş olamaz!");
        } else {
            tilName.setError("");
        }
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
        if (TextUtils.isEmpty(etPasswordConfirm.getText().toString())) {
            isValid = false;
            tilPasswordConfirm.setError("Boş olamaz!");
        } else {
            if (!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())) {
                isValid = false;
                tilPasswordConfirm.setError("Parola eşlemişyor");
            } else {
                tilPasswordConfirm.setError("");
            }
        }

        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.cvSignUp:
                signUp();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etName:
                if (!hasFocus)
                    checkInput(v, tilName);
                break;
            case R.id.etUsername:
                if (!hasFocus)
                    checkInput(v, tilUsername);
                break;
            case R.id.etPassword:
                if (!hasFocus)
                    checkInput(v, tilPassword);
                break;
            case R.id.etPasswordConfirm:
                if (!hasFocus)
                    checkInput(v, tilPasswordConfirm);
                break;
            default:
                break;
        }
    }

    private static class SignUpAsyncTask extends AsyncTask<User, Void, Boolean> {

        private WeakReference<RegisterActivity> activity;
        UserDao userDao;

        public SignUpAsyncTask(RegisterActivity activity, UserDao userDao) {
            this.activity = new WeakReference<>(activity);
            this.userDao = userDao;
        }

        @Override
        protected Boolean doInBackground(User... users) {
            User user = userDao.checkUser(users[0].getUserName());
            if (user == null) {
                userDao.insert(users[0]);
                return true;
            } else return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                activity.get().success();
            } else {
                activity.get().failed();
            }
            super.onPostExecute(aBoolean);
        }
    }
}