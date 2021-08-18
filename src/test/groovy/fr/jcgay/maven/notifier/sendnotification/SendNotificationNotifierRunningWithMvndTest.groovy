package fr.jcgay.maven.notifier.sendnotification

import fr.jcgay.maven.notifier.Configuration
import fr.jcgay.maven.notifier.ConfigurationParser
import fr.jcgay.notification.Notification
import fr.jcgay.notification.Notifier
import groovy.transform.CompileStatic
import org.codehaus.plexus.logging.Logger
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.Fixtures.anEvent
import static fr.jcgay.maven.notifier.KnownElapsedTimeTicker.aStartedStopwatchWithElapsedTime
import static java.util.concurrent.TimeUnit.SECONDS
import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.*
import static org.mockito.MockitoAnnotations.initMocks

@CompileStatic
class SendNotificationNotifierRunningWithMvndTest {

    @Mock
    private Notifier notifier

    @Captor
    private ArgumentCaptor<Notification> notification

    private Configuration configuration

    private SendNotificationNotifier underTest

    @BeforeMethod
    void setUp() throws Exception {
        initMocks this

        configuration = new Configuration()

        def parser = mock ConfigurationParser
        when parser.get() thenReturn configuration

        underTest = new SendNotificationNotifier(notifier, true)
        underTest.configuration = parser
        underTest.logger = mock(Logger)
    }

    @Test
    void 'should not call close when exiting notifier'() {
        underTest.close()

        verify(notifier, never()).close()
    }

    @Test
    void 'should send notification when an event is triggered'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(2L))

        underTest.onEvent(anEvent('title'))

        verify(notifier).send(notification.capture())
        assertThat notification.value.title() isEqualTo 'title [2s]'
    }
}
