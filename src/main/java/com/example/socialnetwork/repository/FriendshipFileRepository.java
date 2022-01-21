package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.validators.base.Validator;
import com.example.socialnetwork.repository.base.FileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class FriendshipFileRepository extends FileRepository<Friendship> {

    public FriendshipFileRepository(Validator<Friendship> validator, String file) throws IOException {
        super(validator, file);
    }

    @Override
    protected ArrayList<Friendship> deserialize(ArrayList<Object> list) {
        return new ObjectMapper().convertValue(list, new TypeReference<>(){});
    }
}
