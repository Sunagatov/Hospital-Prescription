package com.zufar.testtask.model;

import com.zufar.testtask.hsqldb.dto.PersonFullName;

import java.time.LocalDate;

public class Prescription implements Identified<Long> {

    private Long id;
    private String description;
    private PersonFullName patient;
    private PersonFullName doctor;
    private LocalDate creationDate;
    private Long validity;
    private Priority priority;

    public Prescription(
            Long id,
            String description,
            PersonFullName patientFullName,
            PersonFullName doctorFullName,
            LocalDate creationDate,
            Long validity,
            Priority priority
    ) {
        this.id = id;
        this.description = description;
        this.patient = patientFullName;
        this.doctor = doctorFullName;
        this.creationDate = creationDate;
        this.validity = validity;
        this.priority = priority;
    }

    public Prescription(
            String description,
            PersonFullName patientFullName,
            PersonFullName doctorFullName,
            LocalDate creationDate,
            Long validity,
            Priority priority
    ) {
        this.description = description;
        this.patient = patientFullName;
        this.doctor = doctorFullName;
        this.creationDate = creationDate;
        this.validity = validity;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public PersonFullName getPatient() {
        return patient;
    }

    public PersonFullName getDoctor() {
        return doctor;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Long getValidity() {
        return validity;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}