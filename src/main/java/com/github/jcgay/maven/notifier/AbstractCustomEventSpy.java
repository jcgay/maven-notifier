package com.github.jcgay.maven.notifier;

import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCustomEventSpy implements Notifier {

    protected Logger logger;
    protected Configuration configuration;

    @Override
    public void init(EventSpy.Context context) {
        // do nothing
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public boolean shouldNotify() {
        if (getClass().getName().contains(configuration.getImplementation())) {
            return true;
        }
        return false;
    }

    @Requirement
    public void setConfiguration(ConfigurationParser configuration) {
        this.configuration = configuration.get();
    }

    @Requirement
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

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
}
