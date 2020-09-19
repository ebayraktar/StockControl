package com.bayraktar.stockcontrol.database.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bayraktar.stockcontrol.database.AppDatabase;
import com.bayraktar.stockcontrol.database.dao.StockDao;
import com.bayraktar.stockcontrol.database.model.Stock;

import java.util.List;

public class StockRepository {
    private StockDao stockDao;
    private LiveData<List<Stock>> allStocks;

    public StockRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        stockDao = database.stockDao();
        allStocks = stockDao.getAll();
    }

    public void insert(Stock stock) {
        new InsertStockAsyncTask(stockDao).execute(stock);
    }

    public void update(Stock stock) {
        new UpdateStockAsyncTask(stockDao).execute(stock);
    }

    public void delete(Stock stock) {
        new DeleteStockAsyncTask(stockDao).execute(stock);
    }

    public LiveData<List<Stock>> getAllStocks() {
        return allStocks;
    }

    private static class InsertStockAsyncTask extends AsyncTask<Stock, Void, Void> {

        private StockDao stockDao;

        private InsertStockAsyncTask(StockDao stockDao) {
            this.stockDao = stockDao;
        }

        @Override
        protected Void doInBackground(Stock... stocks) {
            stockDao.insert(stocks[0]);
            return null;
        }
    }

    private static class UpdateStockAsyncTask extends AsyncTask<Stock, Void, Void> {

        private StockDao stockDao;

        private UpdateStockAsyncTask(StockDao stockDao) {
            this.stockDao = stockDao;
        }

        @Override
        protected Void doInBackground(Stock... stocks) {
            stockDao.update(stocks[0]);
            return null;
        }
    }

    private static class DeleteStockAsyncTask extends AsyncTask<Stock, Void, Void> {

        private StockDao stockDao;

        private DeleteStockAsyncTask(StockDao stockDao) {
            this.stockDao = stockDao;
        }

        @Override
        protected Void doInBackground(Stock... stocks) {
            stockDao.delete(stocks[0]);
            return null;
        }
    }
}
