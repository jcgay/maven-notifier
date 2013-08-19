package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import com.github.jcgay.maven.notifier.notificationcenter.NotificationCenterEventSpy;
import com.github.jcgay.maven.notifier.notifysend.NotifySendEventSpy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

public class NotificationEventSpyChooserTest {

    @InjectMocks
    private NotificationEventSpyChooser chooser = new NotificationEventSpyChooser();

    @Mock
    private ConfigurationParser parser;

    @Mock
    private Notifier notifier;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "configuration_implementation")
    public void should_select_spy_based_on_configuration_implementation(String implementation, Class expected) throws Exception {

        Configuration configuration = newConfiguration();
        configuration.setImplementation(implementation);
        when(parser.get()).thenReturn(configuration);

        chooser.init(null);

        assertTrue(chooser.getSpy().getClass().isAssignableFrom(expected));
    }

    @DataProvider
    private Object[][] configuration_implementation() {
        return new Object[][] {
                {GrowlEventSpy.class.getName(), GrowlEventSpy.class},
                {NotifySendEventSpy.class.getName(), NotifySendEventSpy.class},
                {NotificationCenterEventSpy.class.getName(), NotificationCenterEventSpy.class},
                {"notificationcenter", NotificationCenterEventSpy.class},
                {"notifysend", NotifySendEventSpy.class},
                {"growl", GrowlEventSpy.class}
        };
    }

    @Test
    public void should_not_notify_if_event_is_not_a_build_result() throws Exception {

        chooser.onEvent("this is not a build result");

        verifyZeroInteractions(notifier);
    }

    private Configuration newConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setGrowlPort("1");
        return configuration;
    }
}
