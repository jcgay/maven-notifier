package com.github.jcgay.maven.notifier.notificationcenter;

import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.KnownElapsedTimeTicker;
import com.github.jcgay.maven.notifier.Status;
import com.github.jcgay.maven.notifier.executor.ExecutorHolder;
import com.github.jcgay.maven.notifier.growl.GrowlNotifier;
import com.google.common.base.Stopwatch;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class NotificationCenterNotifierTest {

    private NotificationCenterNotifier notifier;
    private ExecutorHolder result;
    private Configuration configuration;

    @BeforeMethod
    private void init() {
        result = new ExecutorHolder();
        configuration = new Configuration();
        configuration.setNotificationCenterPath("path");
        notifier = new NotificationCenterNotifier(result, configuration);
        notifier.setLogger(mock(Logger.class));
    }

    @Test
    public void should_notify_a_build_success() throws Exception {

        configuration.setNotificationCenterActivate("com.apple.Terminal");
        configuration.setNotificationCenterSound("default");
        DefaultMavenExecutionResult event = new DefaultMavenExecutionResult();
        MavenProject project = new MavenProject();
        project.setName("project");
        event.setProject(project);
        notifier.setStopwatch(new Stopwatch(new KnownElapsedTimeTicker(TimeUnit.SECONDS.toNanos(3L))));

        notifier.init(new EventSpy.Context() {
            @Override
            public Map<String, Object> getData() {
                return Collections.emptyMap();
            }
        });
        notifier.onEvent(event);

        assertEquals("path", result.getCommand()[0]);
        assertEquals("-title", result.getCommand()[1]);
        assertEquals(project.getName(), result.getCommand()[2]);
        assertEquals("-subtitle", result.getCommand()[3]);
        assertEquals(Status.SUCCESS.message(), result.getCommand()[4]);
        assertEquals("-message", result.getCommand()[5]);
        assertEquals("Built in: 3 second(s).", result.getCommand()[6]);
        assertEquals("-group", result.getCommand()[7]);
        assertEquals("maven", result.getCommand()[8]);
        assertEquals("-activate", result.getCommand()[9]);
        assertEquals("com.apple.Terminal", result.getCommand()[10]);
        assertEquals("-contentImage", result.getCommand()[11]);
        assertEquals(Status.SUCCESS.asPath(), result.getCommand()[12]);
        assertEquals("-sound", result.getCommand()[13]);
        assertEquals("default", result.getCommand()[14]);
    }

    @Test
    public void should_match_configuration() throws Exception {

        configuration.setImplementation(NotificationCenterNotifier.class.getName());
        assertTrue(notifier.shouldNotify());

        configuration.setImplementation("notificationcenter");
        assertTrue(notifier.shouldNotify());
    }

    @Test
    public void should_not_match_configuration() throws Exception {

        configuration.setImplementation(GrowlNotifier.class.getName());
        assertFalse(notifier.shouldNotify());

        configuration.setImplementation("notifysend");
        assertFalse(notifier.shouldNotify());
    }
}
