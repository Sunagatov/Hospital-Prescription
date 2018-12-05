package com.zufar.testtask.ui.window;

import com.zufar.testtask.hsqldb.PatientDao;
import com.vaadin.data.validator.RegexpValidator;
import com.zufar.testtask.hsqldb.DaoFactory;
import com.zufar.testtask.model.Patient;
import com.zufar.testtask.ui.layout.PatientVerticalLayout;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;

import java.sql.SQLException;

public class PatientWindow extends AbstractWindow {

    private TextField firstName = new TextField("First name:");
    private TextField lastName = new TextField("Last name:");
    private TextField patronymic = new TextField("Patronymic:");
    private TextField telephoneNumber = new TextField("Telephone number:");
    private PatientVerticalLayout patientVerticalLayout;
    private Patient selectedPatient;

    public PatientWindow(PatientVerticalLayout doctorVerticalLayout, Patient selectedPatient) {
        this.patientVerticalLayout = doctorVerticalLayout;
        this.selectedPatient = selectedPatient;
        this.verticalFields.addComponents(lastName, firstName, patronymic, telephoneNumber);
        configureComponents();
    }

    private void configureComponents() {
        firstName.focus();
        telephoneNumber.setConverter(new StringToLongConverter());
        telephoneNumber.setNullRepresentation("");
        configureComponentsValidator();
        configureComponentsWidth();
        configureComponentsValidators();
        configureComponentsValidationVisibility();
        configureComponentsRequired();
        configureComponentsTextChangeEventMode();
        configureComponentsChangeListeners();
        configureComponentsMaxLength();
        configureComponentsImmediate();
        configureComponentsValue();
    }

    private void configureComponentsValue() {
        if (selectedPatient == null) {
            return;
        }
        firstName.setValue(selectedPatient.getFirstName());
        lastName.setValue(selectedPatient.getLastName());
        patronymic.setValue(selectedPatient.getPatronymic());
        telephoneNumber.setValue(String.valueOf(selectedPatient.getTelephoneNumber()));
    }

    private void configureComponentsValidator() {
        RegexpValidator wordValidator = new RegexpValidator("[A-Z][a-z]{1,50}", true,
                "Enter the word length from 3 to 50 characters (with a capital letter)");
        firstName.addValidator(wordValidator);
        lastName.addValidator(wordValidator);
        patronymic.addValidator(wordValidator);
        telephoneNumber.addValidator(new LongRangeValidator("Value is negative", 0L, Long.MAX_VALUE));
    }

    private void configureComponentsImmediate() {
        firstName.setImmediate(true);
        lastName.setImmediate(true);
        patronymic.setImmediate(true);
        telephoneNumber.setImmediate(true);
    }

    private void configureComponentsMaxLength() {
        firstName.setMaxLength(50);
        lastName.setMaxLength(50);
        patronymic.setMaxLength(50);
        telephoneNumber.setMaxLength(18);
    }

    private void configureComponentsChangeListeners() {
        firstName.addTextChangeListener(event -> actionAfterTextChangeEvent(event, firstName));
        lastName.addTextChangeListener(event -> actionAfterTextChangeEvent(event, lastName));
        patronymic.addTextChangeListener(event -> actionAfterTextChangeEvent(event, patronymic));
        telephoneNumber.addTextChangeListener(event -> actionAfterTextChangeEvent(event, telephoneNumber));
    }

    private void configureComponentsTextChangeEventMode() {
        firstName.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        lastName.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        patronymic.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        telephoneNumber.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
    }

    private void configureComponentsRequired() {
        firstName.setRequired(true);
        lastName.setRequired(true);
        patronymic.setRequired(true);
        telephoneNumber.setRequired(true);
    }

    private void configureComponentsValidationVisibility() {
        firstName.setValidationVisible(true);
        lastName.setValidationVisible(true);
        patronymic.setValidationVisible(true);
        telephoneNumber.setValidationVisible(true);
    }

    private void configureComponentsValidators() {
        StringLengthValidator stringLengthValidator =
                new StringLengthValidator("Prompt is empty", 1, 50, false);
        telephoneNumber.addValidator(new LongRangeValidator("Value is negative", 0L, Long.MAX_VALUE));
        firstName.addValidator(stringLengthValidator);
        lastName.addValidator(stringLengthValidator);
        patronymic.addValidator(stringLengthValidator);
    }

    private void configureComponentsWidth() {
        firstName.setWidth(String.valueOf(300));
        lastName.setWidth(String.valueOf(300));
        patronymic.setWidth(String.valueOf(300));
        telephoneNumber.setWidth(String.valueOf(300));
    }

    void actionAfterClickOkButton(Button.ClickEvent clickEvent) {
        patientVerticalLayout.disableButtons();
        Patient patient = new Patient(firstName.getValue(), lastName.getValue(), patronymic.getValue(),
                Long.parseLong(telephoneNumber.getConvertedValue().toString()));
        try {
            PatientDao patientDao = DaoFactory.getInstance().getPatientDao();
            if (selectedPatient == null) {
                patientDao.create(patient);
            } else {
                if (selectedPatient.getId() == null) {
                    throw new IllegalArgumentException();
                }
                patient.setId(selectedPatient.getId());
                patientDao.update(patient);
            }
            getUI().getPrescriptionVerticalLayout().refreshGrid(null);
            getUI().getPrescriptionVerticalLayout().refreshPatientFilter();
            patientVerticalLayout.refreshGrid();
            close();
        } catch (SQLException e) {
            Notification.show("Taking action for this patient is impossible!", "There are some problems with the database.",
                    Notification.Type.ERROR_MESSAGE);
        }
    }

    private void actionAfterTextChangeEvent(FieldEvents.TextChangeEvent event, TextField textField) {
        textField.setValue(event.getText());
        textField.setCursorPosition(event.getCursorPosition());
        try {
            firstName.validate();
            lastName.validate();
            patronymic.validate();
            telephoneNumber.validate();
            okButton.setEnabled(true);
        } catch (Validator.InvalidValueException e) {
            okButton.setEnabled(false);
        }
    }
}