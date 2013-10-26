package com.github.jcgay.maven.notifier;

import java.util.List;

import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component(role = EventSpy.class, hint = "notification", description = "Send notification to indicate build status.")
public class NotificationEventSpyChooser extends AbstractEventSpy {

    public static final String SKIP_NOTIFICATION = "skipNotification";
    @Requirement
    private Logger logger;

    @Requirement
    private List<Notifier> availableNotifiers;

    private Notifier activeNotifier;

    @Override
    public void init(Context context) throws Exception {
        chooseNotifier();
        activeNotifier.init(context);
    }

    @Override
    public void onEvent(Object event) throws Exception {
        if (isExecutionResult(event) && shouldSendNotification()) {
            activeNotifier.onEvent((MavenExecutionResult) event);
        }
    }

    private boolean shouldSendNotification() {
        return !"true".equalsIgnoreCase(System.getProperty(SKIP_NOTIFICATION));
    }

    @Override
    public void close() throws Exception {
        activeNotifier.close();
    }

    private boolean isExecutionResult(Object event) {
        return event instanceof MavenExecutionResult;
    }

    private void chooseNotifier() {
        for (Notifier notifier : availableNotifiers) {
            if (notifier.shouldNotify()) {
                activeNotifier = notifier;
                logger.debug("Will notify build success/failure with: " + activeNotifier);
                return;
            }
        }
    }
}
