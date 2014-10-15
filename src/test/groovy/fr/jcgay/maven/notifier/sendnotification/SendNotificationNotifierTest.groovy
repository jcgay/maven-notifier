package fr.jcgay.maven.notifier.sendnotification
import com.github.jcgay.maven.notifier.Configuration
import com.github.jcgay.maven.notifier.ConfigurationParser
import fr.jcgay.notification.Application
import fr.jcgay.notification.Icon
import fr.jcgay.notification.Notification
import fr.jcgay.notification.Notifier
import fr.jcgay.notification.SendNotification
import groovy.transform.CompileStatic
import org.apache.maven.eventspy.EventSpy
import org.apache.maven.execution.MavenExecutionResult
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.assertj.core.api.Assertions.assertThat
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

        underTest = new SendNotificationNotifier(notifier)
        underTest.configuration = parser
    }

    @Test
    void 'should call init when initializing notifier'() {
        underTest.init(aContext())

        verify(notifier).init()
    }

    @Test
    void 'should call close when exiting notifier'() {
        underTest.close()

        verify(notifier).close()
    }

    @Test
    void 'should send notification when an event is triggered'() {
        underTest.init(aContext())
        underTest.onEvent(anEvent('title'))

        verify(notifier).send(notification.capture())
        assertThat notification.value.title() isEqualTo 'title [0s]'
    }

    @Test
    void 'should configure send-notification'() {
        def sendNotification = mock(SendNotification)
        when sendNotification.setApplication(isA(Application)) thenReturn sendNotification
        when sendNotification.addConfigurationProperties(isA(Properties)) thenReturn sendNotification

        SendNotificationNotifier.configureNotifier(sendNotification)

        verify(sendNotification).setApplication(
                Application.builder(
                        "application/x-vnd-apache.maven",
                        "Maven",
                        Icon.create(resource("maven.png"), "maven"))
                    .build())
        verify(sendNotification).addConfigurationProperties(isA(Properties))
        verify(sendNotification).chooseNotifier()
    }

    private static EventSpy.Context aContext() {
        mock EventSpy.Context
    }

    private static MavenExecutionResult anEvent(String title) {
        def result = mock(MavenExecutionResult, RETURNS_DEEP_STUBS)
        when result.project.name thenReturn title
        result
    }

    private static URL resource(String resource) {
        Thread.currentThread().getContextClassLoader().getResource(resource)
    }
}
