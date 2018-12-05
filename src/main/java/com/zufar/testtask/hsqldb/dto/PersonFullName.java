package com.zufar.testtask.hsqldb.dto;

import com.zufar.testtask.model.Identified;

public class PersonFullName implements Identified<Long> {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;

    public PersonFullName(
             Long id,
             String firstName,
             String lastName,
             String patronymic
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic( String patronymic) {
        this.patronymic = patronymic;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + patronymic;
    }
}