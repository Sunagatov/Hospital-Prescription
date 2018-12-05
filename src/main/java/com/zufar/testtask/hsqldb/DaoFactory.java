package com.zufar.testtask.hsqldb;

import com.zufar.testtask.dao.ConnectionManager;

import java.sql.SQLException;

public class DaoFactory {

    private static DaoFactory instance = null;

    private DaoFactory() {
    }
    
    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }
    
    public DoctorDao getDoctorDao() throws SQLException{
        return new DoctorDao(ConnectionManager.getConnection());
    }
    
    public PatientDao getPatientDao() throws SQLException {
        return new PatientDao(ConnectionManager.getConnection());
    }
    
    public PrescriptionDao getPrescriptionDao() throws SQLException {
        return new PrescriptionDao(ConnectionManager.getConnection());
    }
}