package com.example.loftmoney.screens.balance;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.loftmoney.remote.MoneyApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BalanceViewModel extends ViewModel {
    MutableLiveData<String> balance = new MutableLiveData("0");
    MutableLiveData<String> expenses= new MutableLiveData("0");
    MutableLiveData<String> incomes = new MutableLiveData("0");
    MutableLiveData<String> stringMessage = new MutableLiveData<>("");
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void getCurrentBalance(MoneyApi moneyApi) {
        Disposable disposable = moneyApi.getCurrentBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balanceResponse -> {
                    countData(balanceResponse);
                }, throwable -> {
                    stringMessage.postValue(throwable.getLocalizedMessage());
                });
        compositeDisposable.add(disposable);
    }

    private void countData(BalanceResponse balanceResponse) {
        incomes.postValue(balanceResponse.incomes);
        expenses.postValue(balanceResponse.expenses);
        balance.postValue(String.valueOf(Integer.parseInt(incomes.getValue()) - Integer.parseInt(expenses.getValue())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
