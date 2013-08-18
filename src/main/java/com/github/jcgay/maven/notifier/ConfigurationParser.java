package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import com.github.jcgay.maven.notifier.notifysend.NotifySendEventSpy;
import com.google.common.annotations.VisibleForTesting;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component(role = ConfigurationParser.class, hint = "maven-notifier-configuration")
public class ConfigurationParser {

    @Requirement
    private org.codehaus.plexus.logging.Logger logger;

    public static enum Property {
        IMPLEMENTATION("notifier.implementation"),
        NOTIFY_SEND_PATH("notifier.notify-send.path", "/usr/bin/notify-send"),
        NOTIFY_SEND_TIMEOUT("notifier.notify-send.timeout", String.valueOf(TimeUnit.SECONDS.toMillis(2))),
        NOTIFICATION_CENTER_PATH("notifier.notification-center.path", "/Applications/terminal-notifier.app/Contents/MacOS/terminal-notifier"),
        GROWL_PORT("notifier.growl.port", String.valueOf(23053));

        private String key;
        private String defaultValue;

        private Property(String key) {
            this.key = key;
        }

        private Property(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String key() {
            return key;
        }

        public String defaultValue() {
            return defaultValue;
        }
    }

    public Configuration get() {
        URL url = getConfigurationUrl();

        if (url == null) {
            return defaultConfiguration();
        }

        try {
            Properties properties = new Properties();
            properties.load(url.openStream());
            return get(properties);
        } catch (IOException e) {
            logger.debug("Cannot read default configuration file: " + url, e);
            return defaultConfiguration();
        }
    }

    private URL getConfigurationUrl() {
        try {
            return new URL(this.getClass().getProtectionDomain().getCodeSource().getLocation(), "maven-notifier.properties");
        } catch (MalformedURLException e) {
            logger.debug("Cannot create URL for default configuration file.", e);
            return null;
        }
    }

    @VisibleForTesting Configuration get(Properties properties) {
        if (properties.getProperty("os.name") == null) {
            properties.put("os.name", System.getProperty("os.name"));
        }
        Configuration configuration = parse(properties);
        logger.debug("Notifier will use configuration: " + configuration);
        return configuration;
    }

    private Configuration parse(Properties properties) {
        Configuration configuration = new Configuration();
        configuration.setImplementation(properties.getProperty(Property.IMPLEMENTATION.key(), defaultImplementation(properties)));
        configuration.setNotifySendPath(properties.getProperty(Property.NOTIFY_SEND_PATH.key(), Property.NOTIFY_SEND_PATH.defaultValue()));
        configuration.setNotifySendTimeout(properties.getProperty(Property.NOTIFY_SEND_TIMEOUT.key(), Property.NOTIFY_SEND_TIMEOUT.defaultValue()));
        configuration.setNotificationCenterPath(properties.getProperty(Property.NOTIFICATION_CENTER_PATH.key(), Property.NOTIFICATION_CENTER_PATH.defaultValue()));
        configuration.setGrowlPort(properties.getProperty(Property.GROWL_PORT.key(), Property.GROWL_PORT.defaultValue()));
        return configuration;
    }

    private Configuration defaultConfiguration() {
        return get(new Properties());
    }

    private String defaultImplementation(Properties properties) {
        String os = ((String) properties.get("os.name")).toLowerCase();
        if (isMacos(os) || isWindows(os)) {
            return GrowlEventSpy.class.getName();
        }
        return NotifySendEventSpy.class.getName();
    }

    private boolean isMacos(String os) {
        return os.indexOf("mac") != -1;
    }

    private boolean isWindows(String os) {
        return os.indexOf("win") != -1;
    }
}
