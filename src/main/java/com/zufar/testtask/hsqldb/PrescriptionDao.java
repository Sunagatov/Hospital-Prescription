package com.zufar.testtask.hsqldb;

import com.zufar.testtask.dao.AbstractCrudDao;
import com.zufar.testtask.hsqldb.dto.PersonFullName;
import com.zufar.testtask.model.Prescription;
import com.zufar.testtask.model.Priority;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PrescriptionDao extends AbstractCrudDao<Prescription, Long> {

    PrescriptionDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return " SELECT \n" +
                " PRESCRIPTION.id AS id, PRESCRIPTION.description AS description, PRESCRIPTION.creation_date AS creation_date, \n" +
                " PRESCRIPTION.validity AS validity, PRESCRIPTION.priority AS priority, PATIENT.id AS patientID,\n" +
                " PATIENT.first_name AS patientFirstName, PATIENT.last_name AS patientLastName, PATIENT.patronymic AS patientPatronymic,\n" +
                " DOCTOR.id AS doctorID, DOCTOR.first_name AS doctorFirstName, DOCTOR.last_name AS doctorLastName,\n" +
                " DOCTOR.patronymic AS doctorPatronymic FROM PRESCRIPTION JOIN PATIENT ON PRESCRIPTION.patient_id = PATIENT.id \n" +
                " JOIN DOCTOR ON PRESCRIPTION.doctor_id = DOCTOR.id";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO PRESCRIPTION " +
                "(description, patient_id, doctor_id, creation_date, validity, priority) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE PRESCRIPTION SET description = ?, patient_id = ?, doctor_id = ?, creation_date = ?," +
                " validity = ?, priority = ? WHERE id = ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM PRESCRIPTION WHERE id= ?";
    }

    @Override
    protected List<Prescription> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        while (resultSet.next()) {
            Long id = (resultSet.getLong("id"));
            String description = (resultSet.getString("description"));
            PersonFullName patientFullName = new PersonFullName(
                    resultSet.getLong("patientID"),
                    resultSet.getString("patientFirstName"),
                    resultSet.getString("patientLastName"),
                    resultSet.getString("patientPatronymic")
            );
            PersonFullName doctorFullName = new PersonFullName(
                    resultSet.getLong("doctorID"),
                    resultSet.getString("doctorFirstName"),
                    resultSet.getString("doctorLastName"),
                    resultSet.getString("doctorPatronymic")
            );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate creationDate = LocalDate.parse(resultSet.getDate("creation_date").toLocalDate().format(formatter));
            Long validity = resultSet.getLong("validity");
            Priority priority = Priority.valueOf(resultSet.getString("priority"));
            Prescription prescription = new Prescription(id, description, patientFullName, doctorFullName, creationDate, validity, priority);
            prescriptions.add(prescription);
        }
        return prescriptions;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement preparedStatement, Prescription prescription) throws SQLException {
        if (prescription.getId() == null || prescription.getDescription() == null || prescription.getPatient() == null ||
                prescription.getPatient().getId() == null || prescription.getDoctor() == null ||
                prescription.getDoctor().getId() == null || prescription.getCreationDate() == null ||
                prescription.getValidity() == null || prescription.getPriority() == null) {
            throw new IllegalArgumentException("Some prescription object's fields are absent!");
        }
        preparedStatement.setString(1, prescription.getDescription());
        preparedStatement.setLong(2, prescription.getPatient().getId());
        preparedStatement.setLong(3, prescription.getDoctor().getId());
        preparedStatement.setDate(4, Date.valueOf(prescription.getCreationDate()));
        preparedStatement.setLong(5, prescription.getValidity());
        preparedStatement.setString(6, prescription.getPriority().toString());
        preparedStatement.setLong(7, prescription.getId());
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement preparedStatement, Prescription prescription)
            throws SQLException, IllegalArgumentException {
        if (prescription.getDescription() == null || prescription.getPatient() == null ||
                prescription.getPatient().getId() == null || prescription.getDoctor() == null ||
                prescription.getDoctor().getId() == null || prescription.getCreationDate() == null ||
                prescription.getValidity() == null || prescription.getPriority() == null) {
            throw new IllegalArgumentException("Some prescription object's fields are absent!");
        }
        preparedStatement.setString(1, prescription.getDescription());
        preparedStatement.setLong(2, prescription.getPatient().getId());
        preparedStatement.setLong(3, prescription.getDoctor().getId());
        preparedStatement.setDate(4, Date.valueOf(prescription.getCreationDate()));
        preparedStatement.setLong(5, prescription.getValidity());
        preparedStatement.setString(6, prescription.getPriority().toString());
    }

    public List<Prescription> getAllByFilters(String descriptionFilter, PersonFullName patientFilter, Priority priorityFilter)
            throws SQLException {
        StringBuilder filteredSelectQuery = new StringBuilder(getSelectQuery());
        List<String> whereConditions = new ArrayList<>();
        if (descriptionFilter != null && !descriptionFilter.isEmpty()) {
            filteredSelectQuery.append(" AND description LIKE ? ");
            whereConditions.add("%" + descriptionFilter + "%");
        }
        if (patientFilter != null && patientFilter.getId() != null) {
            filteredSelectQuery.append(" AND Patient.id = ? ");
            whereConditions.add(patientFilter.getId().toString());
        }
        if (priorityFilter != null) {
            filteredSelectQuery.append(" AND priority = ? ");
            whereConditions.add(priorityFilter.name());
        }
        PreparedStatement preparedStatement = connection.prepareStatement(filteredSelectQuery.toString());
        for (int i = 0; i < whereConditions.size(); i++) {
            preparedStatement.setString(i + 1, whereConditions.get(i));
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Prescription> prescriptions = new ArrayList<>();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String description = resultSet.getString("description");
            PersonFullName patientFullName = new PersonFullName(
                    resultSet.getLong("PatientID"),
                    resultSet.getString("patientFirstName"),
                    resultSet.getString("patientLastName"),
                    resultSet.getString("patientPatronymic")
            );
            PersonFullName doctorFullName = new PersonFullName(
                    resultSet.getLong("PatientID"),
                    resultSet.getString("doctorFirstName"),
                    resultSet.getString("doctorLastName"),
                    resultSet.getString("doctorPatronymic")
            );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate creationDate = LocalDate.parse(resultSet.getDate("creation_date").toLocalDate().format(formatter), formatter);
            Long validity = resultSet.getLong("validity");
            Priority priority = Priority.valueOf(resultSet.getString("priority"));
            prescriptions.add(new Prescription(id, description, patientFullName, doctorFullName, creationDate, validity, priority));
        }
        return prescriptions;
    }
}