package com.github.jcgay.maven.notifier;

import com.google.code.jgntp.*;
import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class GrowlEventSpy extends AbstractEventSpy {

    private GntpApplicationInfo application;
    private GntpNotificationInfo notification;
    private GntpClient client;

    @Override
    public void init(Context context) throws Exception {
        application = Gntp.appInfo("Maven").build();
        notification = Gntp.notificationInfo(application, "build-status-notification")
                           .displayName("Build result status")
                           .build();
        application.addNotificationInfo(notification);
        client = Gntp.client(application)
                     .listener(new Slf4jGntpListener())
                     .onPort(23053)
                     .build();
        client.register();
        super.init(context);
    }

    @Override
    public void onEvent(Object event) throws Exception {

        if (isEventExecutionResult(event) && isClientRegistered()) {

            BuildSummary summary = ((MavenExecutionResult) event).getBuildSummary(((MavenExecutionResult) event).getProject());

            if (isSuccess(summary)) {
                sendMessage(summary, String.format("Success in %d seconds.", TimeUnit.MILLISECONDS.toSeconds(summary.getTime())));
            } else if (isFailure(summary)) {
                sendMessage(summary, String.format("Failed: %s", StringUtils.abbreviate(((BuildFailure) summary).getCause().getMessage(), 75)));
            }
        }

        super.onEvent(event);
    }

    private void sendMessage(BuildSummary summary, String message) throws InterruptedException {
        GntpNotification success = Gntp.notification(notification, summary.getProject().getName()).text(message).build();
        client.notify(success, 5, TimeUnit.SECONDS);
    }

    private boolean isFailure(BuildSummary summary) {
        return summary instanceof BuildFailure;
    }

    private boolean isSuccess(BuildSummary summary) {
        return summary instanceof BuildSuccess;
    }

    private boolean isEventExecutionResult(Object event) {
        return event instanceof MavenExecutionResult;
    }

    private boolean isClientRegistered() {
        return client != null && client.isRegistered();
    }

    @Override
    public void close() throws Exception {
        if (isClientRegistered()) {
            TimeUnit.SECONDS.sleep(1);
            client.shutdown(5, TimeUnit.SECONDS);
        }
        super.close();
    }
}
