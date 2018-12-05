package com.zufar.testtask.ui.layout;

import com.zufar.testtask.hsqldb.dto.PersonFullName;
import com.zufar.testtask.model.Prescription;
import com.zufar.testtask.hsqldb.DaoFactory;
import com.zufar.testtask.model.Priority;
import com.zufar.testtask.ui.window.PrescriptionWindow;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.List;

public class PrescriptionVerticalLayout extends AbstractVerticalLayout {

    private TextField descriptionFilter = new TextField("Description:");
    private NativeSelect patientFilter = new NativeSelect("Patient:");
    private NativeSelect priorityFilter = new NativeSelect("Priority:");
    private Button filterButton = new Button("Apply", clickEvent -> {
        try {
            List<Prescription> prescriptions = DaoFactory.getInstance().getPrescriptionDao().getAllByFilters(
                    descriptionFilter.getValue(), (PersonFullName) patientFilter.getValue(), (Priority) priorityFilter.getValue());
            refreshGrid(prescriptions);
        } catch (SQLException e) {
            Notification.show("Filtering is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    });

    public PrescriptionVerticalLayout() {
        configureComponents();
    }

    private void configureComponents() {
        buildLayout();
        addButton.addClickListener(clickEvent -> UI.getCurrent().addWindow(new PrescriptionWindow(this, null)));
        editButton.addClickListener(clickEvent -> {
            if (grid.getSelectedRow() == null) {
                disableButtons();
            } else {
                UI.getCurrent().addWindow(new PrescriptionWindow(this, (Prescription) grid.getSelectedRow()));
            }
        });
        label.setValue("Prescription's list");
        filterButton.setEnabled(false);
        filterButton.setIcon(FontAwesome.CHECK);
        configureFilterComponents();
        disableButtons();
        configureGrid();
        refreshGrid(null);
    }

    private void buildLayout() {
        HorizontalLayout filterLayout = new HorizontalLayout(descriptionFilter, patientFilter, priorityFilter, filterButton);
        filterLayout.setMargin(true);
        filterLayout.setSpacing(true);
        filterLayout.setComponentAlignment(filterButton, Alignment.BOTTOM_RIGHT);
        Panel filterPanel = new Panel("Prescription's filter panel");
        filterPanel.setContent(filterLayout);
        addComponents(label, filterPanel, grid, buttonsLayout);
    }

    private void configureFilterComponents() {
        descriptionFilter.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        priorityFilter.addItems(Priority.NORMAL, Priority.SITO, Priority.STATIM);
        descriptionFilter.setInputPrompt("description");
        priorityFilter.setValue(null);
        configureFilterComponentsImmediate();
        configureFilterComponentsNullSelectionAllowed();
        configureFilterComponentsActionListeners();
        configureFilterComponentsNullSelectionItemIds();
        configureFilterComponentsWidth();
        configureFilterComponentsHeight();
        refreshPatientFilter();
    }

    private void configureFilterComponentsNullSelectionItemIds() {
        patientFilter.setNullSelectionItemId("");
        priorityFilter.setNullSelectionItemId("");
    }

    private void configureFilterComponentsActionListeners() {
        patientFilter.addValueChangeListener(event -> actionAfterSelectChangeEvent(event, patientFilter));
        priorityFilter.addValueChangeListener(event -> actionAfterSelectChangeEvent(event, priorityFilter));
        patientFilter.addContextClickListener(contextClickEvent -> refreshPatientFilter());
        descriptionFilter.addTextChangeListener(event -> actionAfterTextChangeEvent(event, descriptionFilter));
    }

    private void configureFilterComponentsNullSelectionAllowed() {
        patientFilter.setNullSelectionAllowed(true);
        priorityFilter.setNullSelectionAllowed(true);
    }

    private void configureFilterComponentsImmediate() {
        descriptionFilter.setImmediate(true);
        patientFilter.setImmediate(true);
    }

    private void configureFilterComponentsHeight() {
        descriptionFilter.setHeight("30");
        patientFilter.setHeight("30");
        priorityFilter.setHeight("30");
        filterButton.setHeight("30");
    }

    private void configureFilterComponentsWidth() {
        filterButton.setWidth("150");
        descriptionFilter.setWidth("200");
        patientFilter.setWidth("200");
        priorityFilter.setWidth("200");
    }

    private void configureGrid() {
        grid.setContainerDataSource(new BeanItemContainer<>(Prescription.class));
        grid.setColumnOrder("description", "patient", "doctor", "creationDate", "validity", "priority");
        grid.removeColumn("id");
        grid.addSelectionListener(selectionEvent -> {
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        });
    }

    private void actionAfterTextChangeEvent(FieldEvents.TextChangeEvent event, TextField textField) {
        textField.setValue(event.getText());
        textField.setCursorPosition(event.getCursorPosition());
        if (patientFilter.isEmpty() && descriptionFilter.isEmpty() && priorityFilter.isEmpty()) {
            refreshGrid(null);
            filterButton.setEnabled(false);
        } else {
            filterButton.setEnabled(true);
        }
    }

    private void actionAfterSelectChangeEvent(Property.ValueChangeEvent event, NativeSelect select) {
        select.setValue(event.getProperty().getValue());
        if (patientFilter.isEmpty() && descriptionFilter.isEmpty() && priorityFilter.isEmpty()) {
            refreshGrid(null);
            filterButton.setEnabled(false);
        } else {
            filterButton.setEnabled(true);
        }
    }

    public void refreshGrid(List<Prescription> filteredPrescriptions) {
        if (filteredPrescriptions != null) {
            grid.setContainerDataSource(new BeanItemContainer<>(Prescription.class, filteredPrescriptions));
            return;
        }
        try {
            List<Prescription> prescriptions = DaoFactory.getInstance().getPrescriptionDao().getAll();
            grid.setContainerDataSource(new BeanItemContainer<>(Prescription.class, prescriptions));
            grid.setColumnOrder("description", "patient", "doctor", "creationDate", "validity", "priority");
        } catch (SQLException e) {
            Notification.show("Refreshing the grid is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    public void disableButtons() {
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
    }

    public boolean refreshPatientFilter() {
        try {
            patientFilter.removeAllItems();
            patientFilter.setValue(null);
            List<PersonFullName> patientsFullNames = DaoFactory.getInstance().getPatientDao().getAllFullNames();
            patientFilter.addItems(patientsFullNames);
        } catch (SQLException e) {
            return true;
        }
        return false;
    }

    @Override
    void actionAfterClickDeleteButton(Button.ClickEvent event) {
        disableButtons();
        if (grid.getSelectedRow() == null) {
            return;
        }
        try {
            DaoFactory.getInstance().getPrescriptionDao().delete((Prescription) grid.getSelectedRow());
            refreshGrid(null);
        } catch (SQLException e) {
            Notification.show("Deleting this prescription is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }
}