package fr.jcgay.maven.notifier
import groovy.transform.CompileStatic
import org.apache.maven.execution.DefaultMavenExecutionResult
import org.apache.maven.execution.MavenExecutionResult
import org.codehaus.plexus.logging.Logger
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.NotificationEventSpyChooser.SKIP_NOTIFICATION
import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

@CompileStatic
class NotificationEventSpyChooserTest {

    @InjectMocks
    private NotificationEventSpyChooser chooser

    @Mock
    private Notifier notifier
    @Mock
    private Notifier unexpectedNotifier
    @Mock
    private MavenExecutionResult anEvent
    @Mock
    private Logger logger

    @BeforeMethod
    void setUp() throws Exception {
        chooser = new NotificationEventSpyChooser()
        MockitoAnnotations.initMocks(this)

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false))

        when unexpectedNotifier.shouldNotify() thenReturn false

        when notifier.shouldNotify() thenReturn true
        chooser.availableNotifiers = [notifier]
    }

    @Test
    void 'should not notify if event is not a build result'() throws Exception {
        chooser.init({ Collections.emptyMap() })
        chooser.onEvent('this is not a build result')
        chooser.close()

        verify(notifier, never()).onEvent(any(MavenExecutionResult))
    }

    @Test
    void 'should not notify when property skipNotification is true'() throws Exception {
        System.setProperty(SKIP_NOTIFICATION, String.valueOf(true))

        chooser.init({ Collections.emptyMap() })
        chooser.onEvent(anEvent)
        chooser.close()

        verify(notifier, never()).onEvent(any(MavenExecutionResult))
    }

    @Test
    void 'should notify when property skipNotification is false'() throws Exception {
        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false))

        chooser.init({ Collections.emptyMap() })
        chooser.onEvent(anEvent)
        chooser.close()

        verify(notifier).onEvent(anEvent)
    }

    @Test
    void 'should notify failure when build fails without project'() throws Exception {
        DefaultMavenExecutionResult event = new DefaultMavenExecutionResult()
        event.project = null
        event.addException(new NullPointerException())

        chooser.init({ Collections.emptyMap() })
        chooser.onEvent(event)
        chooser.close()

        verify(notifier).onFailWithoutProject(event.getExceptions())
        verify(notifier, never()).onEvent(event)
    }

    @Test
    void 'should send notification with configured notifier'() throws Exception {
        chooser.availableNotifiers = [unexpectedNotifier, notifier]

        chooser.init({ Collections.emptyMap() })
        chooser.onEvent(anEvent)
        chooser.close()

        verify(notifier).onEvent(anEvent)
    }

    @Test
    void 'should not fail when no notifier is configured'() throws Exception {
        chooser.availableNotifiers = [unexpectedNotifier]

        chooser.init({ Collections.emptyMap() })
        chooser.onEvent(anEvent)
        chooser.close()

        verify(unexpectedNotifier, never()).onEvent(any(MavenExecutionResult))
    }

    @Test
    void 'should close notifier'() throws Exception {
        chooser.init({ Collections.emptyMap() })
        chooser.onEvent(anEvent)
        chooser.close()

        verify(notifier).close()
    }
}
