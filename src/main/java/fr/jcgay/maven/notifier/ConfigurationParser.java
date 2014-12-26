package fr.jcgay.maven.notifier;

import com.google.common.annotations.VisibleForTesting;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.IMPLEMENTATION;
import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.NOTIFY_WITH;
import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.SHORT_DESCRIPTION;
import static java.lang.Boolean.parseBoolean;

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
            return get(readProperties(url));
        } catch (IOException e) {
            logger.debug("Cannot read default configuration file: " + url, e);
            return defaultConfiguration();
        }
    }

    public static Properties readProperties() {
        try {
            return readProperties(configurationFile());
        } catch (IOException e) {
            return new Properties();
        }
    }

    @VisibleForTesting
    static Properties readProperties(URL url) throws IOException {
        Properties properties = new Properties();
        properties.load(url.openStream());
        String overrideImplementation = System.getProperty(NOTIFY_WITH.key());
        if (overrideImplementation != null) {
            properties.put(IMPLEMENTATION.key(), overrideImplementation);
        }
        return properties;
    }

    private URL getConfigurationUrl() {
        try {
            return configurationFile();
        } catch (MalformedURLException e) {
            logger.debug("Cannot create URL for default configuration file.", e);
            return null;
        }
    }

    private static URL configurationFile() throws MalformedURLException {
        return new URL(ConfigurationParser.class.getProtectionDomain().getCodeSource().getLocation(), "maven-notifier.properties");
    }

    @VisibleForTesting Configuration get(Properties properties) {
        Configuration configuration = parse(new ConfigurationProperties(properties));
        logger.debug("Notifier will use configuration: " + configuration);
        return configuration;
    }

    private Configuration parse(ConfigurationProperties properties) {
        Configuration configuration = new Configuration();
        configuration.setImplementation(properties.get(IMPLEMENTATION));
        configuration.setShortDescription(parseBoolean(properties.get(SHORT_DESCRIPTION)));
        return configuration;
    }

    private Configuration defaultConfiguration() {
        return get(new Properties());
    }

    public static class ConfigurationProperties {

        private static final String OS_NAME = "os.name";

        private final Properties properties;

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
                return "growl";
            }
            return "notifysend";
        }

        private boolean isMacos(String os) {
            return os.contains("mac");
        }

        private boolean isWindows(String os) {
            return os.contains("win");
        }

        public static enum Property {
            IMPLEMENTATION("notifier.implementation"),
            SHORT_DESCRIPTION("notifier.message.short", "false"),
            NOTIFY_WITH("notifyWith");

            private final String key;
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
