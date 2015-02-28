package fr.jcgay.maven.notifier

import com.google.common.base.Stopwatch
import groovy.transform.CompileStatic
import org.apache.maven.execution.DefaultMavenExecutionResult
import org.apache.maven.execution.MavenExecutionResult
import org.codehaus.plexus.logging.Logger
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.THRESHOLD
import static java.util.concurrent.TimeUnit.SECONDS
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.verifyZeroInteractions
import static org.mockito.Mockito.when

@CompileStatic
class AbstractCustomEventSpyThresholdTest {

    private TestNotifier spy
    private DummyNotifier notifier
    private Configuration configuration

    @BeforeMethod
    void init() throws Exception {
        notifier = mock(DummyNotifier)
        spy = new TestNotifier(notifier: notifier)
        initConfiguration(spy)
        spy.logger = mock(Logger)
    }

    @Test
    void 'should not send notification when build ends before threshold'() throws Exception {
        configuration.threshold = 10
        build_will_last(SECONDS.toNanos(2L))

        spy.init({ Collections.emptyMap() })
        spy.onEvent(new DefaultMavenExecutionResult())

        verifyZeroInteractions(notifier)
    }

    @Test
    void 'should send notification when build ends after threshold'() throws Exception {
        configuration.threshold = 1
        build_will_last(SECONDS.toNanos(2L))

        spy.init({ Collections.emptyMap() })
        spy.onEvent(new DefaultMavenExecutionResult())

        verify(notifier).send()
    }

    @Test
    void 'should send notification when threshold is set to its default value'() throws Exception {
        configuration.threshold = THRESHOLD.defaultValue() as int
        build_will_last(SECONDS.toNanos(2L))

        spy.init({ Collections.emptyMap() })
        spy.onEvent(new DefaultMavenExecutionResult())

        verify(notifier).send()
    }

    private void build_will_last(Long time) {
        def stopwatch = new Stopwatch(new KnownElapsedTimeTicker(time))
        spy.stopwatch = stopwatch
    }

    private void initConfiguration(TestNotifier notifier) {
        configuration = new Configuration()
        def parser = mock(ConfigurationParser)
        when parser.get() thenReturn configuration
        notifier.configuration = parser
    }

    static class TestNotifier extends AbstractCustomEventSpy {
        private DummyNotifier notifier

        @Override
        protected void fireNotification(MavenExecutionResult event) {
            notifier.send()
        }

        @Override
        protected void configure() {

        }
    }
}
