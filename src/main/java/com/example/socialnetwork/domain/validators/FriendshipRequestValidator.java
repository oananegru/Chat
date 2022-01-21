package com.example.socialnetwork.domain.validators;

import com.example.socialnetwork.domain.FriendshipRequest;
import com.example.socialnetwork.domain.validators.base.ValidationException;
import com.example.socialnetwork.domain.validators.base.Validator;

import java.util.List;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) throws ValidationException {
        if (entity.getId() == null) {
            throw new ValidationException("Id must be not ull");
        }
        if (entity.getFromUserId() == null) {
            throw new ValidationException("Id must be not null");
        }
        if (entity.getToUserId() == null) {
            throw new ValidationException("Id must be not null");
        }
        if (!List.of(FriendshipRequest.APPROVED, FriendshipRequest.REJECTED, FriendshipRequest.PENDING).contains(entity.getStatus())) {
            throw new ValidationException("Invalid status!");
        }
    }
}
