package com.zufar.testtask.ui.window;

import com.zufar.testtask.hsqldb.PrescriptionDao;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.zufar.testtask.hsqldb.dto.PersonFullName;
import com.zufar.testtask.model.Prescription;
import com.zufar.testtask.hsqldb.DaoFactory;
import com.zufar.testtask.model.Priority;
import com.zufar.testtask.ui.layout.PrescriptionVerticalLayout;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

public class PrescriptionWindow extends AbstractWindow {

    private TextField description = new TextField("Description:");
    private NativeSelect selectPatient = new NativeSelect("Patient:");
    private NativeSelect selectDoctor = new NativeSelect("Doctor:");
    private DateField creationDate = new DateField("Creation's date:", new Date());
    private TextField validity = new TextField("Validity (day's count):");
    private NativeSelect selectPriority = new NativeSelect("Priority:");
    private PrescriptionVerticalLayout prescriptionVerticalLayout;
    private Prescription selectedPrescription;

    public PrescriptionWindow(PrescriptionVerticalLayout prescriptionVerticalLayout, Prescription selectedPrescription) {
        this.prescriptionVerticalLayout = prescriptionVerticalLayout;
        this.selectedPrescription = selectedPrescription;
        this.verticalFields.addComponents(description, selectPatient, selectDoctor, creationDate, validity, selectPriority);
        configureComponents();
    }

    private void configureComponents() {
        description.focus();
        validity.setNullSettingAllowed(false);
        configureComponentsNullRepresentation();
        configureComponentsNullSellectionAllowed();
        configureComponentsWidth();
        configureComponentsValidators();
        configureComponentsValidationVisibility();
        configureComponentsRequired();
        configureComponentsTextChangeEventMode();
        configureComponentsChangeListeners();
        configureComponentsMaxLength();
        configureComponentsConverters();
        configureComponentsImmediate();
        configureComponentsValue();
    }

    private void configureComponentsNullRepresentation() {
        validity.setNullRepresentation("");
        description.setNullRepresentation("");
    }

    private void configureComponentsNullSellectionAllowed() {
        selectPatient.setNullSelectionAllowed(false);
        selectDoctor.setNullSelectionAllowed(false);
        selectPriority.setNullSelectionAllowed(false);
    }

    private void configureComponentsValue() {
        List<PersonFullName> patients;
        List<PersonFullName> doctors;
        try {
            patients = DaoFactory.getInstance().getPatientDao().getAllFullNames();
            selectPatient.addItems(patients);
            if (!patients.isEmpty()) {
                for (PersonFullName patient : patients) {
                    if (patient != null) {
                        selectPatient.setValue(patient);
                        break;
                    }

                }
            }
            doctors = DaoFactory.getInstance().getDoctorDao().getAllFullNames();
            selectDoctor.addItems(doctors);
            if (!doctors.isEmpty()) {
                for (PersonFullName doctor : doctors) {
                    if (doctor != null) {
                        selectDoctor.setValue(doctor);
                        break;
                    }
                }
            }
            selectPriority.addItems(Priority.NORMAL, Priority.SITO, Priority.STATIM);
            selectPriority.setValue(Priority.NORMAL);
        } catch (SQLException e) {
            prescriptionVerticalLayout.disableButtons();
            close();
            return;
        }
        //Если передан selectedPrescription, значит устанавливаем значения для окна EditPrescription
        if (selectedPrescription != null) {
            if (selectedPrescription.getCreationDate() == null || selectedPrescription.getValidity() == null ||
                    selectedPrescription.getPatient() == null || selectedPrescription.getPatient().getId() == null ||
                    selectedPrescription.getDoctor() == null || selectedPrescription.getDoctor().getId() == null) {
                throw new IllegalArgumentException("Some selectedRow's fields are absent!");
            }
            PersonFullName patient = patients.stream().
                    filter(p -> Objects.equals(p.getId(), selectedPrescription.getPatient().getId())).findFirst().get();
            PersonFullName doctor = doctors.stream().
                    filter(d -> Objects.equals(d.getId(), selectedPrescription.getDoctor().getId())).findFirst().get();
            selectPatient.setValue(patient);
            selectDoctor.setValue(doctor);
            description.setValue(selectedPrescription.getDescription());
            creationDate.setValue(Date.from(selectedPrescription.getCreationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            validity.setValue(selectedPrescription.getValidity().toString());
            selectPriority.setValue(selectedPrescription.getPriority());
        }
    }

    private void configureComponentsImmediate() {
        description.setImmediate(true);
        validity.setImmediate(true);
        creationDate.setImmediate(true);
    }

    private void configureComponentsConverters() {
        validity.setConverter(new StringToLongConverter());
        creationDate.setConverter(new StringToDateConverter().getModelType());
    }

    private void configureComponentsMaxLength() {
        description.setMaxLength(50);
        validity.setMaxLength(18);
    }

    private void configureComponentsChangeListeners() {
        selectPriority.addValueChangeListener(event -> actionAfterSelectChangeEvent(event, selectPriority));
        selectPatient.addValueChangeListener(event -> actionAfterSelectChangeEvent(event, selectPatient));
        selectDoctor.addValueChangeListener(event -> actionAfterSelectChangeEvent(event, selectDoctor));
        description.addTextChangeListener(event -> actionAfterTextChangeEvent(event, description));
        validity.addTextChangeListener(event -> actionAfterTextChangeEvent(event, validity));
        creationDate.addValueChangeListener(event -> actionAfterDateChangeEvent());
    }

    private void configureComponentsTextChangeEventMode() {
        description.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        validity.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
    }

    private void configureComponentsRequired() {
        selectPriority.setRequired(true);
        selectPatient.setRequired(true);
        selectDoctor.setRequired(true);
        description.setRequired(true);
        validity.setRequired(true);
    }

    private void configureComponentsValidationVisibility() {
        description.setValidationVisible(true);
        validity.setValidationVisible(true);
        selectPatient.setValidationVisible(true);
        selectDoctor.setValidationVisible(true);
        selectPriority.setValidationVisible(true);
        creationDate.setValidationVisible(true);
    }

    private void configureComponentsValidators() {
        validity.addValidator(new LongRangeValidator("Negative value in field!", 0L, Long.MAX_VALUE));
        creationDate.addValidator(new DateRangeValidator("Wrong date!", null, null, Resolution.DAY));
        description.addValidator(new StringLengthValidator("Field is empty!", 1, 50, false));
    }

    private void configureComponentsWidth() {
        selectPriority.setWidth("300");
        selectPatient.setWidth("300");
        creationDate.setWidth("300");
        selectDoctor.setWidth("300");
        description.setWidth("300");
        validity.setWidth("300");
    }

    void actionAfterClickOkButton(Button.ClickEvent clickEvent) {
        prescriptionVerticalLayout.disableButtons();
        Prescription prescription = new Prescription(
                description.getValue(),
                ((PersonFullName) selectPatient.getValue()),
                ((PersonFullName) selectDoctor.getValue()),
                creationDate.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                (Long) validity.getConvertedValue(),
                Priority.valueOf(selectPriority.getValue().toString())
        );
        try {
            PrescriptionDao prescriptionDao = DaoFactory.getInstance().getPrescriptionDao();
            if (selectedPrescription == null) {
                prescriptionDao.create(prescription);
            } else {
                if (selectedPrescription.getId() == null) {
                    throw new IllegalArgumentException();
                }
                prescription.setId(selectedPrescription.getId());
                prescriptionDao.update(prescription);
            }
            getUI().getPrescriptionVerticalLayout().refreshGrid(null);
            prescriptionVerticalLayout.refreshGrid(null);
            close();
        } catch (SQLException e) {
            Notification.show("Taking action for this prescription is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    private void actionAfterTextChangeEvent(FieldEvents.TextChangeEvent event, TextField textField) {
        textField.setValue(event.getText());
        textField.setCursorPosition(event.getCursorPosition());
        enableButtonByValidityComponents();
    }

    private void actionAfterSelectChangeEvent(Property.ValueChangeEvent event, NativeSelect select) {
        select.setValue(event.getProperty().getValue());
        enableButtonByValidityComponents();
    }

    private void actionAfterDateChangeEvent() {
        if (creationDate.isEmpty()) {
            if (selectedPrescription != null && selectedPrescription.getCreationDate() != null) {
                creationDate.setValue(Date.from(selectedPrescription.getCreationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            } else {
                creationDate.setValue(new Date());
            }
        }
        enableButtonByValidityComponents();
    }

    private void enableButtonByValidityComponents() {
        if (description.getValue() == null || description.getValue().isEmpty() ||
                selectPatient.getValue() == null || selectPatient.getValue().toString().isEmpty() ||
                selectDoctor.getValue() == null || selectDoctor.getValue().toString().isEmpty() ||
                creationDate.getValue() == null || creationDate.getValue().toString().isEmpty() ||
                selectPriority.getValue() == null || selectPriority.getValue().toString().isEmpty()) {
            okButton.setEnabled(false);
            return;
        }
        try {
            creationDate.validate();
            description.validate();
            validity.validate();
            okButton.setEnabled(true);
        } catch (Validator.InvalidValueException e) {
            okButton.setEnabled(false);
        }
    }
}