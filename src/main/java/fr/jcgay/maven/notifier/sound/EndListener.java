package fr.jcgay.maven.notifier.sound;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

class EndListener implements LineListener {

    private boolean playing = true;

    @Override
    public synchronized void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type == LineEvent.Type.STOP || type == LineEvent.Type.CLOSE) {
            playing = false;
            notifyAll();
        }
    }

    public synchronized void waitEnd() throws InterruptedException {
        while (playing) {
            wait();
        }
    }
}
