package fr.jcgay.maven.notifier

import org.codehaus.plexus.logging.Logger
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property
import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property.IMPLEMENTATION
import static org.assertj.core.api.Assertions.assertThat

class ConfigurationParserTest {

    @InjectMocks
    private ConfigurationParser parser

    @Mock
    Logger logger

    @BeforeMethod
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
        System.clearProperty(Property.NOTIFY_WITH.key())
    }

    @Test(dataProvider = 'os and notifier implementation')
    void 'should return default implementation configuration'(String os, String expectedImplementation) throws Exception {

        // Given
        Properties properties = new Properties()
        System.getProperties().entrySet().each { properties << [(it.key):(it.value)] }
        properties << ['os.name':os]

        // When
        Configuration result = parser.get(properties)

        // Then
        assertThat result.getImplementation() isEqualTo expectedImplementation
    }

    @DataProvider
    private Object[][] 'os and notifier implementation'() {
        // outdated os list: http://lopica.sourceforge.net/os.html
        return [
            ['Mac OS X', 'growl'],
            ['Windows XP', 'growl'],
            ['Linux', 'notifysend']
        ]
    }

    @Test
    void 'should return default configuration'() throws Exception {

        Configuration result = parser.get(new Properties());

        assertThat result.isShortDescription() isTrue()
        assertThat result.threshold isEqualTo(-1)
    }

    @Test
    void 'should return configuration'() {

        Properties properties = new Properties();
        properties << [(IMPLEMENTATION.key()):('test')]
        properties << [(Property.SHORT_DESCRIPTION.key()):('false')]
        properties << [(Property.THRESHOLD.key()):('10')]

        Configuration result = parser.get(properties)

        assertThat result.getImplementation() isEqualTo 'test'
        assertThat result.isShortDescription() isFalse()
        assertThat result.threshold isEqualTo(10)
    }

    @Test
    void 'should not override implementation with property when no property is set'() throws Exception {

        def result = ConfigurationParser.readProperties(this.getClass().getResource('/implementation.properties'))

        assertThat result[IMPLEMENTATION.key()] isEqualTo 'growl'
    }

    @Test
    void 'should override implementation with system property'() throws Exception {

        System.setProperty(Property.NOTIFY_WITH.key(), 'override-implementation')
        def result = ConfigurationParser.readProperties(getClass().getResource('/implementation.properties'))

        assertThat result[IMPLEMENTATION.key()] isEqualTo 'override-implementation'
    }

    @Test
    void 'should override implementation with system property when configuration file is not found'() throws Exception {

        System.setProperty(Property.NOTIFY_WITH.key(), 'override-implementation')
        def result = ConfigurationParser.readProperties(new URL('file:///non-existing.properties'))

        assertThat result[IMPLEMENTATION.key()] isEqualTo 'override-implementation'
    }

    @Test
    void 'should overwrite global configuration with user one'() {

        def result = ConfigurationParser.readProperties(
            getClass().getResource('/implementation.properties'),
            getClass().getResource('/implementation-user.properties')
        )

        assertThat(result[IMPLEMENTATION.key()]).isEqualTo('snarl')
    }

    @Test
    void 'should use configuration passed by system property'() {

        System.setProperty('notifier.anybar.port', '1111')
        System.setProperty('notifier.anybar.host', 'localhost')

        def result = ConfigurationParser.readProperties(getClass().getResource('/anybar.properties'))

        assertThat result['notifier.anybar.port'] isEqualTo '1111'
        assertThat result['notifier.anybar.host'] isEqualTo 'localhost'
    }
}
