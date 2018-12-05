package com.zufar.testtask.hsqldb;

import com.zufar.testtask.dao.AbstractCrudDao;
import com.zufar.testtask.hsqldb.dto.PersonFullName;
import com.zufar.testtask.model.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao extends AbstractCrudDao<Doctor, Long> {

    DoctorDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, first_name, last_name, patronymic, specialization FROM DOCTOR";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO DOCTOR (first_name, last_name, patronymic, specialization) VALUES (?, ?, ?, ?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE DOCTOR SET first_name = ?, last_name  = ?, patronymic = ?, specialization = ? WHERE id = ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM DOCTOR WHERE id= ?";
    }

    @Override
    protected List<Doctor> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        while (resultSet.next()) {
            Doctor doctor = new Doctor();
            doctor.setId(resultSet.getLong("id"));
            doctor.setFirstName((resultSet.getString("first_name")));
            doctor.setLastName(resultSet.getString("last_name"));
            doctor.setPatronymic(resultSet.getString("patronymic"));
            doctor.setSpecialization(resultSet.getString("specialization"));
            doctors.add(doctor);
        }
        return doctors;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement preparedStatement, Doctor doctor)
            throws SQLException, IllegalArgumentException {
        if (doctor.getFirstName() == null || doctor.getLastName() == null || doctor.getPatronymic() == null ||
                doctor.getSpecialization() == null || doctor.getId() == null) {
            throw new IllegalArgumentException("Some doctor object's fields are absent!");
        }
        preparedStatement.setString(1, doctor.getFirstName());
        preparedStatement.setString(2, doctor.getLastName());
        preparedStatement.setString(3, doctor.getPatronymic());
        preparedStatement.setString(4, doctor.getSpecialization());
        preparedStatement.setLong(5, doctor.getId());
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement preparedStatement, Doctor doctor)
            throws SQLException, IllegalArgumentException {
        if (doctor.getFirstName() == null || doctor.getLastName() == null || doctor.getPatronymic() == null ||
                doctor.getSpecialization() == null) {
            throw new IllegalArgumentException("Some doctor object's fields are absent!");
        }
        preparedStatement.setString(1, doctor.getFirstName());
        preparedStatement.setString(2, doctor.getLastName());
        preparedStatement.setString(3, doctor.getPatronymic());
        preparedStatement.setString(4, doctor.getSpecialization());
    }

    public List<PersonFullName> getAllFullNames() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, first_name, last_name, patronymic FROM DOCTOR");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<PersonFullName> doctorsFullNames = new ArrayList<>();
        while (resultSet.next()) {
            doctorsFullNames.add(new PersonFullName(
                    resultSet.getLong("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("patronymic")
            ));
        }
        return doctorsFullNames;
    }

    public Long getStatistics(Long primaryKey) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(id) AS prescription_count FROM PRESCRIPTION WHERE doctor_id = ?");
        preparedStatement.setLong(1, primaryKey);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getLong("prescription_count");
        }
        throw new SQLException();
    }
}