package com.github.jcgay.maven.notifier.notifysend;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.Notifier;
import com.github.jcgay.maven.notifier.executor.Executor;
import com.github.jcgay.maven.notifier.executor.RuntimeExecutor;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = Notifier.class, hint = "notify-send")
public class NotifySendNotifier extends AbstractCustomEventSpy {

    private static final String CMD_TIMEOUT = "-t";
    private static final String CMD_ICON = "-i";

    private final Executor executor;

    public NotifySendNotifier() {
        this.executor = new RuntimeExecutor();
    }

    @VisibleForTesting
    NotifySendNotifier(Executor executor, Configuration configuration) {
        this.executor = executor;
        this.configuration = configuration;
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        executor.exec(buildCommand(event));
    }

    private String[] buildCommand(MavenExecutionResult result) {
        String[] commands = new String[7];
        commands[0] = configuration.getNotifySendPath();
        commands[1] = buildTitle(result);
        commands[2] = buildNotificationMessage(result);
        commands[3] = CMD_TIMEOUT;
        commands[4] = String.valueOf(configuration.getNotifySendTimeout());
        commands[5] = CMD_ICON;
        commands[6] = getBuildStatus(result).asPath();

        if (logger.isDebugEnabled()) {
            logger.debug("Will execute command line: " + Joiner.on(" ").join(commands));
        }

        return commands;
    }
}
