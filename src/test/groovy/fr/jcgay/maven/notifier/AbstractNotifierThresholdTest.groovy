package fr.jcgay.maven.notifier

import com.google.common.base.Stopwatch
import groovy.transform.CompileStatic
import org.apache.maven.execution.DefaultMavenExecutionResult
import org.apache.maven.execution.MavenExecutionResult
import org.codehaus.plexus.logging.Logger
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.THRESHOLD
import static fr.jcgay.maven.notifier.Fixtures.skipSendNotificationInit
import static java.util.concurrent.TimeUnit.SECONDS
import static org.mockito.Mockito.*

@CompileStatic
class AbstractNotifierThresholdTest {

    private TestNotifier spy
    private DummyNotifier notifier
    private Configuration configuration

    @BeforeMethod
    void init() throws Exception {
        notifier = mock(DummyNotifier)
        spy = new TestNotifier(notifier: notifier)
        configuration = new Configuration()
        spy.logger = mock(Logger)
    }

    @Test
    void 'should not send notification when build ends before threshold'() throws Exception {
        configuration.threshold = 10
        build_will_last(SECONDS.toNanos(2L))

        spy.init(skipSendNotificationInit(configuration))
        spy.onEvent(new DefaultMavenExecutionResult())

        verifyZeroInteractions(notifier)
    }

    @Test
    void 'should send notification when build ends after threshold'() throws Exception {
        configuration.threshold = 1
        build_will_last(SECONDS.toNanos(2L))

        spy.init(skipSendNotificationInit(configuration))
        spy.onEvent(new DefaultMavenExecutionResult())

        verify(notifier).send()
    }

    @Test
    void 'should send notification when threshold is set to its default value'() throws Exception {
        configuration.threshold = THRESHOLD.defaultValue() as int
        build_will_last(SECONDS.toNanos(2L))

        spy.init(skipSendNotificationInit(configuration))
        spy.onEvent(new DefaultMavenExecutionResult())

        verify(notifier).send()
    }

    private void build_will_last(Long time) {
        def stopwatch = Stopwatch.createUnstarted(new KnownElapsedTimeTicker(time))
        spy.stopwatch = stopwatch
    }

    static class TestNotifier extends AbstractNotifier {
        private DummyNotifier notifier

        @Override
        protected void fireNotification(MavenExecutionResult event) {
            notifier.send()
        }
    }
}
