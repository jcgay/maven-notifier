package fr.jcgay.maven.notifier;

import com.google.common.annotations.VisibleForTesting;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.IMPLEMENTATION;
import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.NOTIFY_WITH;
import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.SHORT_DESCRIPTION;
import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.THRESHOLD;
import static java.lang.Boolean.parseBoolean;

@Component(role = ConfigurationParser.class, hint = "maven-notifier-configuration")
public class ConfigurationParser {

    @Requirement
    private Logger logger;

    public Configuration get() {
        return get(readProperties());
    }

    public static Properties readProperties() {
        return readProperties(globalConfiguration(), userConfiguration());
    }

    @VisibleForTesting static Properties readProperties(URL... urls) {
        return new ConfiguredProperties()
            .load(urls)
            .properties();
    }

    private static URL globalConfiguration() {
        try {
            return new URL(ConfigurationParser.class.getProtectionDomain().getCodeSource().getLocation(), "maven-notifier.properties");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static URL userConfiguration() {
        try {
            return new URL("file://" + System.getProperty("user.home") + "/.m2/maven-notifier.properties");
        } catch (MalformedURLException e) {
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
        configuration.setShortDescription(parseBoolean(properties.get(SHORT_DESCRIPTION)));
        configuration.setThreshold(Integer.valueOf(properties.get(THRESHOLD)));
        return configuration;
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

        public enum Property {
            IMPLEMENTATION("notifier.implementation"),
            SHORT_DESCRIPTION("notifier.message.short", "true"),
            NOTIFY_WITH("notifyWith"),
            THRESHOLD("notifier.threshold", "-1");

            private final String key;
            private String defaultValue;

            Property(String key) {
                this.key = key;
            }

            Property(String key, String defaultValue) {
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

    private static class ConfiguredProperties {

        private final Properties properties = new Properties();

        public Properties properties() {
            Properties result = new Properties();
            result.putAll(properties);
            String overrideImplementation = System.getProperty(NOTIFY_WITH.key());
            if (overrideImplementation != null) {
                result.put(IMPLEMENTATION.key(), overrideImplementation);
            }
            return result;
        }

        public ConfiguredProperties load(URL... urls) {
            for (URL url : urls) {
                if (url != null) {
                    InputStream in = null;
                    try {
                        in = url.openStream();
                        properties.load(in);
                    } catch (IOException ignored) {
                        // cannot read configuration file (which is not mandatory)
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException ignored) {
                            }
                        }
                    }
                }
            }
            return this;
        }
    }
}
