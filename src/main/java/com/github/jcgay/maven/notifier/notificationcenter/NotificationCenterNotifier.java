package com.github.jcgay.maven.notifier.notificationcenter;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.Notifier;
import com.github.jcgay.maven.notifier.Status;
import com.github.jcgay.maven.notifier.executor.Executor;
import com.github.jcgay.maven.notifier.executor.RuntimeExecutor;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(role = Notifier.class, hint = "notification-center")
public class NotificationCenterNotifier extends AbstractCustomEventSpy {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationCenterNotifier.class);

    private static final String CMD_MESSAGE = "-message";
    private static final String CMD_TITLE = "-title";
    private static final String CMD_GROUP = "-group";
    private static final String CMD_OPEN = "-open";
    private static final String GROUP = "maven";

    private final Executor executor;

    public NotificationCenterNotifier() {
        this.executor = new RuntimeExecutor();
    }

    @VisibleForTesting
    NotificationCenterNotifier(Executor executor, Configuration configuration) {
        this.executor = executor;
        this.configuration = configuration;
    }

    @Override
    public boolean shouldNotify() {
        if (NotificationCenterNotifier.class.getName().contains(configuration.getImplementation())) {
            return true;
        }
        return false;
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        executor.exec(buildCommand(event));
    }

    @Override
    protected String buildNotificationMessage(MavenExecutionResult result) {
        StringBuilder builder = new StringBuilder();
        builder.append(result.getProject().getName());
        builder.append(": ");
        Status status = result.hasExceptions() ? Status.FAILURE : Status.SUCCESS;
        builder.append(status.message());
        return builder.toString();
    }

    private String[] buildCommand(MavenExecutionResult result) {
        String[] commands = new String[9];
        commands[0] = configuration.getNotificationCenterPath();
        commands[1] = CMD_TITLE;
        commands[2] = result.getProject().getName();
        commands[3] = CMD_MESSAGE;
        commands[4] = buildNotificationMessage(result);
        commands[5] = CMD_GROUP;
        commands[6] = GROUP;
        commands[7] = CMD_OPEN;
        commands[8] = result.getProject().getBuild().getDirectory();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: {}", Joiner.on(" ").skipNulls().join(commands));
        }

        return commands;
    }
}
