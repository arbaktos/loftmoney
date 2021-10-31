package com.example.loftmoney.screens.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loftmoney.LoftApp;
import com.example.loftmoney.R;
import com.example.loftmoney.screens.login.LoginActivity;
import com.example.loftmoney.screens.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
        if (TextUtils.isEmpty(authToken)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
