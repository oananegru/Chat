package com.example.socialnetwork.database;

import com.example.socialnetwork.database.base.Dao;
import com.example.socialnetwork.domain.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageDao extends Dao<Message> {

    @Override
    public Message get(Long id) throws SQLException {
        String SQL = "SELECT * FROM messages WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    public List<Message> getAll() throws SQLException {
        String SQL = "SELECT * FROM messages";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Message> list = new ArrayList<>();
        while(resultSet.next()) {
            list.add(deserialize(resultSet));
        }
        return list;    }

    @Override
    public int add(Message elem) throws SQLException {
        String SQL = "INSERT INTO messages (id, message, \"from\", date, reply) " +
                "VALUES (?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI'), ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, elem.getId());
        preparedStatement.setString(2, elem.getMessage());
        preparedStatement.setLong(3, elem.getFrom());
        preparedStatement.setString(4, elem.getDate());
        preparedStatement.setInt(5, elem.getReply());
        return preparedStatement.executeUpdate();    }

    @Override
    public int delete(Long id) throws SQLException {
        String SQL = "DELETE FROM messages WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(Message elem) throws SQLException {
        String SQL = "UPDATE messages SET " +
                "message = ?," +
                "\"from\" = ?," +
                "date = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI')," +
                "reply = ?" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, elem.getMessage());
        preparedStatement.setLong(2, elem.getFrom());
        preparedStatement.setString(3, elem.getDate());
        preparedStatement.setLong(4, elem.getReply());
        preparedStatement.setLong(5, elem.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public Message getLast() throws SQLException {
        String SQL = "SELECT * FROM messages ORDER BY id DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return deserialize(resultSet);
        }
        return null;
    }

    @Override
    protected Message deserialize(ResultSet resultSet) throws SQLException {
        return new Message(resultSet.getLong("id"),
                resultSet.getString("message"),
                resultSet.getLong("from"),
                resultSet.getTimestamp("date")
                        .toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")),
                resultSet.getInt("reply"));
    }
}
