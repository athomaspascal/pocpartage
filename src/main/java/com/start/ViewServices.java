package com.start;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ViewServices extends Panel implements View {
    Label userLabel;
    String choixServices = "00000000";
    VerticalLayout mainLayout = new VerticalLayout();

    public ViewServices() {
        userLabel = new Label("User");
        mainLayout.addComponents(userLabel);
        this.setContent(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(event.getParameters() != null){
            // split at "/", add each part as a label
            String[] msgs = event.getParameters().split("&");
            for (String msg : msgs) {
                if (msg.indexOf("Utilisateur") ==0 )
                {
                    Label user= new Label((msg));
                    user.setStyleName("titre");
                    ((Layout)getContent()).replaceComponent(userLabel,user);
                }
                if (msg.indexOf("choix") ==0 ) {
                    int posValue= msg.indexOf("=")+1;
                    this.choixServices = msg.substring(posValue);

                    ListServicesComponent list = new ListServicesComponent();
                    list.init("basic",choixServices,this);
                    mainLayout.setResponsive(true);
                    //mainLayout.setMargin(false);
                    mainLayout.addComponents(list);
                }

            }
        }
    }
}