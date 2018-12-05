package com.zufar.testtask.hsqldb;

import com.zufar.testtask.dao.AbstractCrudDao;
import com.zufar.testtask.hsqldb.dto.PersonFullName;
import com.zufar.testtask.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDao extends AbstractCrudDao<Patient, Long> {

    PatientDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, first_name, last_name, patronymic, telephone_number FROM PATIENT";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO PATIENT (first_name, last_name, patronymic, telephone_number) VALUES (?, ?, ?, ?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE PATIENT SET first_name = ?, last_name  = ?, patronymic = ?, telephone_number = ? WHERE id = ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM PATIENT WHERE id = ?";
    }

    @Override
    protected List<Patient> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        while (resultSet.next()) {
            Patient patient = new Patient();
            patient.setId(resultSet.getLong("id"));
            patient.setFirstName((resultSet.getString("first_name")));
            patient.setLastName(resultSet.getString("last_name"));
            patient.setPatronymic(resultSet.getString("patronymic"));
            patient.setTelephoneNumber(resultSet.getLong("telephone_number"));
            patients.add(patient);
        }
        return patients;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement preparedStatement, Patient patient)
            throws SQLException, IllegalArgumentException {
        if (patient.getFirstName() == null || patient.getLastName() == null || patient.getPatronymic() == null ||
                patient.getTelephoneNumber() == null || patient.getId() == null) {
            throw new IllegalArgumentException("Some patient object's fields are absent!");
        }
        preparedStatement.setString(1, patient.getFirstName());
        preparedStatement.setString(2, patient.getLastName());
        preparedStatement.setString(3, patient.getPatronymic());
        preparedStatement.setLong(4, patient.getTelephoneNumber());
        preparedStatement.setLong(5, patient.getId());
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement preparedStatement, Patient patient)
            throws SQLException, IllegalArgumentException {
        if (patient.getFirstName() == null || patient.getLastName() == null || patient.getPatronymic() == null ||
                patient.getTelephoneNumber() == null) {
            throw new IllegalArgumentException("Some patient object's fields are absent!");
        }
        preparedStatement.setString(1, patient.getFirstName());
        preparedStatement.setString(2, patient.getLastName());
        preparedStatement.setString(3, patient.getPatronymic());
        preparedStatement.setLong(4, patient.getTelephoneNumber());
    }


    public List<PersonFullName> getAllFullNames() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, first_name, last_name, patronymic FROM PATIENT");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<PersonFullName> patientsFullNames = new ArrayList<>();
        while (resultSet.next()) {
            patientsFullNames.add(new PersonFullName(
                    resultSet.getLong("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("patronymic")
            ));
        }
        return patientsFullNames;
    }
}