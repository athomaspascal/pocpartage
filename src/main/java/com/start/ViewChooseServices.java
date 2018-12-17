package com.start;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import dap.entities.services.Services;
import dap.entities.services.ServicesRepository;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class ViewChooseServices extends VerticalLayout implements View {
    private static final Logger logger = Logger.getLogger(ViewChooseServices.class.getName());

    String test = new String();

    public ViewChooseServices() {
        Label titre = new Label("Choose your services");
        titre.setStyleName("titre");
        this.setMargin(false);
        Panel panel = setServicesByTable();
        addComponents(titre,panel);
    }


    private Panel setServicesByTable()
    {
        Panel listServices = new Panel("List Services");
        List<Services> allServices = ServicesRepository.findAll();
        String firstFamilyService="";

        TreeData<String> data = new TreeData<>();
        Tree<String> tree = new Tree<>("Services");
        int n =0;

        for (Services myService:allServices)
        {
            if (!firstFamilyService.equalsIgnoreCase(myService.getServiceFamily()))
            {
                data.addItems(null, myService.getServiceFamily());
                firstFamilyService= myService.getServiceFamily();
            }
            data.addItems(firstFamilyService,myService.getServiceName());
        }

        tree.setDataProvider(new TreeDataProvider<>(data));
        for (Services myService:allServices)
        {
            if (!firstFamilyService.equalsIgnoreCase(myService.getServiceFamily()))
            {
                tree.expand(myService.getServiceFamily());
            }
        }

        tree.setItemIconGenerator(item -> {
            if (item.equals("FILE")) {
                return VaadinIcons.FILE;
            } else if (item.equals("PROGRAM")) {
                return VaadinIcons.FILE_PROCESS;
            } else if (item.equals("STOCKAGE")) {
                return VaadinIcons.FILE_ZIP;
            }
            return null;
        });

        tree.addItemClickListener(itemClick ->
        {
            logger.info("Item:" +itemClick.getItem());

        });


        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(tree);
        listServices.setContent(verticalLayout);
        return listServices;
    }

    private Panel setServices()
    {
        String download="Download file from server";
        String upload = "Upload to server";
        String sharefiles = "Share Files";
        String zipfiles = "Zip files";
        String archivefiles ="Archive files";
        String launchprogram= "Launch Program";
        String deferredprogram = "Deferred Execution Program";
        String regularprogram = "Batch Regular Program";
        String listprogram = "List Runnning Programs";
        String killprogram = "Kill Runnning Programs";


        Panel panel = new Panel("Service List");

        VerticalLayout vert = new VerticalLayout();


        CheckBoxGroup<String> comboBox = new CheckBoxGroup<>("");
        comboBox.setItems(upload,download,sharefiles,zipfiles,archivefiles,
                launchprogram,deferredprogram,regularprogram,listprogram,killprogram);
        Button exec= new Button("Execute");

        exec.addClickListener(event -> {
            String listeDesChoix = "0000000000";
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
                    listeDesChoix= listeDesChoix.substring(0,7) + "1" +listeDesChoix.substring(8);
                if (choix.equalsIgnoreCase(listprogram))
                    listeDesChoix= listeDesChoix.substring(0,8) + "1" +listeDesChoix.substring(9);
                if (choix.equalsIgnoreCase(killprogram))
                    listeDesChoix= listeDesChoix.substring(0,9) + "1" +listeDesChoix.substring(10);
            }

            this.getUI().getNavigator().navigateTo("Default/Utilisateur=pascal&choix=" + listeDesChoix);



        });

        vert.addComponents(comboBox,exec);
        panel.setContent(vert);
        return panel;

    }
}
