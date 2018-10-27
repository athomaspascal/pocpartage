package com.start;


import com.BookExample.TheServices;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadinupload.AdvancedFileDownloader;
import com.vaadinupload.FileSystemDataProvider;
import com.vaadinupload.ImageReceiver;
import dap.entities.JPAService;
import dap.entities.share.ShareSpace;
import dap.entities.share.shareRepository;
import dap.entities.team.Team;
import dap.entities.user.User;
import dap.entities.user.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListServicesComponent extends CustomComponent implements TheServices {
    private static final long serialVersionUID = -4292553844521293140L;
    private TreeGrid treeGrid;
    String user;
    VerticalLayout vLayoutDownload = new VerticalLayout();
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public void init(String context,String choixServices) {
        CssLayout layout = new CssLayout();
        VerticalLayout vLayout = new VerticalLayout();

        if ("basic".equals(context))
            basic(vLayout);
        else if ("advanced".equals(context))
            advanced(vLayout);
        else
            layout.addComponent(new Label("Invalid context: " + context));
        VerticalLayout vLayout2 = new VerticalLayout();

        downloadComponent(vLayout2);
        partageComponent(vLayout2);
        layout.addComponents(vLayout,vLayout2);
        setCompositionRoot(layout);
    }


    public void basic(VerticalLayout layout) {
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

        //layout.addComponent(targetGroup);
        panelContent.addComponents(upload, image, targetGroup);
        panel.setContent(panelContent);

        File uploads = getFileDirectoryUpload();
        if (!uploads.exists() && !uploads.mkdir())
            layout.addComponent(new Label("ERROR: Could not create upload dir"));

        ((VerticalLayout) panel.getContent()).setSpacing(true);
        panel.setWidth("-1");
        layout.addComponent(panel);
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

    public void advanced(VerticalLayout layout) {
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
        layout.addComponent(uploadbox);
        // END-EXAMPLE: component.upload.advanced
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

    public void partageComponent(VerticalLayout mainLayout) {
        Panel panel = new Panel("Sharing file");
        panel.setWidth("800");
        Label info = new Label("Select a file");
        HorizontalLayout panelContent = new HorizontalLayout();

        //panelContent.setMargin(false);
        panelContent.setHeight("100%");
        if (treeGrid == null)
            treeGrid = displayGrid(null,true);
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
            treeGrid = displayGrid(directory.getValue());
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

    public void downloadComponent(VerticalLayout mainLayout) {

        TextField inputFilepathField = new TextField();
        inputFilepathField.setSizeFull();

        HorizontalLayout h = new HorizontalLayout();
        inputFilepathField.setPlaceholder("Write your file");
        inputFilepathField.setWidth("600");
        Button select = new Button("Select");
        select.addClickListener(eventChoose ->{
           Window newWindow = new Window("Choose a file to download");
           vLayoutDownload =new VerticalLayout();

           treeGrid = displayGrid(null);
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
               treeGrid = displayGrid(directory.getValue());
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

    public TreeGrid  displayGrid(String directory)
    {
        return displayGrid(directory,false);
    }

    public TreeGrid  displayGrid(String directory,boolean fullSize)    {
        //-----------------------
        TreeGrid<File> newTreeGrid = new TreeGrid<>();

        if (directory == null) directory = ".";

        newTreeGrid.setDataProvider(new FileSystemDataProvider(new File(directory)));

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

    public File getFileDirectoryUpload()
    {
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
}
