package fr.jcgay.maven.notifier

import groovy.transform.CompileStatic;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenExecutionResult;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static fr.jcgay.maven.notifier.NotificationEventSpyChooser.SKIP_NOTIFICATION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@CompileStatic
class NotificationEventSpyChooserTest {

    @InjectMocks
    private NotificationEventSpyChooser chooser = new NotificationEventSpyChooser()

    @Mock
    private Notifier notifier

    @BeforeMethod
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void 'should not notify if event is not a build result'() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false))

        chooser.onEvent('this is not a build result')

        verifyZeroInteractions(notifier)
    }

    @Test
    void 'should not notify when property skipNotification is true'() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(true))

        chooser.onEvent(mock(MavenExecutionResult.class))

        verifyZeroInteractions(notifier)
    }

    @Test
    void 'should notify when property skipNotification is false'() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false))
        MavenExecutionResult event = mock(MavenExecutionResult.class)

        chooser.onEvent(event)

        verify(notifier).onEvent(event)
    }

    @Test
    void 'should notify failure when build fails without project'() throws Exception {

        System.setProperty(SKIP_NOTIFICATION, String.valueOf(false))
        DefaultMavenExecutionResult event = new DefaultMavenExecutionResult()
        event.project = null
        event.addException(new NullPointerException())

        chooser.onEvent(event)

        verify(notifier).onFailWithoutProject(event.getExceptions())
        verify(notifier, never()).onEvent(event)
    }
}
