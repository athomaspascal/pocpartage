package com.start;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@SpringUI
@Push
@Theme("mytheme")
public class VaadinUI extends UI implements Upload.Receiver, Upload.SucceededListener {

    private Upload upload;
    private ProgressBar progressBar;
    Navigator navigator;

    private final ProcessingService processingService;
    private FastByteArrayOutputStream outputStream;
    public Map<String, String[]> parameterMap = new HashMap<>();
    public VaadinUI(ProcessingService processingService) { // processingService is injected by Spring
        this.processingService = processingService;
    }

    @Override
    protected void init(VaadinRequest request) {
        parameterMap = request.getParameterMap();
        VerticalLayout v = new VerticalLayout();
        navigator = new Navigator(this, v);
        navigator.addView("Default", ViewDefault.class);
        navigator.addView("Choose", ViewChooseServices.class);
        //navigator.addView("view1", View1.class);
        //navigator.addView("view2", View2.class);

        if (navigator.getCurrentView() == null)
           navigator.navigateTo("Choose");

        setContent(v);
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
