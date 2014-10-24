package fr.jcgay.maven.notifier;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;

public class KnownElapsedTimeTicker extends Ticker {

    private long expectedElapsedTime;
    private boolean firstRead;

    public KnownElapsedTimeTicker(long expectedElapsedTime) {
        this.expectedElapsedTime = expectedElapsedTime;
    }

    public static Stopwatch aStopWatchWithElapsedTime(long elapsedTime) {
        return aStartedStopwatchWithElapsedTime(elapsedTime).stop();
    }

    public static Stopwatch aStartedStopwatchWithElapsedTime(long elapsedTimeNano) {
        return new Stopwatch(new KnownElapsedTimeTicker(elapsedTimeNano)).start();
    }

    @Override
    public long read() {
        firstRead = !firstRead;
        return firstRead ? 0 : expectedElapsedTime;
    }
}
