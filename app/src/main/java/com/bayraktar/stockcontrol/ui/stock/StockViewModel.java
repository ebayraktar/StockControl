package com.bayraktar.stockcontrol.ui.stock;

import android.app.Application;
import android.app.Presentation;
import android.media.audiofx.PresetReverb;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bayraktar.stockcontrol.database.model.Stock;
import com.bayraktar.stockcontrol.database.repository.StockRepository;

import java.util.List;

public class StockViewModel extends AndroidViewModel {
    private StockRepository repository;
    private LiveData<List<Stock>> allStocks;

    public StockViewModel(@NonNull Application application) {
        super(application);
        repository = new StockRepository(application);
        allStocks = repository.getAllStocks();
    }

    public void insert(Stock stock) {
        repository.insert(stock);
    }

    public void update(Stock stock) {
        repository.update(stock);
    }

    public void delete(Stock stock) {
        repository.delete(stock);
    }

    public LiveData<List<Stock>> getAllStocks() {
        return allStocks;
    }
}