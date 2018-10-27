package com.start;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ViewDefault extends Panel implements View {
    Label userLabel;
    String choixServices;
    public ViewDefault() {
        VerticalLayout mainLayout = new VerticalLayout();
        userLabel = new Label("User");
        ListServicesComponent u = new ListServicesComponent();
        u.init("basic",choixServices);
        mainLayout.addComponents(userLabel,u);
        mainLayout.setMargin(false);
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
                if (msg.indexOf("Choix") ==0 ) {
                    int posValue= msg.indexOf("=");
                    this.choixServices = msg.substring(posValue);
                }

            }
        }
    }
}