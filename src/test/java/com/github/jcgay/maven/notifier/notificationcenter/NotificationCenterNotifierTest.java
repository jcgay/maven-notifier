package com.github.jcgay.maven.notifier.notificationcenter;

import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.executor.ExecutorHolder;
import com.github.jcgay.maven.notifier.growl.GrowlNotifier;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertEquals;
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
    }

    @Test
    public void should_notify_a_build_success() throws Exception {

        DefaultMavenExecutionResult event = new DefaultMavenExecutionResult();
        MavenProject project = new MavenProject();
        project.setName("project");
        Build build = new Build();
        build.setDirectory("/Applications");
        project.setBuild(build);
        event.setProject(project);

        notifier.onEvent(event);

        assertEquals("path", result.getCommand()[0]);
        assertEquals("-title", result.getCommand()[1]);
        assertEquals(project.getName(), result.getCommand()[2]);
        assertEquals("-message", result.getCommand()[3]);
        assertEquals("project: Success", result.getCommand()[4]);
        assertEquals("-group", result.getCommand()[5]);
        assertEquals("maven", result.getCommand()[6]);
        assertEquals("-open", result.getCommand()[7]);
        assertEquals("/Applications", result.getCommand()[8]);
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
