package com.example.socialnetwork.database.base;

import java.sql.SQLException;
import java.util.List;

public interface DaoInterface<E> {

    /**
     * @param id - id for the entity
     * @return - entity with specified id
     * @throws SQLException
     */
    E get(Long id) throws SQLException;

    /**
     * @return all entities
     * @throws SQLException
     */
    List<E> getAll() throws SQLException;

    /**
     * Add new entity
     * @param elem - entity to be added
     * @return number of rows affected
     * @throws SQLException
     */
    int add(E elem) throws SQLException;

    /**
     * Delete specified entity
     * @param id - id for the entity to be deleted
     * @return number of rows affected
     * @throws SQLException
     */
    int delete(Long id) throws SQLException;

    /**
     * Update an existing entity
     * @param elem - entity with new attributes
     * @return number of row affected
     * @throws SQLException
     */
    int update(E elem) throws SQLException;

    /**
     *
     * @return last element inserted
     * @throws SQLException
     */
    E getLast() throws SQLException;
}
