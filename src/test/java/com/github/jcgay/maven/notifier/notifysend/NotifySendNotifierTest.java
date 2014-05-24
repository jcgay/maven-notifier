package com.github.jcgay.maven.notifier.notifysend;

import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.KnownElapsedTimeTicker;
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

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class NotifySendNotifierTest {

    private NotifySendNotifier notifier;
    private ExecutorHolder result;
    private Configuration configuration;

    @BeforeMethod
    private void init() {
        result = new ExecutorHolder();
        configuration = new Configuration();
        configuration.setNotifySendPath("path");
        configuration.setNotifySendTimeout("1");
        notifier = new NotifySendNotifier(result, configuration);
        notifier.setLogger(mock(Logger.class));
    }

    @Test
    public void should_notify_a_build_success() throws Exception {

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
        assertEquals(project.getName() + " [3s]", result.getCommand()[1]);
        assertTrue(result.getCommand()[2].isEmpty());
        assertEquals("-t", result.getCommand()[3]);
        assertEquals("1", result.getCommand()[4]);
        assertEquals("-i", result.getCommand()[5]);
        assertTrue(result.getCommand()[6].endsWith("SUCCESS.png"));
    }

    @Test
    public void should_match_configuration() throws Exception {

        configuration.setImplementation(NotifySendNotifier.class.getName());
        assertTrue(notifier.shouldNotify());

        configuration.setImplementation("notifysend");
        assertTrue(notifier.shouldNotify());
    }

    @Test
    public void should_not_match_configuration() throws Exception {

        configuration.setImplementation(GrowlNotifier.class.getName());
        assertFalse(notifier.shouldNotify());

        configuration.setImplementation("notificationcenter");
        assertFalse(notifier.shouldNotify());
    }
}
