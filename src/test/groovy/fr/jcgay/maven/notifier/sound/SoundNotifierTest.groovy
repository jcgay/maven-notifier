package fr.jcgay.maven.notifier.sound


import groovy.transform.CompileStatic
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.Fixtures.skipSendNotificationInit
import static org.assertj.core.api.Assertions.assertThat

@CompileStatic
class SoundNotifierTest {

    private SoundNotifier notifier

    @BeforeMethod
    void init() throws Exception {
        notifier = new SoundNotifier()
        notifier.init(skipSendNotificationInit())
    }

    @Test
    void 'should return true when sound is the choosen notifier'() throws Exception {
        assertThat notifier.isCandidateFor('sound') isTrue()
    }

    @Test
    void 'should return false when sound is not the choosen notifier'() throws Exception {
        assertThat notifier.isCandidateFor('growl') isFalse()
    }
}
