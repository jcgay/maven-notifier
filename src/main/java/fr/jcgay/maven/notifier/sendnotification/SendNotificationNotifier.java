package fr.jcgay.maven.notifier.sendnotification;

import com.google.common.annotations.VisibleForTesting;
import fr.jcgay.maven.notifier.AbstractCustomEventSpy;
import fr.jcgay.maven.notifier.Configuration;
import fr.jcgay.maven.notifier.ConfigurationParser;
import fr.jcgay.maven.notifier.Notifier;
import fr.jcgay.maven.notifier.Status;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.SendNotification;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static fr.jcgay.notification.Notification.Level.ERROR;
import static fr.jcgay.notification.Notification.Level.INFO;
import static fr.jcgay.notification.Notification.Level.WARNING;

@Component(role = Notifier.class, hint = "send-notification")
public class SendNotificationNotifier extends AbstractCustomEventSpy {

    private static final Icon ICON = Icon.create(resource("maven.png"), "maven");
    private static final Application MAVEN = Application.builder("application/x-vnd-apache.maven", "Maven", ICON).build();
    private static final String LINE_BREAK = System.getProperty("line.separator");

    private final fr.jcgay.notification.Notifier notifier;

    public SendNotificationNotifier() {
        notifier = configureNotifier(new SendNotification());
    }

    @VisibleForTesting
    static fr.jcgay.notification.Notifier configureNotifier(SendNotification sendNotification) {
        return sendNotification
                .setApplication(MAVEN)
                .addConfigurationProperties(ConfigurationParser.readProperties())
                .chooseNotifier();
    }

    @VisibleForTesting
    SendNotificationNotifier(fr.jcgay.notification.Notifier notifier) {
        this.notifier = notifier;
    }

    @VisibleForTesting
    fr.jcgay.notification.Notifier getNotifier() {
        return notifier;
    }

    @Override
    public void init(EventSpy.Context context) {
        super.init(context);
        notifier.init();
    }

    @Override
    public void close() {
        super.close();
        notifier.close();
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        Status status = getBuildStatus(event);
        notifier.send(
            Notification.builder(buildTitle(event), buildNotificationMessage(event), Icon.create(status.url(), status.name()))
                .withLevel(toLevel(status))
                .withSubtitle(status.message())
                .build()
        );
    }

    @Override
    public boolean shouldNotify() {
        return !"sound".equals(configuration.getImplementation());
    }

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        super.onFailWithoutProject(exceptions);
        Status status = Status.FAILURE;
        notifier.send(
            Notification.builder("Build Error", buildErrorDescription(exceptions), Icon.create(status.url(), status.name()))
                .withSubtitle(status.message())
                .withLevel(ERROR)
                .build()
        );
    }

    private static Notification.Level toLevel(Status status) {
        switch (status) {
            case SKIPPED:
                return WARNING;
            case FAILURE:
                return ERROR;
            default:
                return INFO;
        }
    }

    private static URL resource(String resource) {
        return Thread.currentThread().getContextClassLoader().getResource(resource);
    }

    protected String buildNotificationMessage(MavenExecutionResult result) {
        if (shouldBuildShortDescription(result)) {
            return buildShortDescription(result);
        }
        return buildFullDescription(result);
    }

    private String buildFullDescription(MavenExecutionResult result) {
        StringBuilder builder = new StringBuilder();
        for (MavenProject project : result.getTopologicallySortedProjects()) {
            BuildSummary buildSummary = result.getBuildSummary(project);
            Status status = Status.of(buildSummary);
            builder.append(project.getName());
            builder.append(": ");
            builder.append(status.message());
            if (status != Status.SKIPPED) {
                builder.append(" [");
                builder.append(TimeUnit.MILLISECONDS.toSeconds(buildSummary.getTime()));
                builder.append("s] ");
            }
            builder.append(LINE_BREAK);
        }
        return builder.toString();
    }

    private String buildShortDescription(MavenExecutionResult result) {
        if (getBuildStatus(result) == Status.SUCCESS) {
            return "Built in: " + elapsedTime() + " second(s).";
        }
        return "...";
    }

    private boolean shouldBuildShortDescription(MavenExecutionResult result) {
        return configuration.isShortDescription() || hasOnlyOneModule(result);
    }

    private boolean hasOnlyOneModule(MavenExecutionResult result) {
        return result.getTopologicallySortedProjects().size() == 1;
    }

    protected String buildTitle(MavenExecutionResult result) {
        if (shouldBuildShortDescription(result)) {
            return buildShortTitle(result);
        }
        return buildFullTitle(result);
    }

    private String buildFullTitle(MavenExecutionResult result) {
        return result.getProject().getName() + " [" + elapsedTime() + "s]";
    }

    private String buildShortTitle(MavenExecutionResult result) {
        return result.getProject().getName();
    }

    protected String buildErrorDescription(List<Throwable> exceptions) {
        StringBuilder builder = new StringBuilder();
        for (Throwable exception : exceptions) {
            builder.append(exception.getMessage());
            builder.append(LINE_BREAK);
        }
        return builder.toString();
    }

    @VisibleForTesting
    Configuration getConfiguration() {
        return configuration;
    }
}
