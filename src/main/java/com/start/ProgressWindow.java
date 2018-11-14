package com.start;

import com.vaadin.ui.*;
import ssh.UserAuthKI;

import javax.swing.text.StyleConstants;

public class ProgressWindow extends Window {
    ProgressBar progressBar ;
    VerticalLayout layout = new VerticalLayout();

    private Label currentStatus;

    public ProgressWindow() {
        center();
        setVisible(true);
        setResizable(false);
        setDraggable(false);
        //setImmediate(true);
        setModal(true);
        setClosable(false);
        setCaption("Command is running");


        layout.setMargin(true);
        layout.setWidth("250");

        currentStatus = new Label();
        currentStatus.addStyleName(String.valueOf(StyleConstants.ALIGN_CENTER));
        currentStatus.setSizeFull();
        //this.currentStatus.setImmediate(true);

        progressBar = new ProgressBar();
        progressBar.setSizeFull();
        progressBar.setIndeterminate(true);
        //progressBar.setImmediate(true);
        progressBar.setVisible(true);

        layout.addComponent(progressBar);
        layout.addComponent(this.currentStatus);
        layout.setComponentAlignment(this.currentStatus, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }



    public void execCommand(String[] args,boolean stopAfterConnecte,String command,boolean closeFactory)
    {

        new Thread(() -> {
            UserAuthKI userAuthKI = new UserAuthKI();
            userAuthKI.connect(args,stopAfterConnecte,command,closeFactory);
            updateStatus("Status:" + userAuthKI.getExitStatus());

            focus();
        }).start();


    }



    public void updateStatus(String status) {
        this.currentStatus.setCaption(status);
        layout.removeComponent(progressBar);
        Button close = new Button("Close",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        this.setCaption("Command Finish");

        layout.addComponent(close);
        layout.setComponentAlignment(close,Alignment.MIDDLE_CENTER);
        getUI().push();
    }



}
