package com.example.socialnetwork.domain;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class Message extends Entity{
    private String message;
    private Long from;
    private String date;
    private int reply;

    @ConstructorProperties({"id", "message", "from", "date", "reply"})
    public Message(Long id, String message, Long from, String date, int reply) {
        super(id);
        this.message = message;
        this.from = from;
        this.date = date;
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", from='" + from + '\'' +
                ", date=" + date + '\'' +
                ", reply=" + reply +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getReply() {return reply;}

    public void setReply(int reply) {
        this.reply = reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message1 = (Message) o;
        return getReply() == message1.getReply() && Objects.equals(getMessage(), message1.getMessage()) && Objects.equals(getFrom(), message1.getFrom()) && Objects.equals(getDate(), message1.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getFrom(), getDate(), getReply());
    }
}
