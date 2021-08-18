package fr.jcgay.maven.notifier

import org.apache.maven.execution.BuildSuccess
import org.apache.maven.execution.MavenExecutionResult
import org.apache.maven.project.MavenProject

import static org.mockito.AdditionalAnswers.returnsElementsOf
import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.*

class Fixtures {

    static MavenExecutionResult aSuccessfulProject() {
        def project = aProjectWithOneModule('project')
        when project.hasExceptions() thenReturn false
        project
    }

    static MavenExecutionResult aFailingProject() {
        def project = aProjectWithOneModule('project')
        when project.hasExceptions() thenReturn true
        project
    }

    static MavenExecutionResult aProjectWithMultipleModule(String projectName) {
        def result = anEvent(projectName)
        when result.getTopologicallySortedProjects().size() thenReturn 4
        result
    }

    static MavenExecutionResult aProjectWithMultipleModule(String projectName, Project... projects) {
        def result = anEvent(projectName)
        when result.getTopologicallySortedProjects() thenReturn projects.collect { Project project -> mavenProject(project.name) }
        when result.getBuildSummary(isA(MavenProject)) thenAnswer returnsElementsOf(projects.collect { Project project -> new BuildSuccess(mavenProject(project.name), project.time) })
        result
    }

    static MavenExecutionResult aProjectWithOneModule(String projectName) {
        def result = anEvent(projectName)
        when result.getTopologicallySortedProjects().size() thenReturn 1
        result
    }

    static MavenExecutionResult anEvent(String projectName) {
        def result = mock(MavenExecutionResult, RETURNS_DEEP_STUBS)
        when result.project.name thenReturn projectName
        result
    }

    static URL resource(String resource) {
        Thread.currentThread().getContextClassLoader().getResource(resource)
    }

    static Project aModule(String name, long time) {
        def project = new Project()
        project.name = name
        project.time = time
        project
    }

    static MavenProject mavenProject(String name) {
        def project = new MavenProject()
        project.name = name
        project
    }

    static class Project {
        String name
        long time
    }
}
