#Maven Notifier

Just notifiers for Maven 3.x.
A status will be send at the end of a Maven build.

##Installation

Get [maven-notifier](http://repository-jcgay.forge.cloudbees.com/release/com/github/jcgay/maven/maven-notifier/0.5/maven-notifier-0.5.zip) and extract it in your `%M2_HOME%/lib/ext` folder.

##Available notifier

###Growl

Used by default on Mac OS X and Windows.

Growl must listen for incoming notifications. The option is available in the network section (Mac OS X) or the security section of Growl (Windows).

###notify-send

Used by default on linux.

###Notification center

Available only on Mac OS X (Moutain lion).  
Need to have [terminal-notifier](https://github.com/alloy/terminal-notifier) installed.

###Sound

Play a success or failure sound when build ends.

##Configuration

If needed, configuration can be done by creating a `maven-notifier.properties` file in your `%M2_HOME%/lib/ext` folder.  

- `notifier.implementation` : which implementation to use. (`growl`, `notificationcenter`, `notifysend`, `sound`)
- `notifier.growl.port` : growl listening port
- `notifier.notify-send.path` = notify-send binary path
- `notifier.notify-send.timeout` = the timeout in milliseconds at which to expire the notification
- `notifier.notification-center.path` : terminal-notifier binary path.