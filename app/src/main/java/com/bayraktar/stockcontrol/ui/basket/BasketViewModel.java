package com.bayraktar.stockcontrol.ui.basket;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bayraktar.stockcontrol.database.model.Stock;
import com.bayraktar.stockcontrol.database.repository.StockRepository;

import java.util.List;

public class BasketViewModel extends AndroidViewModel {
    private StockRepository repository;
    private LiveData<List<Stock>> allStocks;

    public BasketViewModel(@NonNull Application application) {
        super(application);
        repository = new StockRepository(application);
        allStocks = repository.getAllStocks();
    }

    public LiveData<List<Stock>> getAllByIds(int[] ids) {
        return repository.getAllByIds(ids);
    }

    public LiveData<List<Stock>> getAllStocks() {
        return allStocks;
    }
}
