package com.example.start;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.OutputStream;

@SpringUI
@Push
@Theme("mytheme")
public class VaadinUI extends UI implements Upload.Receiver, Upload.SucceededListener {

    private Upload upload;
    private ProgressBar progressBar;

    private final ProcessingService processingService;
    private FastByteArrayOutputStream outputStream;

    public VaadinUI(ProcessingService processingService) { // processingService is injected by Spring
        this.processingService = processingService;
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout mainLayout = new VerticalLayout();
        UploadTestComponent u = new UploadTestComponent();
        u.init("basic");
        mainLayout.addComponent(u);

        setContent(mainLayout);
    }

    @Override
    public OutputStream receiveUpload(String s, String s1) {
        progressBar.setVisible(true);
        return outputStream = new FastByteArrayOutputStream();
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        upload.setVisible(false);

        progressBar.setCaption("Processing...");
        progressBar.setIndeterminate(false);

        // the actual job is started inside the service class in a new thread
        processingService.processData(outputStream.toString(),
                this::processingUpdated, this::processingSucceeded);
    }

    private void processingUpdated(Float percentage) {
        // use access when modifying the UI from a background thread
        access(() -> progressBar.setValue(percentage));
    }

    private void processingSucceeded() {
        access(() -> {
            progressBar.setVisible(false);
            Notification.show("Done!");
        });
    }
}
