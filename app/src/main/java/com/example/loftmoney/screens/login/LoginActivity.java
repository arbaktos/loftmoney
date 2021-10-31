package com.example.loftmoney.screens.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.loftmoney.LoftApp;
import com.example.loftmoney.R;
import com.example.loftmoney.remote.AuthApi;
import com.example.loftmoney.screens.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 392;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configureViews();
        configureViewModel();
        configureGoogleAuth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                AuthApi authApi = ((LoftApp) getApplication()).authApi;
                loginViewModel.makeLogin(authApi, account.getId());
            } else {
                Log.w("debug", "can't parse account");
            }
        } catch (ApiException e) {
            Log.w("debug", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void configureGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void configureViews() {
        AppCompatButton btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> {

            //loginViewModel.makeLogin(authApi);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

    }

    private void configureViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.authToken.observe(this, token -> {
            if (!TextUtils.isEmpty(token)) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);
                sharedPreferences.edit().putString(LoftApp.AUTH_KEY, token).apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
        loginViewModel.messageString.observe(this, error -> {
            if (!TextUtils.isEmpty(error))
            showToast(error);
        });
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
