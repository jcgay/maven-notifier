package com.github.jcgay.maven.notifier;

import com.google.common.base.Objects;

public class Configuration {

    private String implementation;
    private String notifySendPath;
    private String notifySendTimeout;
    private String notificationCenterPath;
    private String growlPort;
    private String notificationCenterActivate;
    private String systemTrayWaitBeforeEnd;
    private String growlHost;
    private String growlPassword;

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

    public void setNotificationCenterActivate(String notificationCenterActivate) {
        this.notificationCenterActivate = notificationCenterActivate;
    }

    public String getNotificationCenterActivate() {
        return notificationCenterActivate;
    }

    public long getSystemTrayWaitBeforeEnd() {
        return Long.valueOf(systemTrayWaitBeforeEnd);
    }

    public void setSystemTrayWaitBeforeEnd(String systemTrayWaitBeforeEnd) {
        this.systemTrayWaitBeforeEnd = systemTrayWaitBeforeEnd;
    }

    public String getGrowlHost() {
        return growlHost;
    }

    public void setGrowlHost(String growlHost) {
        this.growlHost = growlHost;
    }

    public String getGrowlPassword() {
        return growlPassword;
    }

    public void setGrowlPassword(String growlPassword) {
        this.growlPassword = growlPassword;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("implementation", implementation)
                .add("notifySendPath", notifySendPath)
                .add("notifySendTimeout", notifySendTimeout)
                .add("notificationCenterPath", notificationCenterPath)
                .add("notificationCenterActivate", notificationCenterActivate)
                .add("growlPort", growlPort)
                .add("growlHost", growlHost)
                .add("systemTrayWaitBeforeEnd", systemTrayWaitBeforeEnd)
                .toString();
    }
}
