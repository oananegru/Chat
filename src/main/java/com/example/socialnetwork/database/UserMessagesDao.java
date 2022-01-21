package com.example.socialnetwork.database;

import com.example.socialnetwork.database.base.Dao;
import com.example.socialnetwork.domain.UserMessages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMessagesDao extends Dao<UserMessages> {
    @Override
    public UserMessages get(Long id) throws SQLException {
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
    public List<UserMessages> getAll() throws SQLException {
        String SQL = "SELECT * FROM usersMessages";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<UserMessages> list = new ArrayList<>();
        while(resultSet.next()) {
            list.add(deserialize(resultSet));
        }
        return list;
    }

    @Override
    public int add(UserMessages elem) throws SQLException {
        String SQL = "INSERT INTO usersMessages (id, user_id, message_id) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getId());
        preparedStatement.setLong(2, elem.getUserId());
        preparedStatement.setLong(3, elem.getMessageId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(Long id) throws SQLException {
        String SQL = "DELETE FROM usersMessages WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement.executeUpdate();
    }


    @Override
    public int update(UserMessages elem) throws SQLException {
        String SQL = "UPDATE usersMessages SET " +
                "user_id = ?," +
                "message_id = ?" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getUserId());
        preparedStatement.setLong(2, elem.getMessageId());
        preparedStatement.setLong(3, elem.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public UserMessages getLast() throws SQLException {
        String SQL = "SELECT * FROM usersMessages ORDER BY id DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    protected UserMessages deserialize(ResultSet resultSet) throws SQLException {
        return new UserMessages(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("message_id")
        );
    }
}
