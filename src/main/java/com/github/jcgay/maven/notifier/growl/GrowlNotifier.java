package com.github.jcgay.maven.notifier.growl;

import com.github.jcgay.maven.notifier.*;
import com.google.code.jgntp.*;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

import java.util.concurrent.TimeUnit;

@Component(role = Notifier.class, hint = "growl")
public class GrowlNotifier extends AbstractCustomEventSpy {

    private GntpApplicationInfo application;
    private GntpNotificationInfo notification;
    private GntpClient client;

    @Override
    public void init(EventSpy.Context context) {
        super.init(context);
        initGrowlApplication();
        initBuildStatusGrowlNotification();
        initGrowlClient();
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        if (isClientRegistered()) {
            sendNotificationFor(event);
        }
    }

    @Override
    public void close() {
        super.close();
        if (isClientRegistered()) {
            try {
                TimeUnit.SECONDS.sleep(1); // Seems that the client can be shutdown without having processed all the notifications...
                client.shutdown(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void initGrowlClient() {
        Gntp clientBuilder = Gntp.client(application)
                .listener(new Slf4jGntpListener())
                .onPort(configuration.getGrowlPort());
        if (configuration.getGrowlHost() != null) {
            clientBuilder.forHost(configuration.getGrowlHost());
        }
        if (configuration.getGrowlPassword() != null) {
            clientBuilder.withPassword(configuration.getGrowlPassword());
        }
        client = clientBuilder.build();
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

    private void sendNotificationFor(MavenExecutionResult resultEvent) {
        sendMessageWithIcon(getBuildStatus(resultEvent), resultEvent.getProject().getName(), buildNotificationMessage(resultEvent));
    }

    private void sendMessageWithIcon(Status status, String title, String message) {
        GntpNotification success = Gntp.notification(notification, title).text(message).icon(status.icon()).build();
        try {
            client.notify(success, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isClientRegistered() {
        return client != null && client.isRegistered();
    }
}