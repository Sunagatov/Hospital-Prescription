package com.zufar.testtask.ui.layout;

import com.zufar.testtask.ui.MainUI;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

abstract class AbstractVerticalLayout extends VerticalLayout {

    Grid grid = new Grid();
    Label label = new Label();
    Button addButton = new Button();
    Button editButton = new Button();
    Button deleteButton = new Button("Delete", this::actionAfterClickDeleteButton);
    HorizontalLayout buttonsLayout = new HorizontalLayout();

    AbstractVerticalLayout() {
        configureCommonComponents();
    }

    abstract void actionAfterClickDeleteButton(Button.ClickEvent event);

    private void configureCommonComponents() {
        addComponents(label, grid, buttonsLayout);
        buttonsLayout.addComponents(addButton, editButton, deleteButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setMargin(true);
        addButton.setCaption("Add");
        editButton.setCaption("Edit");
        addButton.setEnabled(true);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        configureComponentsWidth();
        configureComponentsIcons();
    }

    private void configureComponentsWidth() {
        addButton.setWidth("150");
        editButton.setWidth("150");
        deleteButton.setWidth("150");
    }

    private void configureComponentsIcons() {
        addButton.setIcon(FontAwesome.PLUS);
        editButton.setIcon(FontAwesome.EDIT);
        deleteButton.setIcon(FontAwesome.TRASH);
    }

    @Override
    public MainUI getUI() {
        return (MainUI) super.getUI();
    }
}