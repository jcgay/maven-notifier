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

import static fr.jcgay.maven.notifier.Fixtures.*
import static fr.jcgay.maven.notifier.KnownElapsedTimeTicker.aStartedStopwatchWithElapsedTime
import static fr.jcgay.notification.Notification.Level.ERROR
import static fr.jcgay.notification.Notification.Level.INFO
import static java.util.concurrent.TimeUnit.SECONDS
import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.*
import static org.mockito.MockitoAnnotations.initMocks

@CompileStatic
class SendNotificationNotifierTest {

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

        underTest = new SendNotificationNotifier(notifier, false)
        underTest.configuration = parser
        underTest.logger = mock(Logger)
    }

    @Test
    void 'should call close when exiting notifier'() {
        underTest.close()

        verify(notifier).close()
    }

    @Test
    void 'should send notification when an event is triggered'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(2L))

        underTest.onEvent(anEvent('title'))

        verify(notifier).send(notification.capture())
        assertThat notification.value.title() isEqualTo 'title [2s]'
    }

    @Test
    void 'should send notification for a multi module project'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(3L))

        underTest.onEvent(aProjectWithMultipleModule('project-multimodule',
                aModule('module-1', SECONDS.toMillis(1L)), aModule('module-2', SECONDS.toMillis(2L))))

        verify(notifier).send(notification.capture())
        assertThat notification.value.title() isEqualTo 'project-multimodule [3s]'
        assertThat notification.value.message() isEqualTo String.format('module-1: Success [1s] %nmodule-2: Success [2s] %n')
    }

    @Test
    void 'should send notification with short message when project has only one module'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(1L))

        underTest.onEvent(aProjectWithOneModule('title'))

        verify(notifier).send(notification.capture())
        assertThat notification.value.title() isEqualTo 'title'
        assertThat notification.value.message() isEqualTo 'Built in: 1 second(s).'
    }

    @Test
    void 'should send notification with short message when project has only one module and fails'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(1L))

        underTest.onEvent(aFailingProject())

        verify(notifier).send(notification.capture())
        assertThat notification.value.title() isEqualTo 'project'
        assertThat notification.value.message() isEqualTo 'Build Failed.'
    }

    @Test
    void 'should send notification with short message when configuration is set to be short'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(1L))
        underTest.configuration.shortDescription = true

        underTest.onEvent(aProjectWithMultipleModule('title'))

        verify(notifier).send(notification.capture())
        assertThat notification.value.title() isEqualTo 'title'
        assertThat notification.value.message() isEqualTo 'Built in: 1 second(s).'
    }

    @Test
    void 'should use notifier when implementation is not sound'() {
        configuration.setImplementation("growl")

        assertThat underTest.shouldNotify() isTrue()
    }

    @Test
    void 'should not use notifier when implementation is sound'() {
        configuration.setImplementation("sound")

        assertThat underTest.shouldNotify() isFalse()
    }

    @Test
    void 'should send a notification with level ERROR when build is failing'() throws Exception {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(1L))

        underTest.onEvent(aFailingProject())

        verify(notifier).send(notification.capture())
        assertThat notification.value.level() isEqualTo ERROR
    }

    @Test
    void 'should send a notification with level INFO when build is successful'() throws Exception {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(1L))

        underTest.onEvent(aSuccessfulProject())

        verify(notifier).send(notification.capture())
        assertThat notification.value.level() isEqualTo INFO
    }

    @Test
    void 'should send a notification with level ERROR when build is misconfigured'() throws Exception {
        underTest.onFailWithoutProject([new Throwable("error")])

        verify(notifier).send(notification.capture())

        def result = notification.value
        assertThat result.level() isEqualTo ERROR
        assertThat result.message() contains 'error'
        assertThat result.title() isEqualTo 'Build Error'
    }

    @Test
    void 'should always send notification when notifier is persistent even if threshold is passed'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(1L))
        configuration.setThreshold(10)
        when(notifier.isPersistent()).thenReturn(true)

        underTest.onEvent(aSuccessfulProject())

        verify(notifier).send(any(Notification.class))
    }

    @Test
    void 'should not send notification when notifier is not persistent and threshold is passed'() {
        underTest.stopwatch = aStartedStopwatchWithElapsedTime(SECONDS.toNanos(1L))
        configuration.setThreshold(10)
        when(notifier.isPersistent()).thenReturn(false)

        underTest.onEvent(aSuccessfulProject())

        verify(notifier, never()).send(any(Notification.class))
    }
}
