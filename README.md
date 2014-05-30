#Maven Notifier

Notifiers that can be used with Maven 3.x.
A status notification will be send at the end of a Maven build.

##Installation

Get [maven-notifier](http://dl.bintray.com/jcgay/maven/com/github/jcgay/maven/maven-notifier/0.8.1/maven-notifier-0.8.1.zip) and extract it in your `$M2_HOME/lib/ext` folder.

If you are using Maven > 3.1.x, you don't need to extract `slf4j-nop-x.x.x.jar`, Maven already comes with a SLF4J binding.

##What's new ?

See [CHANGELOG](https://github.com/jcgay/maven-notifier/blob/master/CHANGELOG.md) to get latest changes.

##Available notifier

###Growl

Used by default on OS X [(paid app)](http://growl.info/) and Windows [(free)](http://www.growlforwindows.com/gfw/).

Growl must listen for incoming notifications. This option is available in the network section (OS X) or in the security section of Growl (Windows).  
This implementation is also compatible with [Snarl](http://snarl.fullphat.net/) for Windows via its Growl compatibility.

![Growl success](http://jeanchristophegay.com/images/notifier.growl_.success.png)  ![Growl fail](http://jeanchristophegay.com/images/notifier.growl_.fail_.png)

###notify-send

Used by default on linux. Installation if it's not done yet: `sudo apt-get install libnotify-bin`

![notify-send success](http://jeanchristophegay.com/images/notifier.notify-send.success.png)  
![notify-send fail](http://jeanchristophegay.com/images/notifier.notify-send.error_.fail_.png)

###Notification center

Available only for OS X (at least Mountain lion).  
Need to have [terminal-notifier](https://github.com/alloy/terminal-notifier) installed: `brew install terminal-notifier` 

![terminal-notifier](http://jeanchristophegay.com/images/notifier.notification-center.success.png)  ![terminal-notifier fail](http://jeanchristophegay.com/images/notifier.notification-center.failure.png)

###Sound

Play a success or failure sound when build ends.

###System tray

Use Java `SystemTray` to display notification.

During the build an icon ![Build Icon](http://jeanchristophegay.com/images/notifier.system.tray_.building.png) is displayed in the System Tray. At the end the icon reflects the build status and a notification is sent.  
![System Tray success](http://jeanchristophegay.com/images/notifier.system.tray_.success.png)  ![System Tray fail](http://jeanchristophegay.com/images/notifier.system.tray_.fail_.png)

###Snarl

Send notificiation to [Snarl](http://snarl.fullphat.net/) for Windows.  
Snarl must listen for incoming notifications. This option is available in `Options > Network` section. 

![Snarl](http://jeanchristophegay.com/images/notifier.snarl.success.png)  ![Snarl fail](http://jeanchristophegay.com/images/notifier.snarl.failure.png)

##Configuration

If needed, configuration can be done by creating a `maven-notifier.properties` file in your `$M2_HOME/lib/ext` folder.  

- `notifier.implementation` = which implementation to use. (`growl`, `notificationcenter`, `notifysend`, `sound`, `systemtray`, `snarl`).
- `notifier.growl.port` = growl listening port (default: `23053`).
- `notifier.growl.host` = growl host (default: `localhost`).
- `notifier.growl.password` = growl target password.
- `notifier.notify-send.path` = notify-send binary path (default: `notify-send`).
- `notifier.notify-send.timeout` = the timeout in milliseconds at which to expire the notification (default: `2s`).
- `notifier.notification-center.path` = terminal-notifier binary path (default: `terminal-notifier`).
- `notifier.notification-center.activate` = Indicate which application should be activated when clicking on terminal-notifier message. See `Info.plist` file inside the application bundle to find the bundle identifier (key: `CFBundleIdentifier`). For example to open [iTerm2](http://www.iterm2.com/#/section/home), use `com.googlecode.iterm2` (default: `com.apple.Terminal`).
- `notifier.notification-center.sound` = Sound to play when the notification is fired. Use `default` to select the default sound. The possible names are listed in Sound Preferences.
- `notifier.system-tray.wait` = Java SystemTray notification display time in second. (default: `2s`)
- `notifier.message.short` = true/false (by default). Choose between a full description with each module notification or a short one just reflecting the build status.
- `notifier.snarl.host` = snarl host (default: `localhost`).
- `notifier.snarl.port` = snarl listening port (default: `9887`).
- `notifier.snarl.password` = snarl password.

Notification can be skipped by using `-DskipNotification` when launching Maven.

    mvn package -DskipNotification