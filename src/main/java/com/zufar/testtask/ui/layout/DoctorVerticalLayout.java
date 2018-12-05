package com.zufar.testtask.ui.layout;

import com.zufar.testtask.hsqldb.DaoFactory;
import com.zufar.testtask.model.Doctor;
import com.zufar.testtask.ui.window.DoctorWindow;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.List;

public class DoctorVerticalLayout extends AbstractVerticalLayout {

    private Button statisticsButton = new Button("Statistics", this::actionAfterClickStatisticsButton);

    public DoctorVerticalLayout() {
        configureComponents();
    }

    private void configureComponents() {
        buttonsLayout.addComponents(statisticsButton);
        label.setValue("Doctor's list");
        statisticsButton.setIcon(FontAwesome.BAR_CHART);
        statisticsButton.setWidth("150");
        configureComponentsActionListeners();
        disableButtons();
        configureGrid();
        refreshGrid();
    }

    private void configureComponentsActionListeners() {
        addButton.addClickListener(clickEvent -> UI.getCurrent().addWindow(new DoctorWindow(this, null)));
        editButton.addClickListener(clickEvent -> {
            if (grid.getSelectedRow() == null) {
                disableButtons();
            } else {
                UI.getCurrent().addWindow(new DoctorWindow(this, (Doctor) grid.getSelectedRow()));
            }
        });
    }

    private void configureGrid() {
        grid.setContainerDataSource(new BeanItemContainer<>(Doctor.class));
        grid.setColumnOrder("lastName", "firstName", "patronymic", "specialization");
        grid.removeColumn("id");
        grid.addSelectionListener(selectionEvent -> {
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
            statisticsButton.setEnabled(true);
        });
    }

    public void refreshGrid() {
        try {
            List<Doctor> doctors = DaoFactory.getInstance().getDoctorDao().getAll();
            grid.setContainerDataSource(new BeanItemContainer<>(Doctor.class, doctors));
        } catch (SQLException e) {
            Notification.show("Deleting this doctor is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    void actionAfterClickDeleteButton(Button.ClickEvent event) {
        disableButtons();
        Doctor selectedRow = (Doctor) grid.getSelectedRow();
        if (selectedRow == null) {
            return;
        }
        try {
            DaoFactory.getInstance().getDoctorDao().delete(selectedRow);
            getUI().getPrescriptionVerticalLayout().refreshGrid(null);
            refreshGrid();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            Notification.show("Deleting this doctor is impossible!", "There are some prescriptions which are connected with this doctor.",
                    Notification.Type.WARNING_MESSAGE);
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
            statisticsButton.setEnabled(true);
        } catch (SQLException e) {
            Notification.show("Deleting this doctor is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    private void actionAfterClickStatisticsButton(Button.ClickEvent event) {
        Doctor selectedRow = (Doctor) grid.getSelectedRow();
        if (selectedRow == null || selectedRow.getId() == null) {
            disableButtons();
            return;
        }
        try {
            Long statistics = DaoFactory.getInstance().getDoctorDao().getStatistics(selectedRow.getId());
            Notification.show("Note!", "This doctor made " + statistics + " prescription(s).",
                    Notification.Type.WARNING_MESSAGE);
        } catch (SQLException e) {
            Notification.show("Deleting this doctor is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    public void disableButtons() {
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
        statisticsButton.setEnabled(false);
    }
}