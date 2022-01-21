package com.example.socialnetwork.domain.validators;

import com.example.socialnetwork.domain.UserMessages;
import com.example.socialnetwork.domain.validators.base.ValidationException;
import com.example.socialnetwork.domain.validators.base.Validator;

public class UserMessagesValidator implements Validator<UserMessages> {

    @Override
    public void validate(UserMessages entity) throws ValidationException {
        if (entity.getId() == null) {
            throw new ValidationException("Id must be not ull");
        }
        if (entity.getUserId() == null) {
            throw new ValidationException("Id must be not null");
        }
    }
}
