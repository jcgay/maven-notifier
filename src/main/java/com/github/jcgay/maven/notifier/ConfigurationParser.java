package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlNotifier;
import com.github.jcgay.maven.notifier.notifysend.NotifySendNotifier;
import com.google.common.annotations.VisibleForTesting;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.GROWL_HOST;
import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.GROWL_PORT;
import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.IMPLEMENTATION;
import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.NOTIFICATION_CENTER_ACTIVATE;
import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.NOTIFICATION_CENTER_PATH;
import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.NOTIFY_SEND_PATH;
import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.NOTIFY_SEND_TIMEOUT;
import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.SYSTEM_TRAY_WAIT;

@Component(role = ConfigurationParser.class, hint = "maven-notifier-configuration")
public class ConfigurationParser {

    @Requirement
    private Logger logger;

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
        Configuration configuration = parse(new ConfigurationProperties(properties));
        logger.debug("Notifier will use configuration: " + configuration);
        return configuration;
    }

    private Configuration parse(ConfigurationProperties properties) {
        Configuration configuration = new Configuration();
        configuration.setImplementation(properties.get(IMPLEMENTATION));
        configuration.setNotifySendPath(properties.get(NOTIFY_SEND_PATH));
        configuration.setNotifySendTimeout(properties.get(NOTIFY_SEND_TIMEOUT));
        configuration.setNotificationCenterPath(properties.get(NOTIFICATION_CENTER_PATH));
        configuration.setNotificationCenterActivate(properties.get(NOTIFICATION_CENTER_ACTIVATE));
        configuration.setGrowlHost(properties.get(GROWL_HOST));
        configuration.setGrowlPort(properties.get(GROWL_PORT));
        configuration.setSystemTrayWaitBeforeEnd(properties.get(SYSTEM_TRAY_WAIT));
        return configuration;
    }

    private Configuration defaultConfiguration() {
        return get(new Properties());
    }

    public static class ConfigurationProperties {

        private static final String OS_NAME = "os.name";

        private Properties properties;

        private ConfigurationProperties(Properties properties) {
            this.properties = properties;
            if (currentOs() == null) {
                properties.put(OS_NAME, System.getProperty(OS_NAME));
            }
        }

        public String get(Property property) {
            switch (property) {
                case IMPLEMENTATION:
                    return properties.getProperty(property.key(), defaultImplementation());
                default:
                    return properties.getProperty(property.key(), property.defaultValue());
            }
        }

        public String currentOs() {
            return properties.getProperty(OS_NAME);
        }

        private String defaultImplementation() {
            String os = currentOs().toLowerCase();
            if (isMacos(os) || isWindows(os)) {
                return GrowlNotifier.class.getName();
            }
            return NotifySendNotifier.class.getName();
        }

        private boolean isMacos(String os) {
            return os.indexOf("mac") != -1;
        }

        private boolean isWindows(String os) {
            return os.indexOf("win") != -1;
        }

        public static enum Property {
            IMPLEMENTATION("notifier.implementation"),
            NOTIFY_SEND_PATH("notifier.notify-send.path", "notify-send"),
            NOTIFY_SEND_TIMEOUT("notifier.notify-send.timeout", String.valueOf(TimeUnit.SECONDS.toMillis(2))),
            NOTIFICATION_CENTER_PATH("notifier.notification-center.path", "terminal-notifier"),
            NOTIFICATION_CENTER_ACTIVATE("notifier.notification-center.activate", "com.apple.Terminal"),
            GROWL_PORT("notifier.growl.port", String.valueOf(23053)),
            GROWL_HOST("notifier.growl.host"),
            SYSTEM_TRAY_WAIT("notifier.system-tray.wait", String.valueOf(TimeUnit.SECONDS.toMillis(2)));

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
    }
}
