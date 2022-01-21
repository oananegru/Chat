package com.example.socialnetwork.repository.base;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.domain.validators.base.Validator;

public abstract class Repository<E extends Entity> implements RepositoryInterface<E> {

    protected Validator<E> validator;

    public Repository(Validator<E> validator) {
        this.validator = validator;
    }
}
