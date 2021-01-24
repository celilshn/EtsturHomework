package com.cengcelil.phonenumbersappetstur.Models;

import java.io.Serializable;
import java.util.Date;

public class PersonModel implements Serializable {
    int id;
    String name;
    String surname;
    String email;
    String note;
    String country_code;

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname.trim();
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email.trim();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note.trim();
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhone_number() {
        return phone_number.trim();
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBirth_date_str() {
        return birth_date.toString();
    }
    public long getBirth_date() {
        return birth_date.getTime();
    }
    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    String phone_number="a";
    Date birth_date;
}
