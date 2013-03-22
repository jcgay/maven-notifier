package com.github.jcgay.maven.notifier.notificationcenter;

import com.github.jcgay.maven.notifier.executor.ExecutorHolder;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertEquals;

public class NotificationCenterEventSpyTest {

    private NotificationCenterEventSpy spy;
    private ExecutorHolder result;

    @BeforeMethod
    private void init() {
        result = new ExecutorHolder();
        spy = new NotificationCenterEventSpy(result);
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

        spy.onEvent(event);

        assertEquals("/Applications/terminal-notifier.app/Contents/MacOS/terminal-notifier", result.getCommand()[0]);
        assertEquals("-title", result.getCommand()[1]);
        assertEquals(project.getName(), result.getCommand()[2]);
        assertEquals("-message", result.getCommand()[3]);
        assertEquals("project: Success", result.getCommand()[4]);
        assertEquals("-group", result.getCommand()[5]);
        assertEquals("maven", result.getCommand()[6]);
        assertEquals("-open", result.getCommand()[7]);
        assertEquals("/Applications", result.getCommand()[8]);
    }
}
