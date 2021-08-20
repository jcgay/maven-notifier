package fr.jcgay.maven.notifier;

import com.google.common.base.Stopwatch;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public abstract class AbstractNotifier implements Notifier {

    protected Logger logger;
    protected Configuration configuration;

    private Stopwatch stopwatch = Stopwatch.createUnstarted();

    protected abstract void fireNotification(MavenExecutionResult event);

    @Override
    public final void init(EventSpy.Context context) {
        stopwatch.start();
        configuration = (Configuration) context.getData().get("notifier.configuration");
        if (!context.getData().containsKey("notifier.skip.init")) {
            initNotifier();
        }
    }

    @Override
    public final void onEvent(MavenExecutionResult event) {
        stopwatch.stop();
        if (stopwatch.elapsed(SECONDS) > configuration.getThreshold() || isPersistent()) {
            fireNotification(event);
        } else {
            logger.debug("No notification sent because build ends before threshold: " + configuration.getThreshold() + "s.");
        }
    }

    @Override
    public final void close() {
        stopwatch.reset();
        closeNotifier();
    }

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        // do nothing
    }

    @Override
    public boolean isCandidateFor(String desiredImplementation) {
        return getClass().getName().contains(desiredImplementation);
    }

    @Requirement
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setStopwatch(Stopwatch stopwatch) {
        this.stopwatch = stopwatch;
    }

    protected void initNotifier() {
        // do nothing
    }

    protected void closeNotifier() {
        // do nothing
    }

    protected Status getBuildStatus(MavenExecutionResult result) {
        return result.hasExceptions() ? Status.FAILURE : Status.SUCCESS;
    }

    protected long elapsedTime() {
        return stopwatch.elapsed(SECONDS);
    }

    protected boolean isPersistent() {
        return false;
    }
}
