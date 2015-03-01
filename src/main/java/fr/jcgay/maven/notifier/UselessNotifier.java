package fr.jcgay.maven.notifier;

import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;

import java.util.List;

public enum UselessNotifier implements Notifier {
    EMPTY;

    @Override
    public boolean shouldNotify() {
        return false;
    }

    @Override
    public void init(EventSpy.Context context) {
        // do nothing
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        // do nothing
    }
}
