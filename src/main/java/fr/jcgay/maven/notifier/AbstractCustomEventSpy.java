package fr.jcgay.maven.notifier;

import com.google.common.base.Stopwatch;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCustomEventSpy implements Notifier {

    static final String OVERRIDE_NOTIFIER_IMPLEMENTATION = "notifyWith";
    protected Logger logger;
    protected Configuration configuration;
    protected Stopwatch stopwatch = new Stopwatch();

    @Override
    public void init(EventSpy.Context context) {
        stopwatch.start();
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        stopwatch.stop();
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        // do nothing
    }

    @Override
    public boolean shouldNotify() {
        String notifierOverride = System.getProperty(OVERRIDE_NOTIFIER_IMPLEMENTATION);
        if (notifierOverride != null && !notifierOverride.equals("")) {
            return getClass().getName().contains(notifierOverride);
        }

        if (getClass().getName().contains(configuration.getImplementation())) {
            return true;
        }
        return false;
    }

    @Requirement
    public void setConfiguration(ConfigurationParser configuration) {
        this.configuration = configuration.get();
    }

    @Requirement
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setStopwatch(Stopwatch stopwatch) {
        this.stopwatch = stopwatch;
    }

    protected Status getBuildStatus(MavenExecutionResult result) {
        return result.hasExceptions() ? Status.FAILURE : Status.SUCCESS;
    }

    protected long elapsedTime() {
        return stopwatch.elapsedTime(TimeUnit.SECONDS);
    }
}
