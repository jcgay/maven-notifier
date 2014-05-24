package com.github.jcgay.maven.notifier.growl;

import com.google.code.jgntp.GntpErrorStatus;
import com.google.code.jgntp.GntpListener;
import com.google.code.jgntp.GntpNotification;
import org.codehaus.plexus.logging.Logger;

public class PlexusLoggerGntpListener implements GntpListener {

    private final Logger logger;

    public PlexusLoggerGntpListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onRegistrationSuccess() {
        logger.debug("Growl Registration success !");
    }

    @Override
    public void onNotificationSuccess(GntpNotification gntpNotification) {
        logger.debug("Growl Notification success: " + gntpNotification);
    }

    @Override
    public void onClickCallback(GntpNotification gntpNotification) {
        logger.debug("Growl Click callback: " + gntpNotification);
    }

    @Override
    public void onCloseCallback(GntpNotification gntpNotification) {
        logger.debug("Growl Close callback: " + gntpNotification);
    }

    @Override
    public void onTimeoutCallback(GntpNotification gntpNotification) {
        logger.debug("Growl Timeout callback: " + gntpNotification);
    }

    @Override
    public void onRegistrationError(GntpErrorStatus gntpErrorStatus, String s) {
        logger.error(String.format("Growl Registration error: %s - %s", gntpErrorStatus, s));
    }

    @Override
    public void onNotificationError(GntpNotification gntpNotification, GntpErrorStatus gntpErrorStatus, String s) {
        logger.error(String.format("Growl Notification error: %s - %s - %s", gntpErrorStatus, s, gntpNotification.getText()));
    }

    @Override
    public void onCommunicationError(Throwable throwable) {
        logger.error("Growl Communication error", throwable);
    }
}
