package com.github.jcgay.maven.notifier;

import com.google.code.jgntp.*;
import com.google.common.io.Closeables;
import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class GrowlEventSpy extends AbstractEventSpy {

    private GntpApplicationInfo application;
    private GntpNotificationInfo notification;
    private GntpClient client;

    @Override
    public void init(Context context) throws Exception {
        initGrowlApplication();
        initBuildStatusGrowlNotification();
        initGrowlClient();
        super.init(context);
    }

    @Override
    public void onEvent(Object event) throws Exception {

        if (isEventExecutionResult(event) && isClientRegistered()) {

            MavenExecutionResult resultEvent = (MavenExecutionResult) event;
            Map<String, BuildSummary> summaries = buildResultSummaries(resultEvent);
            BuildSummary mainSummary = summaries.get(resultEvent.getProject().getId());

            if (isSuccess(mainSummary) && isMultiModuleBuild(summaries)) {
                sendMessage(mainSummary, buildMultiModuleMessage(summaries));
            } else if (isSuccess(mainSummary)) {
                sendMessage(mainSummary, String.format("Success in %d seconds !", getExecutionTime(mainSummary)));
            } else if (isFailure(mainSummary)) {
                sendMessage(mainSummary, "Failed: " + getFailureMessage(mainSummary));
            }
        }

        super.onEvent(event);
    }

    @Override
    public void close() throws Exception {
        if (isClientRegistered()) {
            TimeUnit.SECONDS.sleep(1); // Seems that the client can be shutdown without having processed all the notifications...
            client.shutdown(5, TimeUnit.SECONDS);
        }
        super.close();
    }

    private void initGrowlClient() {
        client = Gntp.client(application)
                .listener(new Slf4jGntpListener())
                .onPort(23053)
                .build();
        client.register();
    }

    private void initBuildStatusGrowlNotification() {
        notification = Gntp.notificationInfo(application, "build-status-notification")
                .displayName("Build result status")
                .build();
    }

    private void initGrowlApplication() {
        application = Gntp.appInfo("Maven").build();
    }


    private boolean isMultiModuleBuild(Map<String, BuildSummary> summaries) {
        return summaries.size() > 1;
    }

    private String getFailureMessage(BuildSummary mainSummary) {
        return StringUtils.abbreviate(getExceptionCauseMessage(mainSummary), 75);
    }

    private long getExecutionTime(BuildSummary mainSummary) {
        return TimeUnit.MILLISECONDS.toSeconds(mainSummary.getTime());
    }

    private String getExceptionCauseMessage(BuildSummary mainSummary) {
        return ((BuildFailure) mainSummary).getCause().getMessage();
    }

    private Map<String, BuildSummary> buildResultSummaries(MavenExecutionResult result) {

        Map<String, BuildSummary> summaries = new LinkedHashMap<String, BuildSummary>(result.getTopologicallySortedProjects().size());

        for (MavenProject project : result.getTopologicallySortedProjects()) {
            BuildSummary summary = result.getBuildSummary(project);
            if (summary != null) {
                summaries.put(project.getId(), summary);
            }
        }

        return summaries;
    }

    private String buildMultiModuleMessage(Map<String, BuildSummary> summaries) {
        StringBuilder builder = new StringBuilder();
        for (Entry<String, BuildSummary> summary : summaries.entrySet()) {
            builder.append(summary.getValue().getProject().getName());
            builder.append(": Success in ");
            builder.append(TimeUnit.MILLISECONDS.toSeconds(summary.getValue().getTime()));
            builder.append(" seconds.");
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    private void sendMessage(BuildSummary summary, String message) throws InterruptedException {
        GntpNotification success = Gntp.notification(notification, summary.getProject().getName()).text(message).icon(getIcon(summary)).build();
        client.notify(success, 5, TimeUnit.SECONDS);
    }

    private RenderedImage getIcon(BuildSummary summary) {
        String icon;
        if (isSuccess(summary)) {
            icon = "/dialog-clean.png";
        } else {
            icon = "/dialog-error-5.png";
        }

        InputStream is = getClass().getResourceAsStream(icon);
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Closeables.closeQuietly(is);
        }
    }

    private boolean isFailure(BuildSummary summary) {
        return summary instanceof BuildFailure;
    }

    private boolean isSuccess(BuildSummary summary) {
        return summary instanceof BuildSuccess;
    }

    private boolean isEventExecutionResult(Object event) {
        return event instanceof MavenExecutionResult;
    }

    private boolean isClientRegistered() {
        return client != null && client.isRegistered();
    }
}