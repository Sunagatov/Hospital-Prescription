package com.zufar.testtask.ui.layout;

import com.zufar.testtask.hsqldb.DaoFactory;
import com.zufar.testtask.model.Patient;
import com.zufar.testtask.ui.window.PatientWindow;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.List;

public class PatientVerticalLayout extends AbstractVerticalLayout {

    public PatientVerticalLayout() {
        configureComponents();
    }

    private void configureComponents() {
        addButton.addClickListener(clickEvent -> UI.getCurrent().addWindow(new PatientWindow(this, null)));
        editButton.addClickListener(clickEvent -> {
            if (grid.getSelectedRow() == null) {
                disableButtons();
            } else {
                UI.getCurrent().addWindow(new PatientWindow(this, (Patient) grid.getSelectedRow()));
            }
        });
        label.setValue("Patient's list");
        disableButtons();
        configureGrid();
        refreshGrid();
    }

    private void configureGrid() {
        grid.setContainerDataSource(new BeanItemContainer<>(Patient.class));
        grid.setColumnOrder("lastName", "firstName", "patronymic", "telephoneNumber");
        grid.removeColumn("id");
        grid.addSelectionListener(selectionEvent -> {
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        });
    }

    public void refreshGrid() {
        try {
            List<Patient> patients = DaoFactory.getInstance().getPatientDao().getAll();
            grid.setContainerDataSource(new BeanItemContainer<>(Patient.class, patients));
        } catch (SQLException e) {
            Notification.show("Deleting this doctor is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    void actionAfterClickDeleteButton(Button.ClickEvent event) {
        disableButtons();
        Patient selectedRow = (Patient) grid.getSelectedRow();
        if (selectedRow == null) {
            return;
        }
        try {
            DaoFactory.getInstance().getPatientDao().delete(selectedRow);
            getUI().getPrescriptionVerticalLayout().refreshGrid(null);
            getUI().getPrescriptionVerticalLayout().refreshPatientFilter();
            refreshGrid();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            Notification.show("Deleting this patient is impossible!", "There are some prescriptions which are connected with this patient.",
                    Notification.Type.WARNING_MESSAGE);
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        } catch (SQLException e) {
            Notification.show("Deleting this patient is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    public void disableButtons() {
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
    }
}