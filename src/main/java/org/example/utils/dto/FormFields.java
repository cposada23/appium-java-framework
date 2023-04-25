package org.example.utils.dto;

import org.example.utils.enums.Gender;

public class FormFields {

    private String userName;
    private Gender gender;
    private String country;

    public FormFields(String userName, Gender gender, String country) {
        this.userName = userName;
        this.gender = gender;
        this.country = country;
    }

    public String getUserName() {
        return userName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }
}
