package fr.jcgay.maven.notifier.sendnotification
import fr.jcgay.maven.notifier.Configuration
import fr.jcgay.maven.notifier.ConfigurationParser
import fr.jcgay.notification.*
import groovy.transform.CompileStatic
import org.apache.maven.execution.BuildSuccess
import org.apache.maven.execution.MavenExecutionResult
import org.apache.maven.project.MavenProject
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.KnownElapsedTimeTicker.aStartedStopwatchWithElapsedTime
import static fr.jcgay.notification.Notification.Level.ERROR
import static fr.jcgay.notification.Notification.Level.INFO
import static java.util.concurrent.TimeUnit.SECONDS
import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.AdditionalAnswers.returnsElementsOf
import static org.mockito.Matchers.isA
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
        verify(sendNotification).initNotifier()
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

    private static MavenExecutionResult aSuccessfulProject() {
        def project = aProjectWithOneModule('project')
        when project.hasExceptions() thenReturn false
        project
    }

    private static MavenExecutionResult aFailingProject() {
        def project = aProjectWithOneModule('project')
        when project.hasExceptions() thenReturn true
        project
    }

    private static MavenExecutionResult aProjectWithMultipleModule(String projectName) {
        def result = anEvent(projectName)
        when result.getTopologicallySortedProjects().size() thenReturn 4
        result
    }

    private static MavenExecutionResult aProjectWithMultipleModule(String projectName, Project... projects) {
        def result = anEvent(projectName)
        when result.getTopologicallySortedProjects() thenReturn projects.collect { Project project -> mavenProject(project.name) }
        when result.getBuildSummary(isA(MavenProject)) thenAnswer returnsElementsOf(projects.collect { Project project -> new BuildSuccess(mavenProject(project.name), project.time) })
        result
    }

    private static MavenExecutionResult aProjectWithOneModule(String projectName) {
        def result = anEvent(projectName)
        when result.getTopologicallySortedProjects().size() thenReturn 1
        result
    }

    private static MavenExecutionResult anEvent(String projectName) {
        def result = mock(MavenExecutionResult, RETURNS_DEEP_STUBS)
        when result.project.name thenReturn projectName
        result
    }

    private static URL resource(String resource) {
        Thread.currentThread().getContextClassLoader().getResource(resource)
    }

    private static Project aModule(String name, long time) {
        def project = new Project()
        project.name = name
        project.time = time
        project
    }

    private static MavenProject mavenProject(String name) {
        def project = new MavenProject()
        project.name = name
        project
    }

    private static class Project {
        String name
        long time
    }
}
