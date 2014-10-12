package fr.jcgay.maven.notifier.sendnotification;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Notifier;
import com.github.jcgay.maven.notifier.Status;
import com.google.common.annotations.VisibleForTesting;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.SendNotification;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

import java.net.URL;

@Component(role = Notifier.class, hint = "send-notification")
public class SendNotificationNotifier extends AbstractCustomEventSpy {

    private static final Icon ICON = Icon.create(resource("maven.png"), "maven");
    private static final Application MAVEN = Application.builder("application/x-vnd-apache.maven", "Maven", ICON).build();

    private final fr.jcgay.notification.Notifier notifier;

    public SendNotificationNotifier() {
        notifier = new SendNotification()
                .setApplication(MAVEN)
                .chooseNotifier();
    }

    @VisibleForTesting
    SendNotificationNotifier(fr.jcgay.notification.Notifier notifier) {
        this.notifier = notifier;
    }

    @VisibleForTesting
    fr.jcgay.notification.Notifier getNotifier() {
        return notifier;
    }

    @Override
    public void init(EventSpy.Context context) {
        super.init(context);
        notifier.init();
    }

    @Override
    public void close() {
        super.close();
        notifier.close();
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        Status status = getBuildStatus(event);
        notifier.send(
                Notification.builder(buildTitle(event), buildNotificationMessage(event), Icon.create(status.url(), status.name()))
                        .withSubtitle(status.message())
                        .build()
        );
    }

    private static URL resource(String resource) {
        return Thread.currentThread().getContextClassLoader().getResource(resource);
    }
}
