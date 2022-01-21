package com.example.socialnetwork.domain;

public class UserMessages extends Entity{

    private Long id;
    private Long userId;
    private Long messageId;

    @Override
    public String toString() {
        return "UserMessages{" +
                "id=" + id +
                ", userId=" + userId +
                ", messageId=" + messageId +
                '}';
    }

    public UserMessages(Long id, Long userId, Long messageId) {
        super(id);
        this.userId = userId;
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }


}

