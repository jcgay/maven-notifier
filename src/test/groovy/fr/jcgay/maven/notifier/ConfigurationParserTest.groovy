package fr.jcgay.maven.notifier

import org.codehaus.plexus.logging.Logger
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import static fr.jcgay.maven.notifier.ConfigurationParser.ConfigurationProperties.Property
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

        assertThat result.getNotifySendPath() isEqualTo Property.NOTIFY_SEND_PATH.defaultValue()
        assertThat Long.valueOf(result.getNotifySendTimeout()) isEqualTo Long.valueOf(Property.NOTIFY_SEND_TIMEOUT.defaultValue())
        assertThat result.getNotificationCenterPath() isEqualTo Property.NOTIFICATION_CENTER_PATH.defaultValue()
        assertThat result.getNotificationCenterActivate() isEqualTo Property.NOTIFICATION_CENTER_ACTIVATE.defaultValue()
        assertThat Integer.valueOf(result.getGrowlPort()) isEqualTo Integer.valueOf(Property.GROWL_PORT.defaultValue())
        assertThat Long.valueOf(result.getSystemTrayWaitBeforeEnd()) isEqualTo Long.valueOf(Property.SYSTEM_TRAY_WAIT.defaultValue())
        assertThat Integer.valueOf(result.getSnarlPort()) isEqualTo Integer.valueOf(Property.SNARL_PORT.defaultValue())
        assertThat result.getSnarlHost() isEqualTo Property.SNARL_HOST.defaultValue()
        assertThat result.isShortDescription() isFalse()
    }

    @Test
    void 'should return configuration'() {

        Properties properties = new Properties();
        properties << [(Property.IMPLEMENTATION.key()):('test')]
        properties << [(Property.NOTIFY_SEND_PATH.key()):('notify-send.path')]
        properties << [(Property.NOTIFY_SEND_TIMEOUT.key()):('1')]
        properties << [(Property.NOTIFICATION_CENTER_PATH.key()):('notification-center.path')]
        properties << [(Property.NOTIFICATION_CENTER_ACTIVATE.key()):('notification-center.activate')]
        properties << [(Property.NOTIFICATION_CENTER_SOUND.key()):('notification-center.sound')]
        properties << [(Property.GROWL_PORT.key()):('1')]
        properties << [(Property.GROWL_HOST.key()):('192.168.0.1')]
        properties << [(Property.GROWL_PASSWORD.key()):('a.password')]
        properties << [(Property.SYSTEM_TRAY_WAIT.key()):('1')]
        properties << [(Property.SNARL_PORT.key()):('1')]
        properties << [(Property.SNARL_HOST.key()):('192.168.1.11')]
        properties << [(Property.SNARL_PASSWORD.key()):('snarl.password')]
        properties << [(Property.SHORT_DESCRIPTION.key()):('true')]
        properties << [(Property.PUSHBULLET_API_KEY.key()):('api.key')]
        properties << [(Property.PUSHBULLET_DEVICE.key()):('device')]

        Configuration result = parser.get(properties)

        assertThat result.getImplementation() isEqualTo 'test'
        assertThat result.getNotifySendPath() isEqualTo 'notify-send.path'
        assertThat result.getNotifySendTimeout() isEqualTo 1
        assertThat result.getNotificationCenterPath() isEqualTo 'notification-center.path'
        assertThat result.getNotificationCenterActivate() isEqualTo 'notification-center.activate'
        assertThat result.getNotificationCenterSound() isEqualTo 'notification-center.sound'
        assertThat result.getGrowlPort() isEqualTo 1
        assertThat result.getGrowlHost() isEqualTo '192.168.0.1'
        assertThat result.getGrowlPassword() isEqualTo 'a.password'
        assertThat result.getSystemTrayWaitBeforeEnd() isEqualTo 1
        assertThat result.getSnarlPort() isEqualTo 1
        assertThat result.getSnarlHost() isEqualTo '192.168.1.11'
        assertThat result.getSnarlPassword() isEqualTo 'snarl.password'
        assertThat result.getPushbulletKey() isEqualTo 'api.key'
        assertThat result.getPushbulletDevice() isEqualTo 'device'
        assertThat result.isShortDescription() isTrue()
    }
}
