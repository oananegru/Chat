package com.example.socialnetwork.repository.base;

import com.example.socialnetwork.database.base.Dao;
import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.domain.validators.base.Validator;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DatabaseRepository<E extends Entity> extends Repository<E> {

    private final Dao<E> dao;

    public DatabaseRepository(Validator<E> validator, Dao<E> dao) {
        super(validator);
        this.dao = dao;
    }

    @Override
    public Result get(Long id) throws IllegalArgumentException {
        try {
            E elem = dao.get(id);
            if (elem != null) {
                return new ResultSuccess<>(elem);
            } else {
                return new ResultError("Entity not found!");
            }
        } catch (SQLException e) {
            return new ResultError(e.getMessage());
        }
    }

    @Override
    public Result getAll() {
        try {
            List<E> elems = dao.getAll();
            return new ResultSuccess<>(elems);
        } catch (SQLException e) {
            return new ResultError(e.getMessage());
        }
    }

    @Override
    public Result add(E entity) throws IllegalArgumentException, IOException {
        try {
            validator.validate(entity);
            if (dao.add(entity) > 0) {
                return new ResultSuccess<>();
            } else {
                return new ResultError("Entity already exists!");
            }
        } catch (SQLException e) {
            return new ResultError(e.getMessage());
        }
    }

    @Override
    public Result delete(Long id) throws IllegalArgumentException {
        try {
            if (dao.delete(id) > 0) {
                return new ResultSuccess<>();
            } else {
                return new ResultError("Entity does not exist!");
            }
        } catch (SQLException e) {
            return new ResultError(e.getMessage());
        }
    }

    @Override
    public Result update(E entity) throws IllegalArgumentException {
        try {
            validator.validate(entity);
            if (dao.update(entity) > 0) {
                return new ResultSuccess<>();
            } else {
                return new ResultError("Entity does not exist!");
            }
        } catch (SQLException e) {
            return new ResultError(e.getMessage());
        }
    }

    @Override
    public E getLastEntry() {
        try {
            return dao.getLast();
        } catch (SQLException e) {
            return null;
        }
    }
}
