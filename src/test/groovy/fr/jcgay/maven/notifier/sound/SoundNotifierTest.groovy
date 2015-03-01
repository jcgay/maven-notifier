package fr.jcgay.maven.notifier.sound
import fr.jcgay.maven.notifier.Configuration
import fr.jcgay.maven.notifier.ConfigurationParser
import groovy.transform.CompileStatic
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@CompileStatic
class SoundNotifierTest {

    private SoundNotifier notifier

    private Configuration configuration

    @BeforeMethod
    void init() throws Exception {
        notifier = new SoundNotifier()

        configuration = new Configuration()

        def parser = mock ConfigurationParser
        when parser.get() thenReturn configuration
        notifier.configuration = parser
    }

    @Test
    void 'should return true when sound is the choosen notifier'() throws Exception {
        configuration.implementation = 'sound'

        assertThat notifier.shouldNotify() isTrue()
    }

    @Test
    void 'should return false when sound is not the choosen notifier'() throws Exception {
        configuration.implementation = 'growl'

        assertThat notifier.shouldNotify() isFalse()
    }
}
