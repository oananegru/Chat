package com.example.socialnetwork.domain.validators;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.base.ValidationException;
import com.example.socialnetwork.domain.validators.base.Validator;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User user) throws ValidationException {
        if (user.getId() == null) {
            throw new ValidationException("User id should not be null!");
        }
        if (user.getFirstName() == null || "".equals(user.getFirstName())) {
            throw new ValidationException("First name should not be null!");
        }
        if (user.getLastName() == null || "".equals(user.getLastName())) {
            throw new ValidationException("Last name should not be null!");
        }
        if (user.getEmail() == null || "".equals(user.getEmail())) {
            throw new ValidationException("Email should not be null!");
        }
        if (user.getPassword() == null || "".equals(user.getPassword())) {
            throw new ValidationException("Password should not be null");
        }
    }
}
