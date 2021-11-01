package com.example.loftmoney.screens.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.loftmoney.remote.AuthApi;
import com.example.loftmoney.remote.AuthResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<String> messageString = new MutableLiveData<>();
    public MutableLiveData<String> authToken = new MutableLiveData<>();

    void makeLogin(AuthApi authApi, String userId) {
        Disposable disposable = authApi.getAuth(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    authToken.postValue(authResponse.getAuthToken());

                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
//                    Log.d("debug", messageString.getValue());
                    Log.d("debug", authToken.getValue().toString());
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}

