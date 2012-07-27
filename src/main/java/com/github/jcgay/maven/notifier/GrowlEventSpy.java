package com.github.jcgay.maven.notifier;

import com.google.code.jgntp.*;
import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;

import java.util.concurrent.TimeUnit;

public class GrowlEventSpy extends AbstractEventSpy {

    private GntpApplicationInfo application;
    private GntpNotificationInfo notification;
    private GntpClient client;

    @Override
    public void init(Context context) throws Exception {
        initGrowlApplication();
        initBuildStatusGrowlNotification();
        initGrowlClient();
        super.init(context);
    }

    @Override
    public void onEvent(Object event) throws Exception {
        if (isExecutionResult(event) && isClientRegistered()) {
            sendNotificationFor((MavenExecutionResult) event);
        }
        super.onEvent(event);
    }

    @Override
    public void close() throws Exception {
        if (isClientRegistered()) {
            TimeUnit.SECONDS.sleep(1); // Seems that the client can be shutdown without having processed all the notifications...
            client.shutdown(5, TimeUnit.SECONDS);
        }
        super.close();
    }

    private Status determineBuildStatus(MavenExecutionResult result) {
        for (MavenProject project : result.getTopologicallySortedProjects()) {
            BuildSummary summary = result.getBuildSummary(project);
            if (Status.of(summary) == Status.FAILURE) {
                return Status.FAILURE;
            }
        }
        return Status.SUCCESS;
    }

    private void initGrowlClient() {
        client = Gntp.client(application)
                     .listener(new Slf4jGntpListener())
                     .onPort(23053)
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
        sendMessageWithIcon(determineBuildStatus(resultEvent), resultEvent.getProject().getName(), buildNotificationMessage(resultEvent));
    }

    private String buildNotificationMessage(MavenExecutionResult result) {
        StringBuilder builder = new StringBuilder();
        for (MavenProject project : result.getTopologicallySortedProjects()) {
            BuildSummary buildSummary = result.getBuildSummary(project);
            Status status = Status.of(buildSummary);
            builder.append(project.getName());
            builder.append(": ");
            builder.append(status.message());
            if (status != Status.SKIPPED) {
                builder.append(" [");
                builder.append(TimeUnit.MILLISECONDS.toSeconds(buildSummary.getTime()));
                builder.append("s] ");
            }
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    private void sendMessageWithIcon(Status status, String title, String message) throws InterruptedException {
        GntpNotification success = Gntp.notification(notification, title).text(message).icon(status.icon()).build();
        client.notify(success, 5, TimeUnit.SECONDS);
    }

    private boolean isExecutionResult(Object event) {
        return event instanceof MavenExecutionResult;
    }

    private boolean isClientRegistered() {
        return client != null && client.isRegistered();
    }
}