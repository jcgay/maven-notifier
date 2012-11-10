package com.github.jcgay.maven.notifier;

import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCustomEventSpy extends AbstractEventSpy {

    protected Status getBuildStatus(MavenExecutionResult result) {
        return result.hasExceptions() ? Status.FAILURE : Status.SUCCESS;
    }

    protected String buildNotificationMessage(MavenExecutionResult result) {
        StringBuilder builder = new StringBuilder();
        for (MavenProject project : result.getTopologicallySortedProjects()) {
            BuildSummary buildSummary = result.getBuildSummary(project);
            Status status = Status.of(buildSummary);
            builder.append(project.getName());
            builder.append(": ");
            builder.append(status.message());
            if (status != Status.SKIPPED) {
                builder.append(" [");
                builder.append(TimeUnit.MILLISECONDS.toSeconds(buildSummary.getTime()));
                builder.append("s] ");
            }
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    protected boolean isExecutionResult(Object event) {
        return event instanceof MavenExecutionResult;
    }
}
