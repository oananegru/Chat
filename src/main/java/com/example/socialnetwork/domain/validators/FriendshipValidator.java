package com.example.socialnetwork.domain.validators;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.validators.base.ValidationException;
import com.example.socialnetwork.domain.validators.base.Validator;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship friendship) throws ValidationException {
        if (friendship.getId() == null) {
            throw new ValidationException("Id must be not null!");
        }
        if (friendship.getIdUser1() == null) {
            throw new ValidationException("Id user 1 must be not null!");
        }
        if (friendship.getIdUser2() == null) {
            throw new ValidationException("Id user 2 must be not null!");
        }
        if (friendship.getDate() == null) {
            throw new ValidationException("Date must be not null!");
        }
    }
}
