package com.github.jcgay.maven.notifier.growl;

import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.ConfigurationParser;
import com.github.jcgay.maven.notifier.notifysend.NotifySendNotifier;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class GrowlNotifierTest {

    private GrowlNotifier notifier;

    private Configuration configuration;

    @BeforeMethod
    public void setUp() throws Exception {
        configuration = new Configuration();

        ConfigurationParser parser = mock(ConfigurationParser.class);
        when(parser.get()).thenReturn(configuration);

        notifier = new GrowlNotifier();
        notifier.setConfiguration(parser);
    }

    @Test
    public void should_match_configuration() throws Exception {

        configuration.setImplementation(GrowlNotifier.class.getName());
        assertTrue(notifier.shouldNotify());

        configuration.setImplementation("growl");
        assertTrue(notifier.shouldNotify());
    }

    @Test
    public void should_not_match_configuration() throws Exception {

        configuration.setImplementation(NotifySendNotifier.class.getName());
        assertFalse(notifier.shouldNotify());

        configuration.setImplementation("notificationcenter");
        assertFalse(notifier.shouldNotify());
    }
}
