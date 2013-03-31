package com.github.jcgay.maven.notifier.notifysend;

import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.executor.ExecutorHolder;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static junit.framework.Assert.*;

public class NotifySendEventSpyTest {

    private NotifySendEventSpy spy;
    private ExecutorHolder result;

    @BeforeMethod
    private void init() {
        result = new ExecutorHolder();
        Configuration configuration = new Configuration();
        configuration.setNotifySendPath("path");
        configuration.setNotifySendTimeout("1");
        spy = new NotifySendEventSpy(result, configuration);
    }

    @Test
    public void should_notify_a_build_success() throws Exception {

        DefaultMavenExecutionResult event = new DefaultMavenExecutionResult();
        MavenProject project = new MavenProject();
        project.setName("project");
        event.setProject(project);

        spy.onEvent(event);

        assertEquals("path", result.getCommand()[0]);
        assertEquals(project.getName(), result.getCommand()[1]);
        assertTrue(result.getCommand()[2].isEmpty());
        assertEquals("-t", result.getCommand()[3]);
        assertEquals("1", result.getCommand()[4]);
        assertEquals("-i", result.getCommand()[5]);
        assertTrue(result.getCommand()[6].endsWith("SUCCESS.png"));
    }

    @Test
    public void should_not_notify_if_event_is_not_a_build_result() throws Exception {

        spy.onEvent("this is not a build result");

        assertNull(result.getCommand());
    }

}
