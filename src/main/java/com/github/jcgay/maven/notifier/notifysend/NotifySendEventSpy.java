package com.github.jcgay.maven.notifier.notifysend;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Status;
import com.github.jcgay.maven.notifier.executor.Executor;
import com.github.jcgay.maven.notifier.executor.RuntimeExecutor;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import org.apache.maven.execution.MavenExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NotifySendEventSpy extends AbstractCustomEventSpy {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifySendEventSpy.class);

    private static final String NOTIFY_PATH = "/usr/bin/notify-send";
    private static final String CMD_TIMEOUT = "-t";
    private static final String CMD_ICON = "-i";

    private final Executor executor;

    public NotifySendEventSpy() {
        this.executor = new RuntimeExecutor();
    }

    @VisibleForTesting NotifySendEventSpy(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onEvent(Object event) throws Exception {
        super.onEvent(event);
        if (isExecutionResult(event)) {
            executor.exec(buildCommand((MavenExecutionResult) event));
        }
    }

    private String[] buildCommand(MavenExecutionResult result) {
        String[] commands = new String[7];
        commands[0] = NOTIFY_PATH;
        commands[1] = result.getProject().getName();
        commands[2] = buildNotificationMessage(result);
        commands[3] = CMD_TIMEOUT;
        commands[4] = String.valueOf(TimeUnit.SECONDS.toMillis(2));
        commands[5] = CMD_ICON;
        commands[6] = toFilePath(getBuildStatus(result));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: {}", Joiner.on(" ").join(commands));
        }

        return commands;
    }

    private String toFilePath(Status status) {
        String folder = System.getProperty("java.io.tmpdir") + "/maven-notifier-icons/";
        File icon = new File(folder + status.name() + ".png");
        if (!icon.exists()) {
            new File(folder).mkdirs();
            try {
                ImageIO.write(status.icon(), "png", icon);
            } catch (IOException e) {
                throw new RuntimeException("Can't write notification icon icon: " + icon.getPath(), e);
            }
        }
        return icon.getPath();
    }

}
