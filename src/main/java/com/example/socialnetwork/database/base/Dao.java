package com.example.socialnetwork.database.base;

import com.example.socialnetwork.database.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Dao<E> implements DaoInterface<E> {

    protected Connection connection;

    public Dao() {
        this.connection = new DBConnect().connection();
    }

    protected abstract E deserialize(ResultSet resultSet) throws SQLException;
}
