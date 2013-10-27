package com.github.jcgay.maven.notifier.systemtray;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Notifier;
import com.github.jcgay.maven.notifier.Status;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

@Component(role = Notifier.class, hint = "system-tray")
public class SystemTrayNotifier extends AbstractCustomEventSpy {

    private boolean skipNotifications;
    private TrayIcon icon;

    @Override
    public void init(EventSpy.Context context) {
        if (!SystemTray.isSupported()) {
            skipNotifications = true;
            logger.warn("SystemTray is not supported, skipping notifications...");
            return;
        }

        icon = new TrayIcon(createImage(readBuildIcon()), "Maven");
        icon.setImageAutoSize(true);

        try {
            SystemTray.getSystemTray().add(icon);
        } catch (AWTException e) {
            throw new RuntimeException("Error initializing SystemTray Icon.", e);
        }
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        Status status = getBuildStatus(event);
        icon.setImage(createImage(status.toByteArray()));
        icon.displayMessage(event.getProject().getName(), buildNotificationMessage(event), toMessageType(status));
    }

    private TrayIcon.MessageType toMessageType(Status status) {
        return status == Status.FAILURE ? TrayIcon.MessageType.WARNING : TrayIcon.MessageType.INFO;
    }

    @Override
    public void close() {
        if (!skipNotifications) {
            try {
                Thread.sleep(configuration.getSystemTrayWaitBeforeEnd());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            SystemTray.getSystemTray().remove(icon);
        }
    }

    private Image createImage(byte[] imageData) {
        return Toolkit.getDefaultToolkit().createImage(imageData);
    }

    private byte[] readBuildIcon() {
        InputStream image = getClass().getResourceAsStream("/Action-build-icon.png");
        try {
            return ByteStreams.toByteArray(image);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading SystemTray icon.", e);
        } finally {
            Closeables.closeQuietly(image);
        }
    }
}
