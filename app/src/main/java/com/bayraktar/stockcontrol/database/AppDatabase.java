package com.bayraktar.stockcontrol.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bayraktar.stockcontrol.database.model.*;
import com.bayraktar.stockcontrol.database.dao.*;

@Database(entities = {User.class, Stock.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();

    public abstract StockDao stockDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private StockDao stockDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            stockDao = db.stockDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            stockDao.insert(new Stock("Ülker Milk", "60 g", "https://www.ulker.com.tr/documents/Ulker/Urunler/ulker-cikolata-sutlu-kare.png", 4.7f));
            stockDao.insert(new Stock("Ülker Bitter", "60 g", "https://www.ulker.com.tr/documents/Ulker/Urunler/ulker-cikolata-bitter-kare-60.png", 4.7f));
            stockDao.insert(new Stock("Tadım Peanut", "30 g", "https://www.tadim.com/en/timthumb.php?src=uploads/urunresim/647514837-.png&h=238&w=155&zc=2", 1.75f));
            stockDao.insert(new Stock("Tadım Pistachio", "30 g", "https://www.tadim.com/en/timthumb.php?src=uploads/urunresim/1797865549-.png&h=238&w=155&zc=2", 5.45f));
            stockDao.insert(new Stock("Nesfit Strawberry", "23.5 g", "https://st1.myideasoft.com/idea/eg/61/myassets/products/161/nesfit-bar-cilekli-820x1094_min.png?revision=1585762948", 2.5f));
            return null;
        }
    }
}
