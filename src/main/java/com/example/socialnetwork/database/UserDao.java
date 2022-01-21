package com.example.socialnetwork.database;

import com.example.socialnetwork.database.base.Dao;
import com.example.socialnetwork.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends Dao<User> {

    @Override
    public User get(Long id) throws SQLException {
        String SQL = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    public List<User> getAll() throws SQLException {
        String SQL = "SELECT * FROM users";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> list = new ArrayList<>();
        while(resultSet.next()) {
            list.add(deserialize(resultSet));
        }
        return list;
    }

    @Override
    public int add(User elem) throws SQLException {
        String SQL = "INSERT INTO users (id, first_name,last_name, email, password) VALUES(?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getId());
        preparedStatement.setString(2, elem.getFirstName());
        preparedStatement.setString(3, elem.getLastName());
        preparedStatement.setString(4, elem.getEmail());
        preparedStatement.setString(5, elem.getPassword());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(Long id) throws SQLException {
        String SQL = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(User elem) throws SQLException {
        String SQL = "UPDATE users SET " +
                "first_name = ?," +
                "last_name = ?," +
                "email = ?," +
                "password = ?" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getId());
        preparedStatement.setString(2, elem.getFirstName());
        preparedStatement.setString(3, elem.getLastName());
        preparedStatement.setString(4, elem.getEmail());
        preparedStatement.setString(5, elem.getPassword());
        return preparedStatement.executeUpdate();
    }

    @Override
    public User getLast() throws SQLException {
        String SQL = "SELECT * FROM users ORDER BY id DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    protected User deserialize(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
