package com.example.socialnetwork.domain.validators;

import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.validators.base.ValidationException;
import com.example.socialnetwork.domain.validators.base.Validator;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message message) throws ValidationException {
        if (message.getId() == null) {
            throw new ValidationException("Id must be not null!");
        }
        if (message.getFrom() == null) {
            throw new ValidationException("Id from must be not null!");
        }
        if (message.getDate() == null) {
            throw new ValidationException("Date must be not null!");
        }
    }
}
