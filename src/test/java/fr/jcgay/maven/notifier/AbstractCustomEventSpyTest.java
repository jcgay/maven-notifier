package fr.jcgay.maven.notifier;

import com.google.common.base.Stopwatch;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class AbstractCustomEventSpyTest {

    private AbstractCustomEventSpy eventSpy;
    private Stopwatch stopwatch;
    private Configuration configuration;

    @BeforeMethod
    public void setUp() throws Exception {
        eventSpy = new AbstractCustomEventSpy() {};
        stopwatch = new Stopwatch(new KnownElapsedTimeTicker(TimeUnit.SECONDS.toNanos(2L)));
        eventSpy.setStopwatch(stopwatch);

        configuration = new Configuration();
        ConfigurationParser parser = mock(ConfigurationParser.class);
        when(parser.get()).thenReturn(configuration);
        eventSpy.setConfiguration(parser);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        System.setProperty(AbstractCustomEventSpy.OVERRIDE_NOTIFIER_IMPLEMENTATION, "");
    }

    @Test
    public void should_start_timer_when_initiating_event_spy() throws Exception {

        eventSpy.init(new EventSpy.Context() {
            @Override
            public Map<String, Object> getData() {
                return Collections.emptyMap();
            }
        });

        assertTrue(stopwatch.isRunning());
    }

    @Test
    public void should_stop_timer_when_closing_event_spy() throws Exception {

        eventSpy.init(new EventSpy.Context() {
            @Override
            public Map<String, Object> getData() {
                return Collections.emptyMap();
            }
        });
        eventSpy.onEvent(new DefaultMavenExecutionResult());

        assertEquals(stopwatch.elapsedTime(TimeUnit.SECONDS), 2L);
    }

    @Test
    public void should_override_notifier_implementation_with_system_property() throws Exception {

        System.setProperty(AbstractCustomEventSpy.OVERRIDE_NOTIFIER_IMPLEMENTATION, "Custom");
        configuration.setImplementation("growl");

        assertTrue(eventSpy.shouldNotify());
    }

    @Test
    public void should_not_notify_when_system_property_is_set_and_does_not_match_implementation() throws Exception {

        System.setProperty(AbstractCustomEventSpy.OVERRIDE_NOTIFIER_IMPLEMENTATION, "growl");
        configuration.setImplementation("Custom");

        assertFalse(eventSpy.shouldNotify());
    }
}
