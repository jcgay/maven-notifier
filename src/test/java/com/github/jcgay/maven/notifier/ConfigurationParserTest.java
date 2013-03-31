package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import com.github.jcgay.maven.notifier.notifysend.NotifySendEventSpy;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;

import static com.github.jcgay.maven.notifier.ConfigurationParser.Property;
import static org.testng.Assert.assertEquals;

public class ConfigurationParserTest {

    @Test(dataProvider = "os_and_notifier_implementation")
    public void should_return_default_implementation_configuration(String os, String expectedImplementation) throws Exception {

        // Given
        Properties properties = new Properties();
        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
        properties.setProperty("os.name", os);

        ConfigurationParser parser = new ConfigurationParser(properties);

        // When
        Configuration result = parser.get();

        // Then
        assertEquals(result.getImplementation(), expectedImplementation);
    }

    @DataProvider
    private Object[][] os_and_notifier_implementation() {
        // outdated os list: http://lopica.sourceforge.net/os.html
        return new Object[][] {
                {"Mac OS X", GrowlEventSpy.class.getName()},
                {"Windows XP", GrowlEventSpy.class.getName()},
                {"Linux", NotifySendEventSpy.class.getName()}
        };
    }

    @Test
    public void should_return_default_configuration() throws Exception {

        ConfigurationParser parser = new ConfigurationParser(new Properties());
        Configuration result = parser.get();

        assertEquals(result.getNotifySendPath(), Property.NOTIFY_SEND_PATH.defaultValue());
        assertEquals(Long.valueOf(result.getNotifySendTimeout()), Long.valueOf(Property.NOTIFY_SEND_TIMEOUT.defaultValue()));
        assertEquals(result.getNotificationCenterPath(), Property.NOTIFICATION_CENTER_PATH.defaultValue());
        assertEquals(Integer.valueOf(result.getGrowlPort()), Integer.valueOf(Property.GROWL_PORT.defaultValue()));
    }

    @Test
    public void should_return_configuration() {

        Properties properties = new Properties();
        properties.put(Property.IMPLEMENTATION.key(), "test");
        properties.put(Property.NOTIFY_SEND_PATH.key(), "notify-send.path");
        properties.put(Property.NOTIFY_SEND_TIMEOUT.key(), "1");
        properties.put(Property.NOTIFICATION_CENTER_PATH.key(), "notification-center.path");
        properties.put(Property.GROWL_PORT.key(), "1");

        ConfigurationParser parser = new ConfigurationParser(properties);

        Configuration result = parser.get();
        assertEquals(result.getImplementation(), "test");
        assertEquals(result.getNotifySendPath(), "notify-send.path");
        assertEquals(result.getNotifySendTimeout(), 1);
        assertEquals(result.getNotificationCenterPath(), "notification-center.path");
        assertEquals(result.getGrowlPort(), 1);
    }
}
