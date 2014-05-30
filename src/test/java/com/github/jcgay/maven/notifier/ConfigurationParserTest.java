package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlNotifier;
import com.github.jcgay.maven.notifier.notifysend.NotifySendNotifier;
import org.codehaus.plexus.logging.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;

import static com.github.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ConfigurationParserTest {

    @InjectMocks
    private ConfigurationParser parser = new ConfigurationParser();

    @Mock
    Logger logger;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "os_and_notifier_implementation")
    public void should_return_default_implementation_configuration(String os, String expectedImplementation) throws Exception {

        // Given
        Properties properties = new Properties();
        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
        properties.setProperty("os.name", os);

        // When
        Configuration result = parser.get(properties);

        // Then
        assertEquals(result.getImplementation(), expectedImplementation);
    }

    @DataProvider
    private Object[][] os_and_notifier_implementation() {
        // outdated os list: http://lopica.sourceforge.net/os.html
        return new Object[][] {
                {"Mac OS X", GrowlNotifier.class.getName()},
                {"Windows XP", GrowlNotifier.class.getName()},
                {"Linux", NotifySendNotifier.class.getName()}
        };
    }

    @Test
    public void should_return_default_configuration() throws Exception {

        Configuration result = parser.get(new Properties());

        assertEquals(result.getNotifySendPath(), Property.NOTIFY_SEND_PATH.defaultValue());
        assertEquals(Long.valueOf(result.getNotifySendTimeout()), Long.valueOf(Property.NOTIFY_SEND_TIMEOUT.defaultValue()));
        assertEquals(result.getNotificationCenterPath(), Property.NOTIFICATION_CENTER_PATH.defaultValue());
        assertEquals(result.getNotificationCenterActivate(), Property.NOTIFICATION_CENTER_ACTIVATE.defaultValue());
        assertEquals(Integer.valueOf(result.getGrowlPort()), Integer.valueOf(Property.GROWL_PORT.defaultValue()));
        assertEquals(Long.valueOf(result.getSystemTrayWaitBeforeEnd()), Long.valueOf(Property.SYSTEM_TRAY_WAIT.defaultValue()));
        assertEquals(Integer.valueOf(result.getSnarlPort()), Integer.valueOf(Property.SNARL_PORT.defaultValue()));
        assertEquals(result.getSnarlHost(), Property.SNARL_HOST.defaultValue());
        assertFalse(result.isShortDescription());
    }

    @Test
    public void should_return_configuration() {

        Properties properties = new Properties();
        properties.put(Property.IMPLEMENTATION.key(), "test");
        properties.put(Property.NOTIFY_SEND_PATH.key(), "notify-send.path");
        properties.put(Property.NOTIFY_SEND_TIMEOUT.key(), "1");
        properties.put(Property.NOTIFICATION_CENTER_PATH.key(), "notification-center.path");
        properties.put(Property.NOTIFICATION_CENTER_ACTIVATE.key(), "notification-center.activate");
        properties.put(Property.NOTIFICATION_CENTER_SOUND.key(), "notification-center.sound");
        properties.put(Property.GROWL_PORT.key(), "1");
        properties.put(Property.GROWL_HOST.key(), "192.168.0.1");
        properties.put(Property.GROWL_PASSWORD.key(), "a.password");
        properties.put(Property.SYSTEM_TRAY_WAIT.key(), "1");
        properties.put(Property.SNARL_PORT.key(), "1");
        properties.put(Property.SNARL_HOST.key(), "192.168.1.11");
        properties.put(Property.SNARL_PASSWORD.key(), "snarl.password");
        properties.put(Property.SHORT_DESCRIPTION.key(), "true");

        Configuration result = parser.get(properties);

        assertEquals(result.getImplementation(), "test");
        assertEquals(result.getNotifySendPath(), "notify-send.path");
        assertEquals(result.getNotifySendTimeout(), 1);
        assertEquals(result.getNotificationCenterPath(), "notification-center.path");
        assertEquals(result.getNotificationCenterActivate(), "notification-center.activate");
        assertEquals(result.getNotificationCenterSound(), "notification-center.sound");
        assertEquals(result.getGrowlPort(), 1);
        assertEquals(result.getGrowlHost(), "192.168.0.1");
        assertEquals(result.getGrowlPassword(), "a.password");
        assertEquals(result.getSystemTrayWaitBeforeEnd(), 1);
        assertEquals(result.getSnarlPort(), 1);
        assertEquals(result.getSnarlHost(), "192.168.1.11");
        assertEquals(result.getSnarlPassword(), "snarl.password");
        assertTrue(result.isShortDescription());
    }
}
