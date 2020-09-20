package com.bayraktar.stockcontrol.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bayraktar.stockcontrol.database.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    LiveData<List<User>> getAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE name LIKE :name LIMIT 1")
    User findByName(String name);

    @Query("SELECT * FROM user WHERE user_name = :userName LIMIT 1")
    User findByUserName(String userName);

    @Query("SELECT * FROM user WHERE user_name = :userName AND password = :password LIMIT 1")
    User login(String userName, String password);

    @Query("SELECT * FROM user WHERE user_name = :userName LIMIT 1")
    User checkUser(String userName);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
