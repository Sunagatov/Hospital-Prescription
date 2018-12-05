package com.zufar.testtask.ui;

import com.zufar.testtask.ui.layout.DoctorVerticalLayout;
import com.zufar.testtask.ui.layout.PatientVerticalLayout;
import com.zufar.testtask.ui.layout.PrescriptionVerticalLayout;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.*;

@Title("Hospital")
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private DoctorVerticalLayout dctorVerticalLayout = new DoctorVerticalLayout();
    private PatientVerticalLayout patientVerticalLayout = new PatientVerticalLayout();
    private PrescriptionVerticalLayout prescriptionVerticalLayout = new PrescriptionVerticalLayout();

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout rootVerticalLayout = new VerticalLayout(
                dctorVerticalLayout,
                patientVerticalLayout,
                prescriptionVerticalLayout
        );
        setContent(rootVerticalLayout);
        rootVerticalLayout.setMargin(true);
    }

    public PrescriptionVerticalLayout getPrescriptionVerticalLayout() {
        return prescriptionVerticalLayout;
    }
}