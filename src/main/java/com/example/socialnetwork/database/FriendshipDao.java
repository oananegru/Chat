package com.example.socialnetwork.database;

import com.example.socialnetwork.database.base.Dao;
import com.example.socialnetwork.domain.Friendship;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDao extends Dao<Friendship> {

    @Override
    public Friendship get(Long id) throws SQLException {
        String SQL = "SELECT * FROM friendships WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return deserialize(resultSet);
    }

    @Override
    public List<Friendship> getAll() throws SQLException {
        String SQL = "SELECT * FROM friendships";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Friendship> list = new ArrayList<>();
        while(resultSet.next()) {
            list.add(deserialize(resultSet));
        }
        return list;
    }

    @Override
    public int add(Friendship elem) throws SQLException {
        String SQL = "INSERT INTO friendships (id, id_user1, id_user2, register_date) " +
                "VALUES (?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI'))";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getId());
        preparedStatement.setLong(2, elem.getIdUser1());
        preparedStatement.setLong(3, elem.getIdUser2());
        preparedStatement.setString(4, elem.getDate());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(Long id) throws SQLException {
        String SQL =  "DELETE FROM friendships WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(Friendship elem) throws SQLException {
        String SQL = "UPDATE friendships SET " +
                "id_user1 = ?," +
                "id_user2 = ?," +
                "date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI')" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getIdUser1());
        preparedStatement.setLong(2, elem.getIdUser2());
        preparedStatement.setString(3, elem.getDate());
        preparedStatement.setLong(4, elem.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public Friendship getLast() throws SQLException {
        String SQL = "SELECT * FROM friendships ORDER BY id DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    protected Friendship deserialize(ResultSet resultSet) throws SQLException {
        return new Friendship(
                resultSet.getLong("id"),
                resultSet.getLong("id_user1"),
                resultSet.getLong("id_user2"),
                resultSet.getTimestamp("register_date")
                        .toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))
        );
    }
}
