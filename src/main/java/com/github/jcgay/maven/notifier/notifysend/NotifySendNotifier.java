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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Component(role = Notifier.class, hint = "notify-send")
public class NotifySendNotifier extends AbstractCustomEventSpy {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifySendNotifier.class);

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
    public boolean shouldNotify() {
        if (this.getClass().getName().contains(configuration.getImplementation())) {
            return true;
        }
        return false;
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        executor.exec(buildCommand(event));
    }

    private String[] buildCommand(MavenExecutionResult result) {
        String[] commands = new String[7];
        commands[0] = configuration.getNotifySendPath();
        commands[1] = result.getProject().getName();
        commands[2] = buildNotificationMessage(result);
        commands[3] = CMD_TIMEOUT;
        commands[4] = String.valueOf(configuration.getNotifySendTimeout());
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
