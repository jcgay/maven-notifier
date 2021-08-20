package fr.jcgay.maven.notifier

import com.google.common.base.Stopwatch
import groovy.transform.CompileStatic
import org.apache.maven.execution.DefaultMavenExecutionResult
import org.apache.maven.execution.MavenExecutionResult
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.Fixtures.skipSendNotificationInit
import static java.util.concurrent.TimeUnit.SECONDS
import static org.assertj.core.api.Assertions.assertThat

@CompileStatic
class AbstractNotifierTest {

    private AbstractNotifier eventSpy
    private Stopwatch stopwatch

    @BeforeMethod
    void setUp() throws Exception {
        eventSpy = new AbstractNotifier() {
            @Override
            protected void fireNotification(MavenExecutionResult event) {

            }
        }
        stopwatch = Stopwatch.createUnstarted(new KnownElapsedTimeTicker(SECONDS.toNanos(2L)))
        eventSpy.stopwatch = stopwatch
    }

    @Test
    void 'should start timer when initiating event spy'() throws Exception {

        eventSpy.init(skipSendNotificationInit())

        assertThat stopwatch.isRunning() isTrue()
    }

    @Test
    void 'should stop timer when listening to an event'() throws Exception {

        eventSpy.init(skipSendNotificationInit())
        eventSpy.onEvent(new DefaultMavenExecutionResult())

        assertThat stopwatch.isRunning() isFalse()
        assertThat stopwatch.elapsed(SECONDS) isEqualTo 2L
    }

    @Test
    void 'should reset time when closing event spy'() {

        eventSpy.init({ Collections.emptyMap() })
        eventSpy.close()

        assertThat stopwatch.isRunning() isFalse()
        assertThat stopwatch.elapsed(SECONDS) isEqualTo 0L
    }
}
