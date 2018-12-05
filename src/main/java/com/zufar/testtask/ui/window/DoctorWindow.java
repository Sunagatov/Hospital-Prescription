package com.zufar.testtask.ui.window;

import com.zufar.testtask.ui.layout.DoctorVerticalLayout;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.zufar.testtask.hsqldb.DaoFactory;
import com.zufar.testtask.hsqldb.DoctorDao;
import com.zufar.testtask.model.Doctor;
import com.vaadin.event.FieldEvents;
import com.vaadin.data.Validator;

import java.sql.SQLException;

import com.vaadin.ui.*;

public class DoctorWindow extends AbstractWindow {

    private TextField firstName = new TextField("First name:");
    private TextField lastName = new TextField("Last name:");
    private TextField patronymic = new TextField("Patronymic:");
    private TextField specialization = new TextField("Specialization:");
    private DoctorVerticalLayout doctorVerticalLayout;
    private Doctor selectedDoctor;

    public DoctorWindow(DoctorVerticalLayout doctorVerticalLayout, Doctor selectedDoctor) {
        this.doctorVerticalLayout = doctorVerticalLayout;
        this.selectedDoctor = selectedDoctor;
        this.verticalFields.addComponents(lastName, firstName, patronymic, specialization);
        configureComponents();
    }

    private void configureComponents() {
        firstName.focus();
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

    private void configureComponentsValidator() {
        RegexpValidator wordValidator = new RegexpValidator("[A-Z][a-z]{1,50}",true,
                        "Enter the word length from 2 to 50 characters (with a capital letter)");
        firstName.addValidator(wordValidator);
        lastName.addValidator(wordValidator);
        patronymic.addValidator(wordValidator);
        specialization.addValidator(wordValidator);
    }

    private void configureComponentsValue() {
        if (selectedDoctor == null) {
            return;
        }
        firstName.setValue(selectedDoctor.getFirstName());
        lastName.setValue(selectedDoctor.getLastName());
        patronymic.setValue(selectedDoctor.getPatronymic());
        specialization.setValue(selectedDoctor.getSpecialization());
    }

    private void configureComponentsImmediate() {
        firstName.setImmediate(true);
        lastName.setImmediate(true);
        patronymic.setImmediate(true);
        specialization.setImmediate(true);
    }

    private void configureComponentsChangeListeners() {
        firstName.addTextChangeListener(event -> actionAfterTextChangeEvent(event, firstName));
        lastName.addTextChangeListener(event -> actionAfterTextChangeEvent(event, lastName));
        patronymic.addTextChangeListener(event -> actionAfterTextChangeEvent(event, patronymic));
        specialization.addTextChangeListener(event -> actionAfterTextChangeEvent(event, specialization));
    }

    private void configureComponentsRequired() {
        firstName.setRequired(true);
        lastName.setRequired(true);
        patronymic.setRequired(true);
        specialization.setRequired(true);
    }

    private void configureComponentsTextChangeEventMode() {
        firstName.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        lastName.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        patronymic.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        specialization.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
    }

    private void configureComponentsValidationVisibility() {
        firstName.setValidationVisible(true);
        lastName.setValidationVisible(true);
        patronymic.setValidationVisible(true);
        specialization.setValidationVisible(true);
    }

    private void configureComponentsValidators() {
        StringLengthValidator stringLengthValidator =
                new StringLengthValidator("Prompt is empty", 1, 50, false);
        firstName.addValidator(stringLengthValidator);
        lastName.addValidator(stringLengthValidator);
        patronymic.addValidator(stringLengthValidator);
        specialization.addValidator(stringLengthValidator);
    }

    private void configureComponentsMaxLength() {
        firstName.setMaxLength(50);
        lastName.setMaxLength(50);
        patronymic.setMaxLength(50);
        specialization.setMaxLength(50);
    }

    private void configureComponentsWidth() {
        firstName.setWidth(String.valueOf(300));
        lastName.setWidth(String.valueOf(300));
        patronymic.setWidth(String.valueOf(300));
        specialization.setWidth(String.valueOf(300));
    }

    void actionAfterClickOkButton(Button.ClickEvent clickEvent) {
        doctorVerticalLayout.disableButtons();
        Doctor doctor = new Doctor(firstName.getValue(), lastName.getValue(), patronymic.getValue(), specialization.getValue());
        try {
            DoctorDao doctorDao = DaoFactory.getInstance().getDoctorDao();
            if (selectedDoctor == null) {
                doctorDao.create(doctor);
            } else {
                if (selectedDoctor.getId() == null) {
                    throw new IllegalArgumentException();
                }
                doctor.setId(selectedDoctor.getId());
                doctorDao.update(doctor);
            }
            getUI().getPrescriptionVerticalLayout().refreshGrid(null);
            doctorVerticalLayout.refreshGrid();
            close();
        } catch (SQLException e) {
            Notification.show("Taking action for this doctor is impossible!", "There are some problems with the database.",
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
            specialization.validate();
            okButton.setEnabled(true);
        } catch (Validator.InvalidValueException e) {
            okButton.setEnabled(false);
        }
    }
}