package com.example.loftmoney.screens.budget;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.loftmoney.LoftApp;
import com.example.loftmoney.R;
import com.example.loftmoney.model.Item;
import com.example.loftmoney.remote.ItemRemote;
import com.example.loftmoney.remote.MoneyApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BudgetViewModel extends AndroidViewModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<Item>> itemsLiveList = new MutableLiveData<>(Collections.emptyList());
    public MutableLiveData<String> messageString = new MutableLiveData<>();
    public MutableLiveData<Integer> messageInt = new MutableLiveData<>();
    public MutableLiveData<Boolean> toRefresh = new MutableLiveData<>();
    public MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);
    public MutableLiveData<Integer> selectedItemCounter = new MutableLiveData<>(0);

    public BudgetViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadItems(MoneyApi moneyApi, Item.ItemType type, SharedPreferences sharedPreferences) {
        ArrayList<Item> items = new ArrayList<>();
        items.add( new Item("cat", "30", Item.ItemType.EXPENSE));
        items.add( new Item("caterpillar", "750", Item.ItemType.EXPENSE));
        items.add( new Item("creo", "4000", Item.ItemType.EXPENSE));
        items.add( new Item("salary", "8390", Item.ItemType.INCOME));
        items.add( new Item("dept", "156", Item.ItemType.INCOME));
        itemsLiveList.postValue(items);
//        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
//        Disposable disposable = moneyApi.getMoneyItems(type.name().toLowerCase(Locale.ROOT))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(itemsRemote -> {
//                    for (ItemRemote itemRemote : itemsRemote) {
//                        items.add(Item.getInstance(itemRemote));
//                        itemsLiveList.postValue(items);
//                        toRefresh.postValue(false);
//                    }
//                }, throwable -> {
//                    messageString.postValue(throwable.getLocalizedMessage());
//                    toRefresh.postValue(false);
//                });
//        compositeDisposable.add(disposable);
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

    public void countSelected() {
        int counter = 0;
        for (Item item: itemsLiveList.getValue()) {
            if (item.getSelected()) {
                counter++;
            }
        }
        selectedItemCounter.postValue(counter);
    }



    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
