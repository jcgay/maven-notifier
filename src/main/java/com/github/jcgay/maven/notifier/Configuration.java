package com.github.jcgay.maven.notifier;

import com.google.common.base.Objects;

public class Configuration {

    private String implementation;
    private String notifySendPath;
    private String notifySendTimeout;
    private String notificationCenterPath;
    private String growlPort;

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getImplementation() {
        return implementation;
    }

    public String getNotifySendPath() {
        return notifySendPath;
    }

    public void setNotifySendPath(String notifySendPath) {
        this.notifySendPath = notifySendPath;
    }

    public long getNotifySendTimeout() {
        return Long.valueOf(notifySendTimeout);
    }

    public void setNotifySendTimeout(String notifySendTimeout) {
        this.notifySendTimeout = notifySendTimeout;
    }

    public String getNotificationCenterPath() {
        return notificationCenterPath;
    }

    public void setNotificationCenterPath(String notificationCenterPath) {
        this.notificationCenterPath = notificationCenterPath;
    }

    public int getGrowlPort() {
        return Integer.valueOf(growlPort);
    }

    public void setGrowlPort(String growlPort) {
        this.growlPort = growlPort;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("implementation", implementation)
                .add("notifySendPath", notifySendPath)
                .add("notifySendTimeout", notifySendTimeout)
                .add("notificationCenterPath", notificationCenterPath)
                .add("growlPort", growlPort)
                .toString();
    }
}
