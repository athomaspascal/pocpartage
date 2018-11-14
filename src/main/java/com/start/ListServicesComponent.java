package com.start;


import com.BookExample.TheServices;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.*;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadinupload.AdvancedFileDownloader;
import com.vaadinupload.FileSystemDataProvider;
import com.vaadinupload.ImageReceiver;
import dap.entities.JPAService;
import dap.entities.actions.Servers;
import dap.entities.actions.ServersRepository;
import dap.entities.share.ShareSpace;
import dap.entities.share.shareRepository;
import dap.entities.team.Team;
import dap.entities.user.User;
import dap.entities.user.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import ssh.UserAuthKI;
import zip.ZipMultipleFiles;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class ListServicesComponent extends CustomComponent implements TheServices {
    private static final Logger logger = Logger.getLogger(AdvancedFileDownloader.class.getName());
    private TreeGrid treeGrid;
    private TreeGrid treeZipGrid;
    private String user;
    VerticalLayout vLayoutDownload = new VerticalLayout();
    HorizontalLayout mainLayout = new HorizontalLayout();
    private String hostname;

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    ViewServices myParentView;
    EntityManager emTest = JPAService.getFactory().createEntityManager();

    ListServicesComponent(String myUser){
        user = myUser;

    }

    public void init(String context, String choixServices, ViewServices parentView) {
        myParentView = parentView;
        if (choixServices!= null && choixServices.substring(0,1).equalsIgnoreCase("1"))
        {
            if ("basic".equals(context))
                basic();
            else if ("advanced".equals(context))
                advanced();
            else
                mainLayout.addComponent(new Label("Invalid context: " + context));
        }
        if (choixServices!= null && choixServices.substring(1,2).equalsIgnoreCase("1"))
            downloadComponent();
        if (choixServices!= null && choixServices.substring(2,3).equalsIgnoreCase("1"))
           sharingComponent();
        if (choixServices!= null && choixServices.substring(3,4).equalsIgnoreCase("1"))
            zipComponent();
        if (choixServices!= null && choixServices.substring(5,6).equalsIgnoreCase("1"))
            launchComponent();
        setCompositionRoot(mainLayout);

    }


    public void basic() {
        final Image image = new Image("");
        image.setVisible(false);
        ImageReceiver receiver = new ImageReceiver(image);
        // Create the upload with a caption and set receiver later
        final Upload upload = new Upload("Clik on the Button", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);

        // Prevent too big downloads
        final long UPLOAD_LIMIT = 1000000l;
        upload.addStartedListener(new StartedListener() {
            private static final long serialVersionUID = 4728847902678459488L;

            @Override
            public void uploadStarted(StartedEvent event) {
                if (event.getContentLength() > UPLOAD_LIMIT) {
                    Notification.show("Too big file",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Check the size also during progress
        upload.addProgressListener(new ProgressListener() {
            private static final long serialVersionUID = 8587352676703174995L;

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                if (readBytes > UPLOAD_LIMIT) {
                    Notification.show("Too big file",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Put the components in a panel
        Panel panel = new Panel("Upload File on the server");
        panel.setResponsive(true);
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setMargin(true);
        List<String> data = getTargetList();
        RadioButtonGroup<String> targetGroup = new RadioButtonGroup<String>("Select a Target Group ", data);
        targetGroup.setItemCaptionGenerator(item -> "Group " + item);
        targetGroup.setSelectedItem(data.get(0));

        //mainLayout.addComponent(targetGroup);
        panelContent.addComponents(upload, image, targetGroup);
        panel.setContent(panelContent);

        File uploads = getFileDirectoryUpload();
        if (!uploads.exists() && !uploads.mkdir())
            mainLayout.addComponent(new Label("ERROR: Could not create upload dir"));

        ((VerticalLayout) panel.getContent()).setSpacing(true);
        panel.setWidth("-1");
        panel.setResponsive(true);

        /*
        Window uploadWindow = new Window();
        uploadWindow.setContent(panel);
        **/

        DragSourceExtension<Panel> myDragSource = new DragSourceExtension<>(panel);

        // set the allowed effect
        myDragSource.setEffectAllowed(EffectAllowed.MOVE);
        // set the text to transfer
        myDragSource.setDataTransferText("hello receiver");
        // set other data to transfer (in this case HTML)
        myDragSource.setDataTransferData("text/html", "<label>hello receiver</label>");

        DropTargetExtension<HorizontalLayout> dropTarget = new DropTargetExtension<>(mainLayout);
        dropTarget.setDropEffect(DropEffect.MOVE);

        dropTarget.addDropListener(event -> {
            // if the drag source is in the same UI as the target
            Optional<AbstractComponent> dragSource = event.getDragSourceComponent();
            if (dragSource.isPresent() && dragSource.get() instanceof Label) {
                // move the label to the layout
                mainLayout.addComponent(dragSource.get());

                /*
                // get possible transfer data
                String message = event.getDataTransferData("text/html");
                if (message != null) {
                    Notification.show("DropEvent with data transfer html: " + message);
                } else {
                    // get transfer text
                    message = event.getDataTransferText();
                    Notification.show("DropEvent with data transfer text: " + message);
                }
                */

                // handle possible server side drag data, if the drag source was in the same UI
                /*
                event.getDragData().ifPresent(eventData -> handleMyDragData((MyObject) eventData));
                */
            }
        });


        mainLayout.addComponent(panel);

    }

    public void advanced() {
        // BEGIN-EXAMPLE: component.upload.advanced
        class UploadBox extends CustomComponent
                implements Receiver, ProgressListener,
                FailedListener, SucceededListener {
            private static final long serialVersionUID = -46336015006190050L;

            // Put upload in this memory buffer that grows automatically
            ByteArrayOutputStream os =
                    new ByteArrayOutputStream(10240);

            // Name of the uploaded file
            String filename;

            ProgressBar progress = new ProgressBar(0.0f);

            // Show uploaded file in this placeholder
            Image image = new Image("Uploaded Image");

            public UploadBox() {
                // Create the upload component and handle all its events
                Upload upload = new Upload("Upload the image here", null);
                upload.setReceiver(this);
                upload.addProgressListener(this);
                upload.addFailedListener(this);
                upload.addSucceededListener(this);

                // Put the upload and image display in a panel
                Panel panel = new Panel("Upload Document on the server");
                panel.setWidth("400px");
                VerticalLayout panelContent = new VerticalLayout();
                panelContent.setSpacing(true);
                panel.setContent(panelContent);
                panelContent.addComponent(upload);
                panelContent.addComponent(progress);
                panelContent.addComponent(image);

                progress.setVisible(false);
                image.setVisible(false);

                setCompositionRoot(panel);
            }

            public OutputStream receiveUpload(String filename, String mimeType) {
                this.filename = filename;
                os.reset(); // Needed to allow re-uploading
                return os;
            }

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                progress.setVisible(true);
                if (contentLength == -1)
                    progress.setIndeterminate(true);
                else {
                    progress.setIndeterminate(false);
                    progress.setValue(((float) readBytes) /
                            ((float) contentLength));
                }
            }

            public void uploadSucceeded(SucceededEvent event) {
                image.setVisible(true);
                image.setCaption("Uploaded Image " + filename +
                        " has length " + os.toByteArray().length);

                // Display the image as a stream resource from
                // the memory buffer
                StreamSource source = new StreamSource() {
                    private static final long serialVersionUID = -4905654404647215809L;

                    public InputStream getStream() {
                        return new ByteArrayInputStream(os.toByteArray());
                    }
                };

                if (image.getSource() == null)
                    // Create a new stream resource
                    image.setSource(new StreamResource(source, filename));
                else { // Reuse the old resource
                    StreamResource resource =
                            (StreamResource) image.getSource();
                    resource.setStreamSource(source);
                    resource.setFilename(filename);
                }

                image.markAsDirty();
            }

            @Override
            public void uploadFailed(FailedEvent event) {
                Notification.show("Upload failed",
                        Notification.Type.ERROR_MESSAGE);
            }
        }

        UploadBox uploadbox = new UploadBox();
        mainLayout.addComponent(uploadbox);
        // END-EXAMPLE: component.upload.advanced
    }

    public void sharingComponent() {
        Panel panel = new Panel("Sharing file");
        panel.setWidth("800");
        Label info = new Label("Select a file");
        HorizontalLayout panelContent = new HorizontalLayout();

        //panelContent.setMargin(false);
        panelContent.setHeight("100%");
        if (treeGrid == null)
            treeGrid = getGrid(null,true,false);
        Panel panelDestination = new Panel("Destination");
        Label infoDestination = new Label("Select a Destination");
        List<String> data = new ArrayList<>();
        EntityManager entityManager = JPAService.getFactory().createEntityManager();
        User user = UserRepository.getByName(this.user, entityManager);
        if(user!=null)
        {
            Team t = user.getTeamid();
            data.add(t.getNomteam());
        }

        List<ShareSpace> shareSpaces = shareRepository.findAll("");
        for(ShareSpace shareSpace : shareSpaces)
        {
            data.add(shareSpace.getShareSpaceName());
        }
        VerticalLayout v1 = new VerticalLayout();
        v1.setMargin(false);
        VerticalLayout v2 = new VerticalLayout();
        v2.setMargin(false);
        RadioButtonGroup<String> targetGroup = new RadioButtonGroup<String>("Select a Target Group ", data);
        targetGroup.setItemCaptionGenerator(item -> "Group " + item);
        targetGroup.setSelectedItem(data.get(0));
        targetGroup.setCaption("Select a Target group");
        VerticalLayout v3 = new VerticalLayout();
        v3.addComponents(targetGroup);
        v3.setSizeUndefined();

        panelDestination.setContent(v3);

        Button copy = new Button("Copy");
        v2.addComponents(infoDestination, panelDestination,copy);
        HorizontalLayout hori = new HorizontalLayout();
        TextField directory = new TextField("Directory :");
        directory.setValue(new File(".").getAbsolutePath());
        directory.setSizeFull();

        Button changeDirectory = new Button("Change Directory");
        changeDirectory.addClickListener(eventChange->
        {
            TreeGrid oldTreegrid = treeGrid;
            treeGrid = new TreeGrid();
            treeGrid = getGrid(directory.getValue());
            treeGrid.setSizeUndefined();
            v1.replaceComponent(oldTreegrid,treeGrid);

        });
        hori.addComponents(directory,changeDirectory);
        hori.setSizeFull();
        hori.setComponentAlignment(changeDirectory,Alignment.BOTTOM_RIGHT);
        v1.setSizeUndefined();
        v1.addComponents(info,hori,treeGrid);
        panelContent.addComponents(v1,v2);
        panel.setContent(panelContent);
        //panel.setSizeFull();
        mainLayout.addComponent(panel);
    }

    public void zipComponent() {
        Panel panel = new Panel("Zip files");
        panel.setWidth("800");
        Label info = new Label("Select files to zip");
        VerticalLayout panelContent = new VerticalLayout();

        //panelContent.setMargin(false);
        panelContent.setHeight("100%");
        if (treeZipGrid == null)
            treeZipGrid = getGrid(null,true,true);

        TextField directory = new TextField("Directory :");
        directory.setValue(new File(".").getAbsolutePath());
        directory.setSizeFull();
        treeZipGrid.setSizeFull();
        Button changeDirectory = new Button("Change Directory");
        HorizontalLayout hori = new HorizontalLayout();
        hori.addComponents(directory,changeDirectory);
        hori.setSizeFull();
        hori.setComponentAlignment(changeDirectory,Alignment.BOTTOM_RIGHT);
        changeDirectory.addClickListener(eventChange->
        {
            TreeGrid oldTreegrid = treeZipGrid;
            treeZipGrid = new TreeGrid();
            treeZipGrid = getGrid(directory.getValue());
            treeZipGrid.setSizeUndefined();
            panelContent.replaceComponent(oldTreegrid,treeZipGrid);

        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        FormLayout horiCeiling = new FormLayout();
        horiCeiling.setMargin(false);
        TextField zipFileName = new TextField("Zip File Name");
        horiCeiling.setSizeFull();
        zipFileName.setSizeFull();
        horiCeiling.addComponent(zipFileName);
        horizontalLayout.setSizeFull();
        Button zip = new Button("Zip");
        zip.addClickListener(eventZip-> {
            Set<File> selectedItems = treeZipGrid.getSelectedItems();
            File[] listFiles= new File[selectedItems.size()];

            int i=0;
            for(File oneFile:selectedItems)
            {
                listFiles[i] = oneFile;
                i++;
            }

            ZipMultipleFiles.zipAction(listFiles,zipFileName.getValue());


        });
        horizontalLayout.addComponents(horiCeiling,zip);
        //horizontalLayout.setComponentAlignment(zip,Alignment.BOTTOM_RIGHT);
        panelContent.addComponents(info,hori,treeZipGrid,horizontalLayout);
        panel.setContent(panelContent);
        //panel.setSizeFull();
        mainLayout.addComponent(panel);
    }

    public void downloadComponent() {

        TextField inputFilepathField = new TextField();
        inputFilepathField.setSizeFull();

        HorizontalLayout h = new HorizontalLayout();
        inputFilepathField.setPlaceholder("Write your file");
        inputFilepathField.setWidth("600");
        Button select = new Button("Select");
        select.addClickListener(eventChoose ->{
           Window newWindow = new Window("Choose a file to download");
           vLayoutDownload =new VerticalLayout();

           treeGrid = getGrid(null);
           Button selectionFichier = new Button("OK");
           selectionFichier.addClickListener(selection-> {
               Set<File> selectionFiles = treeGrid.getSelectedItems();
               for(File file:selectionFiles)
               {
                   inputFilepathField.setValue(file.getAbsolutePath());
               }
               newWindow.close();

           });
           HorizontalLayout hori = new HorizontalLayout();
           TextField directory = new TextField("Directory :");
           directory.setValue(new File(".").getAbsolutePath());
           directory.setSizeFull();

           Button changeDirectory = new Button("Change Directory");
           changeDirectory.addClickListener(eventChange->
           {
               TreeGrid oldTreegrid = treeGrid;
               treeGrid = new TreeGrid();
               treeGrid = getGrid(directory.getValue());
               vLayoutDownload.replaceComponent(oldTreegrid,treeGrid);

           });
           hori.addComponents(directory,changeDirectory);
           hori.setSizeFull();
           hori.setComponentAlignment(changeDirectory,Alignment.BOTTOM_RIGHT);
           vLayoutDownload.addComponents(hori,treeGrid,selectionFichier);
           newWindow.setContent(vLayoutDownload);
           newWindow.setWidth("700");
           newWindow.center();
           newWindow.setDraggable(true);
           newWindow.setResizable(true);
           newWindow.setStyleName("v-window-bluewindow");
           mainLayout.getUI().getUI().addWindow(newWindow);
        });
        h.addComponents(inputFilepathField,select);


        Panel panel = new Panel("Download a file");
        panel.setWidth("720");
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setMargin(true);
        Button downloadButton = new Button("Download Button");

        panelContent.addComponents(h, downloadButton);
        panel.setContent(panelContent);


        final AdvancedFileDownloader downloader = new AdvancedFileDownloader();


        downloader.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {

                String filePath = inputFilepathField.getValue();

                downloader.setFilePath(filePath);

                if (filePath.indexOf("/") > 0)
                    System.out.println("Starting download by button " + filePath.substring(filePath.lastIndexOf("/")));
                else if (filePath.indexOf("\\") > 0)
                    System.out.println("Starting download by button " + filePath.substring(filePath.lastIndexOf("\\")));

            }

        });

        downloader.extend(downloadButton);

        final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
        downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                String filePath = inputFilepathField.getValue();
                downloaderForLink.setFilePath(filePath);
                System.out.println("Starting download by link "
                        + filePath.substring(filePath.lastIndexOf("/")));
            }

        });

        mainLayout.addComponent(panel);
    }

    public void launchComponent()    {
        Panel panelTarget = new Panel("Target");
        VerticalLayout verticalTarget = new VerticalLayout();



        EntityManager em = JPAService.getFactory().createEntityManager();
        Collection<String> hostNames = ServersRepository.listAllServers(em);
        //em.close();

        ComboBox<String> hostname = new ComboBox<>("Select your server", hostNames);

        hostname.setPlaceholder("No server selected");
        // Disallow null selections
        hostname.setEmptySelectionAllowed(false);

        // Set full width
        hostname.setWidth(100.0f, Unit.PERCENTAGE);
        hostname.addValueChangeListener(hostnameEvent->{
           setHostname(hostnameEvent.getValue());
        });

        Panel panelNow = new Panel("Launch Now");
        VerticalLayout verticalNow = new VerticalLayout();
        FormLayout formLayoutNow = new FormLayout();
        TextField directory= new TextField("Directory");
        directory.setWidth("350");
        TextField command= new TextField("Command");
        command.setWidth("350");
        Button launchCommand = new Button("Launch Now");
        formLayoutNow.addComponents(directory,command);
        verticalNow.addComponents(formLayoutNow,launchCommand);
        verticalNow.setHeight("285");

        Button testServer = new Button("Test Server Connection");
        testServer.addClickListener(eventTest->{
            UserAuthKI userAuthKI = new UserAuthKI();
            String[] args = new String[4];
            args[0] = getUser();
            args[1] = hostname.getValue();

            args[1] = getServerIp();
            args[2]= getPassword();
            boolean success = userAuthKI.testConnect(args);
            logger.info("Connection:" + success);
            Window winResultat = new Window("Connection test");
            VerticalLayout vtest = new VerticalLayout();
            Label resultat;
            if (success)
                resultat = new Label("Resultat : SUCCESS" );
            else
                resultat = new Label("Resultat : FAILED" );
            Button close = new Button("Close");
            close.addClickListener(eventClose->{
               winResultat.close();
            });
            vtest.addComponents(resultat,close);
            vtest.setComponentAlignment(close,Alignment.MIDDLE_CENTER);
            winResultat.setContent(vtest);
            winResultat.center();
            winResultat.setWidth("200");
            winResultat.setStyleName("v-window-bluewindow");
            winResultat.addStyleName("v-window");
            this.getParent().getUI().addWindow(winResultat);


        });
        verticalTarget.addComponents(hostname,testServer);
        verticalTarget.setComponentAlignment(testServer,Alignment.BOTTOM_RIGHT);
        verticalTarget.setHeight("285");
        panelTarget.setContent(verticalTarget);
        launchCommand.addClickListener(launchEvent-> {
            String[] args = new String[3];
            args[0] = getUser();
            args[1] = hostname.getValue();
            args[2] = getPassword();
            ProgressWindow win = new ProgressWindow();
            getUI().addWindow(win);
            win.execCommand(args,true,command.getValue(),false);


        });

        verticalNow.setComponentAlignment(launchCommand,Alignment.BOTTOM_RIGHT);
        panelNow.setContent(verticalNow);

        Panel panelLater = new Panel("Launch Later");
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<String> data = Arrays.asList("hours","minutes");
        RadioButtonGroup<String> unit = new RadioButtonGroup<String>("Select your UNIT", data);
        List<String> dataUnit = Arrays.asList("5","10","15","specific");
        RadioButtonGroup<String> factor = new RadioButtonGroup<String>("Select your factor", dataUnit);
        unit.setSelectedItem(data.get(0));
        factor.setSelectedItem(dataUnit.get(0));

        TextField specific = new TextField("");
        horizontalLayout.addComponents(unit,factor,specific);

        specific.setWidth("50");

        horizontalLayout.setMargin(false);
        //verticalLayout.setSpacing(false);
        Button launchLater = new Button("Launch Later");
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.addComponents(launchLater);

        verticalLayout.addComponents(horizontalLayout,horizontalLayout1,launchLater);
        horizontalLayout.setComponentAlignment(specific,Alignment.BOTTOM_RIGHT);
        verticalLayout.setComponentAlignment(launchLater,Alignment.BOTTOM_RIGHT);
        horizontalLayout1.setWidth("300");
        panelLater.setContent(verticalLayout);
        verticalLayout.setHeight("285");
        mainLayout.addComponents(panelTarget,panelNow,panelLater);
    }

    private void setHostname(String value) {
        hostname=value;
    }

    private String getPassword() {
        User userEntity = UserRepository.getByName(getUser(), emTest);
        return userEntity.getPassword();
    }
    private String getServerIp()
    {
        Servers servers = ServersRepository.listAllServers(getHostname(), emTest);
        return servers.getServerIp();
    }

    private String getHostname() {
        return hostname;
    }

    public TreeGrid getGrid(String directory)
    {
        return getGrid(directory,false,false);
    }

    public TreeGrid getGrid(String directory, boolean fullSize,boolean multi)    {
        //-----------------------
        TreeGrid<File> newTreeGrid = new TreeGrid<>();

        if (directory == null) directory = ".";

        newTreeGrid.setDataProvider(new FileSystemDataProvider(new File(directory)));
        if (multi)
        newTreeGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        newTreeGrid.addColumn(file -> {
            String iconHtml;
            if (file.isDirectory()) {
                iconHtml = VaadinIcons.FOLDER_O.getHtml();
            } else {
                iconHtml = VaadinIcons.FILE_O.getHtml();
            }
            return iconHtml + " "
                    + Jsoup.clean(file.getName(), Whitelist.simpleText());
        }, new HtmlRenderer()).setCaption("Name").setId("file-name");

        newTreeGrid.addColumn(file -> file.isDirectory() ? "--" : file.length() + " bytes")
                .setCaption("Size").setId("file-size");

        DateFormat fmt = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        newTreeGrid.addColumn(file -> new Date(file.lastModified()),
                new DateRenderer(fmt)).setCaption("Last Modified")
                .setId("file-last-modified");

        newTreeGrid.setHierarchyColumn("file-name");
        if (fullSize == false)
            newTreeGrid.setSizeFull();
        else
            newTreeGrid.setSizeUndefined();
        return newTreeGrid;
        //-------------------------
    }

    public File getFileDirectoryUpload()    {
        // END-EXAMPLE: component.upload.basic

        // Create uploads directory
        Properties application = new Properties();
        String loadDirectory = "c:\\tmp";
        try {
            application.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
            loadDirectory =application.getProperty("upload.dir");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(loadDirectory );
    }

    private StreamResource createResource() {
        return new StreamResource(new StreamSource() {

            @Override
            public InputStream getStream() {
                String text = "My image";

                BufferedImage bi = new BufferedImage(100, 30, BufferedImage.TYPE_3BYTE_BGR);
                bi.getGraphics().drawChars(text.toCharArray(), 0, text.length(), 10, 20);

                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bi, "png", bos);
                    return new ByteArrayInputStream(bos.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }, "myImage.png");
    }

    @NotNull
    private List<String> getTargetList() {
        List<String> data = new ArrayList<>();
        EntityManager entityManager = JPAService.getFactory().createEntityManager();
        User user = UserRepository.getByName(this.user, entityManager);
        if(user!=null)
        {
            Team t = user.getTeamid();
            data.add(t.getNomteam());
        }

        List<ShareSpace> shareSpaces = shareRepository.findAll("");
        for(ShareSpace shareSpace : shareSpaces)
        {
            data.add(shareSpace.getShareSpaceName());
        }
        return data;
    }


}
