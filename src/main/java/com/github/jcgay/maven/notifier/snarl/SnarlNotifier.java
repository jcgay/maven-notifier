package com.github.jcgay.maven.notifier.snarl;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Status;
import fr.jcgay.snp4j.Application;
import fr.jcgay.snp4j.Icon;
import fr.jcgay.snp4j.Notifier;
import fr.jcgay.snp4j.Server;
import fr.jcgay.snp4j.SnpException;
import fr.jcgay.snp4j.impl.SnpNotifier;
import fr.jcgay.snp4j.request.Notification;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

import java.util.List;

import static com.google.common.io.Closeables.closeQuietly;

@Component(role = com.github.jcgay.maven.notifier.Notifier.class, hint = "snarl")
public class SnarlNotifier extends AbstractCustomEventSpy {

    private Notifier snarl;
    private Application application = Application.of("application/x-vnd-apache.maven", "Maven");

    @Override
    public void init(EventSpy.Context context) {
        super.init(context);
        Server server = Server.builder()
                .withHost(configuration.getSnarlHost())
                .withPort(Integer.valueOf(configuration.getSnarlPort()))
                .withPassword(configuration.getSnarlPassword())
                .build();
        try {
            snarl = SnpNotifier.of(application, server);
        } catch (SnpException e) {
            logger.error(message(), e);
        }
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        if (snarl == null) {
            logger.error("Will not try to notify Snarl, initialization has failed.");
            return;
        }
        sendNotificationFor(getBuildStatus(event), buildNotificationMessage(event), buildTitle(event));
    }

    private void sendNotificationFor(Status status, String message, String title) {
        Notification notification = new Notification();
        notification.setIcon(Icon.base64(status.toByteArray()));
        notification.setText(message);
        notification.setTitle(title);

        try {
            snarl.send(notification);
        } catch (SnpException e) {
            logger.error(message(), e);
        } finally {
            closeQuietly(snarl);
        }
    }

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        sendNotificationFor(Status.FAILURE, buildErrorDescription(exceptions), "Build Error");
    }

    private static String message() {
        return String.format("Error while trying to talk with Snarl.%n%n" +
                "For more information about the errors and possible solutions, please read the following article:%n" +
                "https://github.com/jcgay/maven-notifier/wiki/Snarl");
    }
}
