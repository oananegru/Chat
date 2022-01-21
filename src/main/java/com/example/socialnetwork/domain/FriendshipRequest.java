package com.example.socialnetwork.domain;

public class FriendshipRequest extends Entity {

    private Long fromUserId;
    private Long toUserId;
    private String status;

    // Status values
    public static final String PENDING = "pending";
    public static final String APPROVED = "approved";
    public static final String REJECTED = "rejected";

    public FriendshipRequest(Long id, Long fromUserId, Long toUserId) {
        super(id);
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.status = PENDING;
    }

    public FriendshipRequest(Long id, Long fromUserId, Long toUserId, String status) {
        super(id);
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.status = status;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", status='" + status + '\'' +
                '}';
    }
}
