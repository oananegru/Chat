package com.example.socialnetwork.domain.validators.base;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
