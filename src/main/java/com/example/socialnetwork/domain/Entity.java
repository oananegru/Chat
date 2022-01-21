package com.example.socialnetwork.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Entity implements Serializable {

    protected final Long id;

    @ConstructorProperties("id")
    public Entity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
