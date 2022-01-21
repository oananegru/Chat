package com.example.socialnetwork.domain;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class Friendship extends Entity {

    private final Long idUser1;
    private final Long idUser2;
    private final String date;

    @ConstructorProperties({"id", "idUser1", "idUser2", "date"})
    public Friendship(Long id, Long idUser1, Long idUser2, String date) {
        super(id);
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.date = date;
    }

    public Long getIdUser1() {
        return idUser1;
    }

    public Long getIdUser2() {
        return idUser2;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id='" + id +'\'' +
                ", idUser1=" + idUser1 +
                ", idUser2=" + idUser2 +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(idUser1, that.idUser1) && Objects.equals(idUser2, that.idUser2) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser1, idUser2, date);
    }
}
