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

        assertThat result.isShortDescription() isFalse()
    }

    @Test
    void 'should return configuration'() {

        Properties properties = new Properties();
        properties << [(IMPLEMENTATION.key()):('test')]
        properties << [(Property.SHORT_DESCRIPTION.key()):('true')]

        Configuration result = parser.get(properties)

        assertThat result.getImplementation() isEqualTo 'test'
        assertThat result.isShortDescription() isTrue()
    }

    @Test
    void 'should not override implementation with property when its null'() throws Exception {

        def result = ConfigurationParser.readProperties(this.getClass().getResource('/implementation.properties'))

        assertThat result[IMPLEMENTATION.key()] isEqualTo 'growl'
    }

    @Test
    void 'should override implementation with property'() throws Exception {

        System.setProperty(Property.NOTIFY_WITH.key(), 'override-implementation')
        def result = ConfigurationParser.readProperties(this.getClass().getResource('/implementation.properties'))

        assertThat result[IMPLEMENTATION.key()] isEqualTo 'override-implementation'
    }
}
