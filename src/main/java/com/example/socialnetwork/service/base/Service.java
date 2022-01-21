package com.example.socialnetwork.service.base;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.repository.base.Repository;
import com.example.socialnetwork.result.Result;

import java.io.IOException;

public class Service<E extends Entity> {

    protected final Repository<E> repository;

    public Service(Repository<E> repository) {
        this.repository = repository;
    }

    /**
     * Returns entity by id
     * @param id - the id of entity to be returned
     * @return - com.example.demo.result success with the entity if exists, error com.example.demo.result otherwise
     */
    public Result get(Long id) {
        return repository.get(id);
    }

    /**
     *
     * @return - com.example.demo.result with all entities
     */
    public Result getAll() {
        return repository.getAll();
    }

    /**
     * Delete an entity by id
     * @param id - the id of entity to be deleted
     * @return -
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result delete(Long id) throws IOException {
        return repository.delete(id);
    }
}
