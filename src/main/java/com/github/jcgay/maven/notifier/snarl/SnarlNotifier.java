package com.github.jcgay.maven.notifier.snarl;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.Icon;
import com.github.jcgay.snp4j.Server;
import com.github.jcgay.snp4j.impl.SnpNotifier;
import com.github.jcgay.snp4j.request.Notification;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

import static com.google.common.io.Closeables.closeQuietly;

@Component(role = com.github.jcgay.maven.notifier.Notifier.class, hint = "snarl")
public class SnarlNotifier extends AbstractCustomEventSpy {

    private com.github.jcgay.snp4j.Notifier snarl;
    private Application application = Application.of("application/x-vnd-apache.maven", "Maven");

    @Override
    public void init(EventSpy.Context context) {
        super.init(context);
        snarl = SnpNotifier.of(application, new Server(configuration.getSnarlHost(), Integer.valueOf(configuration.getSnarlPort())));
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);

        Notification notification = new Notification();
        notification.setIcon(Icon.base64(getBuildStatus(event).toByteArray()));
        notification.setText(buildNotificationMessage(event));
        notification.setTitle(buildTitle(event));

        try {
            snarl.send(notification);
        } finally {
            closeQuietly(snarl);
        }
    }
}
