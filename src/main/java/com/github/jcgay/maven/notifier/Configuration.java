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
    private String snarlPort;
    private String snarlHost;
    private String snarlPassword;
    private String notificationCenterSound;
    private boolean shortDescription;

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

    public int getSnarlPort() {
        return Integer.valueOf(snarlPort);
    }

    public void setSnarlPort(String snarlPort) {
        this.snarlPort = snarlPort;
    }

    public String getSnarlHost() {
        return snarlHost;
    }

    public void setSnarlHost(String snarlHost) {
        this.snarlHost = snarlHost;
    }

    public void setNotificationCenterSound(String notificationCenterSound) {
        this.notificationCenterSound = notificationCenterSound;
    }

    public String getNotificationCenterSound() {
        return notificationCenterSound;
    }

    public boolean isShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(boolean shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSnarlPassword() {
        return snarlPassword;
    }

    public void setSnarlPassword(String snarlPassword) {
        this.snarlPassword = snarlPassword;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("implementation", implementation)
                .add("notifySendPath", notifySendPath)
                .add("notifySendTimeout", notifySendTimeout)
                .add("notificationCenterPath", notificationCenterPath)
                .add("notificationCenterActivate", notificationCenterActivate)
                .add("notificationCenterSound", notificationCenterSound)
                .add("growlPort", growlPort)
                .add("growlHost", growlHost)
                .add("systemTrayWaitBeforeEnd", systemTrayWaitBeforeEnd)
                .add("snarlPort", snarlPort)
                .add("snarlHost", snarlHost)
                .add("shortDescription", shortDescription)
                .toString();
    }
}
