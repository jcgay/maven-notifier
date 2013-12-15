package com.github.jcgay.maven.notifier;

import com.google.common.base.Stopwatch;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AbstractCustomEventSpyTest {

    private AbstractCustomEventSpy eventSpy;
    private Stopwatch stopwatch;

    @BeforeMethod
    public void setUp() throws Exception {
        eventSpy = new AbstractCustomEventSpy() {};
        stopwatch = new Stopwatch(new KnownElapsedTimeTicker(TimeUnit.SECONDS.toNanos(2L)));
        eventSpy.setStopwatch(stopwatch);
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
}
