package com.example.socialnetwork.database;

import com.example.socialnetwork.database.base.Dao;
import com.example.socialnetwork.domain.FriendshipRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRequestDao extends Dao<FriendshipRequest> {

    @Override
    public FriendshipRequest get(Long id) throws SQLException {
        String SQL = "SELECT * FROM friendship_requests WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    public List<FriendshipRequest> getAll() throws SQLException {
        String SQL = "SELECT * FROM friendship_requests";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<FriendshipRequest> list = new ArrayList<>();
        while(resultSet.next()) {
            list.add(deserialize(resultSet));
        }
        return list;
    }

    @Override
    public int add(FriendshipRequest elem) throws SQLException {
        String SQL = "INSERT INTO friendship_requests (id, from_user_id, to_user_id, status) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getId());
        preparedStatement.setLong(2, elem.getFromUserId());
        preparedStatement.setLong(3, elem.getToUserId());
        preparedStatement.setString(4, elem.getStatus());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(Long id) throws SQLException {
        String SQL = "DELETE FROM friendship_requests WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(FriendshipRequest elem) throws SQLException {
        String SQL = "UPDATE friendship_requests SET " +
                "from_user_id = ?," +
                "to_user_id = ?," +
                "status = ?" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getFromUserId());
        preparedStatement.setLong(2, elem.getToUserId());
        preparedStatement.setString(3, elem.getStatus());
        preparedStatement.setLong(4, elem.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public FriendshipRequest getLast() throws SQLException {
        String SQL = "SELECT * FROM friendship_requests ORDER BY id DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    protected FriendshipRequest deserialize(ResultSet resultSet) throws SQLException {
        return new FriendshipRequest(
                resultSet.getLong("id"),
                resultSet.getLong("from_user_id"),
                resultSet.getLong("to_user_id"),
                resultSet.getString("status")
        );
    }
}
