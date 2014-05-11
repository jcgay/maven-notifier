package com.github.jcgay.maven.notifier.notifysend;

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

import java.util.List;

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
        executor.exec(buildCommand(buildTitle(event), buildNotificationMessage(event), getBuildStatus(event)));
    }

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        executor.exec(buildCommand("Build Error", buildErrorDescription(exceptions), Status.FAILURE));
    }

    private String[] buildCommand(String title, String message, Status status) {
        String[] commands = new String[7];
        commands[0] = configuration.getNotifySendPath();
        commands[1] = title;
        commands[2] = message;
        commands[3] = CMD_TIMEOUT;
        commands[4] = String.valueOf(configuration.getNotifySendTimeout());
        commands[5] = CMD_ICON;
        commands[6] = status.asPath();

        if (logger.isDebugEnabled()) {
            logger.debug("Will execute command line: " + Joiner.on(" ").join(commands));
        }

        return commands;
    }
}
