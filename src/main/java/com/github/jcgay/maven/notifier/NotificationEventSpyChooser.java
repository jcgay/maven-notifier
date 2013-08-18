package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import com.github.jcgay.maven.notifier.notificationcenter.NotificationCenterEventSpy;
import com.github.jcgay.maven.notifier.notifysend.NotifySendEventSpy;
import com.google.common.annotations.VisibleForTesting;
import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.eventspy.EventSpy;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component(role = EventSpy.class, hint = "notification", description = "Send notification to indicate build status.")
public class NotificationEventSpyChooser extends AbstractEventSpy {

    @Requirement
    private ConfigurationParser parser;

    private EventSpy spy;

    @Override
    public void init(Context context) throws Exception {
        chooseSpy();
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

    @VisibleForTesting EventSpy getSpy() {
        return spy;
    }

    private void chooseSpy() {
        Configuration configuration = parser.get();
        String spy = configuration.getImplementation();
        if (GrowlEventSpy.class.getName().contains(spy)) {
            this.spy = new GrowlEventSpy(configuration);
        } else if (NotifySendEventSpy.class.getName().contains(spy)) {
            this.spy = new NotifySendEventSpy(configuration);
        } else if (NotificationCenterEventSpy.class.getName().contains(spy)) {
            this.spy = new NotificationCenterEventSpy(configuration);
        } else {
            throw new IllegalStateException(String.format("Implementation [%s] is not valid.", spy));
        }
    }
}
