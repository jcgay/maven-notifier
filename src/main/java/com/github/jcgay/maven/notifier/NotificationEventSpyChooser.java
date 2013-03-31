package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import com.github.jcgay.maven.notifier.notificationcenter.NotificationCenterEventSpy;
import com.github.jcgay.maven.notifier.notifysend.NotifySendEventSpy;
import com.google.common.annotations.VisibleForTesting;
import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.eventspy.EventSpy;
import org.codehaus.plexus.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(role = EventSpy.class, hint = "notification", description = "Send notification to indicate build status.")
public class NotificationEventSpyChooser extends AbstractEventSpy {

    private EventSpy spy;

    private NotificationEventSpyChooser(Configuration configuration) {
        if (GrowlEventSpy.class.getName().equals(configuration.getImplementation())) {
            spy = new GrowlEventSpy(configuration);
        } else if (NotifySendEventSpy.class.getName().equals(configuration.getImplementation())) {
            spy = new NotifySendEventSpy(configuration);
        } else if (NotificationCenterEventSpy.class.getName().equals(configuration.getImplementation())) {
            spy = new NotificationCenterEventSpy(configuration);
        } else {
            throw new IllegalStateException(String.format("Implementation [%s] is not valid.", configuration.getImplementation()));
        }
    }

    public NotificationEventSpyChooser() {
        this(new ConfigurationParser().get());
    }

    @Override
    public void init(Context context) throws Exception {
        spy.init(context);
    }

    @Override
    public void onEvent(Object event) throws Exception {
        spy.onEvent(event);
    }

    @Override
    public void close() throws Exception {
        spy.close();
    }
}
