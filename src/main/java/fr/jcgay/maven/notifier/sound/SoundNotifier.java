package fr.jcgay.maven.notifier.sound;

import fr.jcgay.maven.notifier.AbstractCustomEventSpy;
import fr.jcgay.maven.notifier.Notifier;
import fr.jcgay.maven.notifier.Status;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component(role = Notifier.class, hint = "sound")
public class SoundNotifier extends AbstractCustomEventSpy {

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        playSound(Status.FAILURE);
    }

    @Override
    protected void fireNotification(MavenExecutionResult event) {
        playSound(getBuildStatus(event));
    }

    private void playSound(Status status) {
        AudioInputStream ais = getAudioStream(status);
        if (ais == null) {
            logger.warn("Cannot get a sound to play. Skipping notification...");
            return;
        }
        play(ais);
    }

    private void play(AudioInputStream ais) {
        try {
            Clip clip = AudioSystem.getClip();
            EndListener listener = new EndListener();
            clip.addLineListener(listener);
            clip.open(ais);
            playAndWait(clip, listener);
        } catch (LineUnavailableException | IOException e) {
            fail(e);
        } finally {
            IOUtil.close(ais);
        }
    }

    private void playAndWait(Clip clip, EndListener listener) {
        try {
            clip.start();
            listener.waitEnd();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            clip.close();
        }
    }

    private void fail(Exception e) {
        logger.debug("Error playing sound.", e);
    }

    private AudioInputStream getAudioStream(Status success) {
        try {
            return AudioSystem.getAudioInputStream(getUrl(success));
        } catch (UnsupportedAudioFileException | IOException e) {
            return noAudioStream(e);
        }
    }

    private AudioInputStream noAudioStream(Exception e) {
        logger.warn("Error reading audio stream.", e);
        return null;
    }

    private InputStream getUrl(Status status) {
        String sound = status == Status.SUCCESS ? "/109662__grunz__success.wav" : "/Sad_Trombone-Joe_Lamb-665429450.wav";
        return getClass().getResourceAsStream(sound);
    }
}
