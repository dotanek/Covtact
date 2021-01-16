package com.bigpharma.covtact.model;

import java.util.Date;

public class ContactModel {
    private int id;
    private String name;
    private Date date;
    private String note;

    public ContactModel(String name, Date date, String note) {
        this.name = name;
        this.date = date;
        this.note = note;
    }

    public ContactModel(int id, String name, Date date, String note) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
    }
}