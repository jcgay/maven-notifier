#Maven Notifier

Notifiers that can be used with Maven 3.x.
A status notification will be send at the end of a Maven build.

##Installation

Get [maven-notifier](http://dl.bintray.com/jcgay/maven/com/github/jcgay/maven/maven-notifier/0.6/maven-notifier-0.6.zip) and extract it in your `$M2_HOME/lib/ext` folder.

##Available notifier

###Growl

Used by default on OS X [(paid app)](http://growl.info/) and Windows [(free)](http://www.growlforwindows.com/gfw/).

Growl must listen for incoming notifications and do not require password. The options are available in the network section (OS X) or the security section of Growl (Windows).  
This implementation is also be compatible with [Snarl](http://snarl.fullphat.net/) for Windows via its Growl compatibility.

![Growl success](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.growl_.success.png) ![Growl fail](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.growl_.fail_.png)

###notify-send

Used by default on linux. Installation if it's not done yet: `sudo apt-get install libnotify-bin`

![notify-send success](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.notify-send.success.png)  
![notify-send fail](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.notify-send.error_.fail_.png)

###Notification center

Available only on OS X (at least Moutain lion).  
Need to have [terminal-notifier](https://github.com/alloy/terminal-notifier) installed: `brew install terminal-notifier` 

![terminal-notifier](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.terminal-notifier.success.png) ![terminal-notifier fail](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.terminal-notifier.fail_.png)

###Sound

Play a success or failure sound when build ends.

###System tray

Use Java `SystemTray` to display notification.

During the build an icon ![Build Icon](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.system.tray_.building.png) is displayed in the System Tray. At the end the icon reflects the build status and a notification is sent.  
![System Tray success](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.system.tray_.success.png) ![System Tray fail](http://jeanchristophegay.com/wp-content/uploads/2013/10/notifier.system.tray_.fail_.png)

##Configuration

If needed, configuration can be done by creating a `maven-notifier.properties` file in your `$M2_HOME/lib/ext` folder.  

- `notifier.implementation` = which implementation to use. (`growl`, `notificationcenter`, `notifysend`, `sound`, `systemtray`)
- `notifier.growl.port` = growl listening port
- `notifier.notify-send.path` = notify-send binary path
- `notifier.notify-send.timeout` = the timeout in milliseconds at which to expire the notification
- `notifier.notification-center.path` = terminal-notifier binary path.
- `notifier.notification-center.activate` = Indicate which application should be activated when clicking on terminal-notifier message. See `Info.plist` file inside the application bundle to find the bundle identifier.
- `notifier.system-tray.wait` = Java SystemTray notification display time in second.

Notification can be skip when using `-DskipNotification` when launching Maven. Ex: `mvn package -DskipNotification`