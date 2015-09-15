package fr.jcgay.maven.notifier;

import com.google.common.annotations.VisibleForTesting;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
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

    public ConfigurationParser() {
    }

    public ConfigurationParser(Logger logger) {
        this.logger = logger;
    }

    public Configuration get() {
        return get(globalConfiguration(), userConfiguration());
    }

    public Configuration get(URL... urls) {
        return get(readProperties(urls));
    }

    @VisibleForTesting Configuration get(Properties properties) {
        Configuration configuration = parse(new ConfigurationProperties(properties));
        logger.debug("maven-notifier user configuration: " + configuration);
        return configuration;
    }

    private Properties readProperties(URL... urls) {
        return new ConfiguredProperties(logger)
            .load(urls)
            .properties();
    }

    private URL globalConfiguration() {
        try {
            URL url = new URL(ConfigurationParser.class.getProtectionDomain().getCodeSource().getLocation(), "maven-notifier.properties");
            logger.debug("Global configuration is located at: " + url);
            return url;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private URL userConfiguration() {
        try {
            URL url = new URL("file://" + System.getProperty("user.home") + "/.m2/maven-notifier.properties");
            logger.debug("User specific configuration is located at: " + url);
            return url;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private Configuration parse(ConfigurationProperties properties) {
        Configuration configuration = new Configuration();
        configuration.setImplementation(properties.get(IMPLEMENTATION));
        configuration.setShortDescription(parseBoolean(properties.get(SHORT_DESCRIPTION)));
        configuration.setThreshold(Integer.valueOf(properties.get(THRESHOLD)));
        configuration.setNotifierProperties(properties.all());
        return configuration;
    }

    public static class ConfigurationProperties {

        private final Properties properties;

        private ConfigurationProperties(Properties properties) {
            this.properties = properties;
        }

        public String get(Property property) {
            switch (property) {
                case IMPLEMENTATION:
                    return properties.getProperty(property.key(), "send-notification");
                default:
                    return properties.getProperty(property.key(), property.defaultValue());
            }
        }

        public Properties all() {
            return properties;
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

        private final Logger logger;
        private final Properties properties = new Properties();

        public ConfiguredProperties(Logger logger) {
            this.logger = logger;
        }

        public Properties properties() {
            Properties result = new Properties();
            result.putAll(properties);

            for (Map.Entry<Object, Object> property : System.getProperties().entrySet()) {
                if (property.getKey().toString().startsWith("notifier.")) {
                    result.put(property.getKey(), property.getValue());
                }
            }

            String overrideImplementation = System.getProperty(NOTIFY_WITH.key());
            if (overrideImplementation != null) {
                logger.debug("Overriding configured notifier with: " + overrideImplementation);
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
                        logger.debug("Properties after loading [" + url + "]: " + properties);
                    } catch (IOException e) {
                        // cannot read configuration file (which is not mandatory)
                        logger.debug("Can't read file at [" + url + "]. Skipping it...", e);
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
