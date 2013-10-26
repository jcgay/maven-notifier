package com.github.jcgay.maven.notifier.notificationcenter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.Status;
import com.github.jcgay.maven.notifier.executor.ExecutorHolder;
import com.github.jcgay.maven.notifier.growl.GrowlNotifier;

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

        DefaultMavenExecutionResult event = new DefaultMavenExecutionResult();
        MavenProject project = new MavenProject();
        project.setName("project");
        event.setProject(project);
        event.addBuildSummary(new BuildSuccess(project, 1000));

        notifier.onEvent(event);

        assertEquals("path", result.getCommand()[0]);
        assertEquals("-title", result.getCommand()[1]);
        assertEquals(project.getName(), result.getCommand()[2]);
        assertEquals("-subtitle", result.getCommand()[3]);
        assertEquals(Status.SUCCESS.message(), result.getCommand()[4]);
        assertEquals("-message", result.getCommand()[5]);
        assertEquals("Built in: 1 second(s).", result.getCommand()[6]);
        assertEquals("-group", result.getCommand()[7]);
        assertEquals("maven", result.getCommand()[8]);
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
