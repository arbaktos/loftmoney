package com.example.loftmoney.screens.budget;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.loftmoney.LoftApp;
import com.example.loftmoney.R;
import com.example.loftmoney.model.Item;
import com.example.loftmoney.remote.ItemRemote;
import com.example.loftmoney.remote.ItemResponse;
import com.example.loftmoney.remote.MoneyApi;
import com.example.loftmoney.screens.budget.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetViewModel extends AndroidViewModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<Item>> itemsLiveList = new MutableLiveData<>();
    public MutableLiveData<String> messageString = new MutableLiveData<>();
    public MutableLiveData<Integer> messageInt = new MutableLiveData<>();
    public MutableLiveData<Boolean> toRefresh = new MutableLiveData<>();

    public BudgetViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadItems(MoneyApi moneyApi, Item.ItemType type, SharedPreferences sharedPreferences) {
        ArrayList<Item> items = new ArrayList<>();
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
        Disposable disposable = moneyApi.getMoneyItems(type.name().toLowerCase(Locale.ROOT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemsRemote -> {
                    for (ItemRemote itemRemote : itemsRemote) {
                        items.add(Item.getInstance(itemRemote));
                        itemsLiveList.postValue(items);
                        toRefresh.postValue(false);
                    }
                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                    toRefresh.postValue(false);
                });
        compositeDisposable.add(disposable);
    }

    public void addItemToRemote(Item inputItem, MoneyApi moneyApi) {
        String appRef = getApplication().getResources().getString(R.string.app_name);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(appRef, 0);
        Disposable disposable = moneyApi.addItem(
                Integer.parseInt(inputItem.getPrice()), inputItem.getName(),
                inputItem.getType().name().toLowerCase(Locale.ROOT),
                sharedPreferences.getString(LoftApp.AUTH_KEY, "")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    messageInt.postValue(R.string.success_on_adding);
                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                });

        compositeDisposable.add(disposable);

    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
