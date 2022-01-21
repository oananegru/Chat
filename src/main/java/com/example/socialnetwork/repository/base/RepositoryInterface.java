package com.example.socialnetwork.repository.base;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.result.Result;

import java.io.IOException;

public interface RepositoryInterface <E extends Entity> {

    /**
     * Returns a specified entity
     * @param id - the id of entity to be returned
     * @return com.example.demo.result success with the entity if exists, error com.example.demo.result otherwise
     * @throws IllegalArgumentException if the given id is null
     */
    Result get(Long id) throws IllegalArgumentException;

    /**
     *
     * @return com.example.demo.result status with all entities
     */
    Result getAll();

    /**
     * Add a new entity
     * @param entity - new entity
     * @return success status
     * @throws IllegalArgumentException if the given entity is null
     */
    Result add(E entity) throws IllegalArgumentException, IOException;

    /**
     * Removes the entity with specified id
     * @param id - the id of entity to be deleted
     * @return success status
     * @throws IllegalArgumentException if the given id is null
     */
    Result delete(Long id) throws IllegalArgumentException, IOException;

    /**
     * Updates the entity
     * Replace the entity with same id with given entity
     * @param entity - new entity
     * @return com.example.demo.result status
     * @throws IllegalArgumentException if the given entity is null
     */
    Result update(E entity) throws IllegalArgumentException, IOException;

    /**
     *
     * @return last entity stored
     */
    E getLastEntry();
}
