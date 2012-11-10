package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.mockito.Mock;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class GrowlEventSpyExample {

    private GrowlEventSpy spy;
    @Mock private MavenProject project;

    @BeforeTest
    private void initializeClient() throws Exception {
        spy = new GrowlEventSpy();
        spy.init(mock(EventSpy.Context.class));
    }

    @BeforeMethod
    private void initializeTest() {
        initMocks(this);
    }

    @AfterTest
    private void closeClient() throws Exception {
        spy.close();
    }

    @Test
    public void should_send_a_notification_when_build_succeeded() throws Exception {

        MavenExecutionResult result = givenSuccessfulEventForProjectNamed("Successful project");

        spy.onEvent(result);
    }

    @Test
    public void should_send_a_notification_when_build_failed() throws Exception {

        MavenExecutionResult result = givenFailureEventForProjectNamed("Failing project");

        spy.onEvent(result);
    }

    private MavenExecutionResult givenSuccessfulEventForProjectNamed(String projectName) {
        MavenExecutionResult result = givenEventForProjectNamed(projectName);
        given(result.getBuildSummary(any(MavenProject.class))).willReturn(new BuildSuccess(project, 1));
        return result;
    }

    private MavenExecutionResult givenEventForProjectNamed(String projectName) {
        MavenExecutionResult result = mock(MavenExecutionResult.class);
        given(result.getTopologicallySortedProjects()).willReturn(Arrays.asList(project));
        given(project.getName()).willReturn(projectName);
        given(result.getProject()).willReturn(project);
        return result;
    }

    private MavenExecutionResult givenFailureEventForProjectNamed(String projectName) {
        MavenExecutionResult result = givenEventForProjectNamed(projectName);
        given(result.getBuildSummary(any(MavenProject.class))).willReturn(new BuildFailure(project, 1, new Exception("Error.")));
        return result;
    }
}
