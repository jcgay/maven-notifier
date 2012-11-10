package com.github.jcgay.maven.notifier;

import com.github.jcgay.maven.notifier.growl.GrowlEventSpy;
import com.github.jcgay.maven.notifier.notifysend.NotifySendEventSpy;
import org.apache.maven.eventspy.EventSpy;

public class EventSpyChooser implements EventSpy {

    private EventSpy spy;

    public EventSpyChooser() {
        String os = System.getProperty("os.name").toLowerCase();
        if (isWindows(os) || isMacos(os)) {
            spy = new GrowlEventSpy();
        } else {
            spy = new NotifySendEventSpy();
        }
    }

    private boolean isMacos(String os) {
        return os.indexOf("mac") != -1;
    }

    private boolean isWindows(String os) {
        return os.indexOf("win") != -1;
    }

    @Override
    public void init(Context context) throws Exception {
        spy.init(context);
    }

    @Override
    public void onEvent(Object event) throws Exception {
        spy.onEvent(event);
    }

    @Override
    public void close() throws Exception {
        spy.close();
    }
}
