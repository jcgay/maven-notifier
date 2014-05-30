package com.github.jcgay.maven.notifier.growl;

import com.google.code.jgntp.GntpErrorStatus;
import com.google.code.jgntp.GntpListener;
import com.google.code.jgntp.GntpNotification;
import org.codehaus.plexus.logging.Logger;

public class PlexusLoggerGntpListener implements GntpListener {

    private static final String OSX_URL = "https://github.com/jcgay/maven-notifier/wiki/Growl-(OS-X)";
    private static final String WINDOWS_URL = "https://github.com/jcgay/maven-notifier/wiki/Growl-(Windows)";

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
        logger.error(message(String.format("Growl Registration error: %s - %s", gntpErrorStatus, s)));
    }

    @Override
    public void onNotificationError(GntpNotification gntpNotification, GntpErrorStatus gntpErrorStatus, String s) {
        logger.error(message(String.format("Growl Notification error: %s - %s - %s", gntpErrorStatus, s, gntpNotification.getText())));
    }

    @Override
    public void onCommunicationError(Throwable throwable) {
        logger.error(message("Growl Communication error"), throwable);
    }

    private static String message(String message) {
        return String.format("%s%n%n" +
                        "For more information about the errors and possible solutions, please read the following article:%n%s",
                message, getUrl());
    }

    private static String getUrl() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return WINDOWS_URL;
        }
        if (os.contains("mac")) {
            return OSX_URL;
        }
        return "https://github.com/jcgay/maven-notifier/wiki";
    }
}
