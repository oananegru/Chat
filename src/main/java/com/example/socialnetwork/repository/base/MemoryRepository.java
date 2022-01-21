package com.example.socialnetwork.repository.base;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.domain.validators.base.Validator;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MemoryRepository<E extends Entity> extends Repository<E> {

    protected Map<Long, E> entities;

    public MemoryRepository(Validator<E> validator) throws IOException {
        super(validator);
        this.entities = new HashMap<>();
    }

    @Override
    public Result get(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id must be not null!");
        }
        E entity = entities.get(id);
        if (entity != null) {
            return new ResultSuccess<>(entity);
        } else {
            return new ResultError("Entity not found!");
        }
    }

    @Override
    public Result getAll() {
        return new ResultSuccess<>(entities.values());
    }

    @Override
    public Result add(E entity) throws IllegalArgumentException, IOException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must be not null!");
        }
        if (entities.get(entity.getId()) != null) {
            return new ResultError("Entity already exists!");
        } else {
            entities.put(entity.getId(), entity);
            return new ResultSuccess<>();
        }
    }

    @Override
    public Result delete(Long id) throws IllegalArgumentException, IOException {
        if (id == null) {
            throw new IllegalArgumentException("Id must be not null!");
        }
        if (entities.get(id) == null) {
            return new ResultError("Entity does not exist!");
        } else {
            entities.remove(id);
            return new ResultSuccess<>();
        }
    }

    @Override
    public Result update(E entity) throws IllegalArgumentException, IOException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must be not null!");
        }
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return new ResultSuccess<>();
        } else {
            return new ResultError("Entity does not exist!");
        }
    }

    public E getLastEntry() {
        if (!entities.isEmpty()) {
            return (E) entities.values().toArray()[entities.size() - 1];
        }
        return null;
    }
}
