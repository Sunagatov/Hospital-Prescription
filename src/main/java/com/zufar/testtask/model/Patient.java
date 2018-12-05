package com.zufar.testtask.model;

public class Patient implements Identified<Long> {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Long telephoneNumber;

    public Patient() {
    }

    public Patient(
            String firstName,
            String lastName,
            String patronymic,
            Long telephoneNumber
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.telephoneNumber = telephoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Long getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(Long telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long primaryKey) {
        this.id = primaryKey;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.patronymic + " " + this.telephoneNumber;
    }
}