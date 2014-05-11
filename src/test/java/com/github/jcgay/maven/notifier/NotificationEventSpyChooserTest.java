package com.github.jcgay.maven.notifier;

import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenExecutionResult;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.jcgay.maven.notifier.NotificationEventSpyChooser.SKIP_NOTIFICATION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NotificationEventSpyChooserTest {

    @InjectMocks
    private NotificationEventSpyChooser chooser = new NotificationEventSpyChooser();

    @Mock
    private Notifier notifier;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_not_notify_if_event_is_not_a_build_result() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false));

        chooser.onEvent("this is not a build result");

        verifyZeroInteractions(notifier);
    }

    @Test
    public void should_not_notify_when_property_skipNotification_is_true() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(true));

        chooser.onEvent(mock(MavenExecutionResult.class));

        verifyZeroInteractions(notifier);
    }

    @Test
    public void should_notify_when_property_skipNotification_is_false() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false));
        MavenExecutionResult event = mock(MavenExecutionResult.class);

        chooser.onEvent(event);

        verify(notifier).onEvent(event);
    }

    @Test
    public void should_notify_failure_when_build_fails_without_project() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false));
        DefaultMavenExecutionResult event = new DefaultMavenExecutionResult();
        event.setProject(null);
        event.addException(new NullPointerException());

        chooser.onEvent(event);

        verify(notifier).onFailWithoutProject(event.getExceptions());
        verify(notifier, never()).onEvent(event);
    }
}
