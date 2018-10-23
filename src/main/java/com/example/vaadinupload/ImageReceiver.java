package com.example.vaadinupload;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class ImageReceiver implements Upload.Receiver, Upload.SucceededListener {
    private static final long serialVersionUID = -1276759102490466761L;

    public File file;

    Image image;

    public ImageReceiver(Image im)
    {
        image=im;
    }

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        // Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
            file = new File("/tmp/uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Show the uploaded file in the image viewer
        image.setVisible(true);
        image.setSource(new ThemeResource("images/ok.jpg"));
    }
};