package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import com.github.jcgay.maven.notifier.notificationcenter.NotificationCenterEventSpy;
import com.github.jcgay.maven.notifier.notifysend.NotifySendEventSpy;
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

    private void chooseSpy() {
        Configuration configuration = parser.get();
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
}
