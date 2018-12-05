package com.zufar.testtask.ui.window;

import com.zufar.testtask.ui.MainUI;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

abstract class AbstractWindow extends Window {

    Button cancelButton = new Button("Cancel", event -> close());
    Button okButton = new Button("Ok", this::actionAfterClickOkButton);
    VerticalLayout verticalFields = new VerticalLayout();

    AbstractWindow() {
        commonActions();
    }

    abstract void actionAfterClickOkButton(Button.ClickEvent clickEvent);

    private void commonActions() {
        verticalFields.setSpacing(true);
        verticalFields.setMargin(true);
        center();
        setModal(true);
        setSizeUndefined();
        HorizontalLayout buttonsLayout = new HorizontalLayout(okButton, cancelButton);
        buttonsLayout.setSpacing(true);
        VerticalLayout verticalMain = new VerticalLayout(verticalFields, buttonsLayout);
        verticalMain.setMargin(true);
        verticalMain.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
        setContent(verticalMain);

        okButton.setEnabled(false);
        cancelButton.setEnabled(true);

        cancelButton.setIcon(FontAwesome.CLOSE);
        okButton.setIcon(FontAwesome.CHECK);

        okButton.setWidth(String.valueOf(120));
        cancelButton.setWidth(String.valueOf(120));
    }

    @Override
    public MainUI getUI() {
        return (MainUI) super.getUI();
    }
}