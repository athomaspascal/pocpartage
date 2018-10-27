package com.start;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;

import java.util.Set;

public class ViewChooseServices extends VerticalLayout implements View {
    private final String download="Download file from server";
    private final String upload = "Upload to server";
    private final String sharefiles = "Share Files";
    private final String zipfiles = "Zip files";
    private final String archivefiles ="Archive files";
    private final String launchprogram= "Launch Program";
    private final String deferredprogram = "Deferred Execution Program";
    private final String regularprogram = "Batch Regular Program";


    String test = new String();

    public ViewChooseServices() {
        Label titre = new Label("Choose your services");
        titre.setStyleName("titre");
        Panel panel = new Panel("Service List");

        VerticalLayout vert = new VerticalLayout();


        CheckBoxGroup<String> comboBox = new CheckBoxGroup<>("");
        comboBox.setItems(upload,download,sharefiles,zipfiles,archivefiles,
                launchprogram,deferredprogram,regularprogram);
        Button exec= new Button("Execute");

        exec.addClickListener(event -> {
            String listeDesChoix = "00000000";
            Set<String> listChoix = comboBox.getSelectedItems();
            for(String choix:listChoix)
            {
                if (choix.equalsIgnoreCase(upload))
                    listeDesChoix= "1" +listeDesChoix.substring(1);
                if (choix.equalsIgnoreCase(download))
                    listeDesChoix= listeDesChoix.substring(0,1) + "1" +listeDesChoix.substring(2);
                if (choix.equalsIgnoreCase(sharefiles))
                    listeDesChoix= listeDesChoix.substring(0,2) + "1" +listeDesChoix.substring(3);
                if (choix.equalsIgnoreCase(zipfiles))
                    listeDesChoix= listeDesChoix.substring(0,3) + "1" +listeDesChoix.substring(4);
                if (choix.equalsIgnoreCase(archivefiles))
                    listeDesChoix= listeDesChoix.substring(0,4) + "1" +listeDesChoix.substring(5);
                if (choix.equalsIgnoreCase(launchprogram))
                    listeDesChoix= listeDesChoix.substring(0,5) + "1" +listeDesChoix.substring(6);
                if (choix.equalsIgnoreCase(deferredprogram))
                    listeDesChoix= listeDesChoix.substring(0,6) + "1" +listeDesChoix.substring(7);
                if (choix.equalsIgnoreCase(regularprogram))
                    listeDesChoix= listeDesChoix.substring(0,7) + "1" ;
            }

            this.getUI().getNavigator().navigateTo("Default/Utilisateur=user2&choix=" + listeDesChoix);



        });

        vert.addComponents(comboBox,exec);
        panel.setContent(vert);

        this.setMargin(false);
        addComponents(titre,panel);


    }
}
