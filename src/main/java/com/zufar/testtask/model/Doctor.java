package com.zufar.testtask.model;

public class Doctor implements Identified<Long> {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String specialization;

    public Doctor(
            String firstName,
            String lastName,
            String patronymic,
            String specialization
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    public Doctor() {
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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
        return this.firstName + " " + this.lastName + " " + this.patronymic + " " + this.specialization;
    }
}