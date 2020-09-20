package com.bayraktar.stockcontrol.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bayraktar.stockcontrol.database.model.Stock;

import java.util.List;

@Dao
public interface StockDao {
    @Query("SELECT * FROM stock")
    LiveData<List<Stock>> getAll();

    @Query("SELECT * FROM stock WHERE id IN (:stockIds)")
    LiveData<List<Stock>> getAllByIds(int[] stockIds);

    @Query("SELECT * FROM stock WHERE name LIKE (:name) LIMIT 1")
    Stock findByName(String name);

    @Query("SELECT * FROM stock WHERE id = (:id)")
    LiveData<Stock> findById(int id);

    @Insert
    void insert(Stock stock);

    @Update
    void update(Stock stock);

    @Delete
    void delete(Stock stock);
}
