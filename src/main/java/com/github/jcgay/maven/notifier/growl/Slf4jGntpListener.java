package com.github.jcgay.maven.notifier.growl;

import com.google.code.jgntp.GntpErrorStatus;
import com.google.code.jgntp.GntpListener;
import com.google.code.jgntp.GntpNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jGntpListener implements GntpListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jGntpListener.class);

    @Override
    public void onRegistrationSuccess() {
        LOGGER.info("Registration success !");
    }

    @Override
    public void onNotificationSuccess(GntpNotification gntpNotification) {
        LOGGER.info("Notification success: {}", gntpNotification);
    }

    @Override
    public void onClickCallback(GntpNotification gntpNotification) {
       LOGGER.info("Click callback: {}", gntpNotification);
    }

    @Override
    public void onCloseCallback(GntpNotification gntpNotification) {
        LOGGER.info("Close callback: {}", gntpNotification);
    }

    @Override
    public void onTimeoutCallback(GntpNotification gntpNotification) {
        LOGGER.info("Timeout callback: {}", gntpNotification);
    }

    @Override
    public void onRegistrationError(GntpErrorStatus gntpErrorStatus, String s) {
        LOGGER.error("Registration error: {} - {}", gntpErrorStatus, s);
    }

    @Override
    public void onNotificationError(GntpNotification gntpNotification, GntpErrorStatus gntpErrorStatus, String s) {
        String message = String.format("Notification error: %s - %s - %s", gntpErrorStatus, s, gntpNotification.getText());
        LOGGER.error(message);
    }

    @Override
    public void onCommunicationError(Throwable throwable) {
        LOGGER.error("Communication error", throwable);
    }
}
