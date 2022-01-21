package com.example.socialnetwork.manager;

import com.example.socialnetwork.domain.User;

public class SessionManager {
    private User user;
    private static final SessionManager instance = new SessionManager();

    private SessionManager(){
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
