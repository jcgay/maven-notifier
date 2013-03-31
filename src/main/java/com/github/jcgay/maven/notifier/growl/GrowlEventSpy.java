package com.github.jcgay.maven.notifier.growl;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.Status;
import com.google.code.jgntp.*;
import org.apache.maven.execution.MavenExecutionResult;

import java.util.concurrent.TimeUnit;

public class GrowlEventSpy extends AbstractCustomEventSpy {

    private GntpApplicationInfo application;
    private GntpNotificationInfo notification;
    private GntpClient client;
    private Configuration configuration;

    public GrowlEventSpy(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void init(Context context) throws Exception {
        super.init(context);
        initGrowlApplication();
        initBuildStatusGrowlNotification();
        initGrowlClient();
    }

    @Override
    public void onEvent(Object event) throws Exception {
        super.onEvent(event);
        if (isExecutionResult(event) && isClientRegistered()) {
            sendNotificationFor((MavenExecutionResult) event);
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (isClientRegistered()) {
            TimeUnit.SECONDS.sleep(1); // Seems that the client can be shutdown without having processed all the notifications...
            client.shutdown(5, TimeUnit.SECONDS);
        }
    }

    private void initGrowlClient() {
        client = Gntp.client(application)
                     .listener(new Slf4jGntpListener())
                     .onPort(configuration.getGrowlPort())
                     .build();
        client.register();
    }

    private void initBuildStatusGrowlNotification() {
        notification = Gntp.notificationInfo(application, "build-status-notification")
                           .displayName("Build result status")
                           .build();
    }

    private void initGrowlApplication() {
        application = Gntp.appInfo("Maven").build();
    }

    private void sendNotificationFor(MavenExecutionResult resultEvent) throws InterruptedException {
        sendMessageWithIcon(getBuildStatus(resultEvent), resultEvent.getProject().getName(), buildNotificationMessage(resultEvent));
    }

    private void sendMessageWithIcon(Status status, String title, String message) throws InterruptedException {
        GntpNotification success = Gntp.notification(notification, title).text(message).icon(status.icon()).build();
        client.notify(success, 5, TimeUnit.SECONDS);
    }

    private boolean isClientRegistered() {
        return client != null && client.isRegistered();
    }
}