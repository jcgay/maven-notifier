package fr.jcgay.maven.notifier;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker
import groovy.transform.CompileStatic;

@CompileStatic
class KnownElapsedTimeTicker extends Ticker {

    private long expectedElapsedTime
    private boolean firstRead

    KnownElapsedTimeTicker(long expectedElapsedTime) {
        this.expectedElapsedTime = expectedElapsedTime
    }

    static Stopwatch aStopWatchWithElapsedTime(long elapsedTime) {
        aStartedStopwatchWithElapsedTime(elapsedTime).stop()
    }

    static Stopwatch aStartedStopwatchWithElapsedTime(long elapsedTimeNano) {
        Stopwatch.createStarted(new KnownElapsedTimeTicker(elapsedTimeNano))
    }

    @Override
    long read() {
        firstRead = !firstRead;
        firstRead ? 0 : expectedElapsedTime
    }
}
