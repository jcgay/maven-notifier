#Maven Notifier

Notifiers that can be used with Maven 3.x.
A status notification will be send at the end of a Maven build.

##Installation

### Maven >= 3.1

Get [maven-notifier](http://dl.bintray.com/jcgay/maven/fr/jcgay/maven/maven-notifier/1.1/maven-notifier-1.1.jar) and copy it in your `$M2_HOME/lib/ext` folder.

### Maven < 3.1

Get [maven-notifier](http://dl.bintray.com/jcgay/maven/fr/jcgay/maven/maven-notifier/1.1/maven-notifier-1.1.zip) and extract it in your `$M2_HOME/lib/ext` folder.

##What's new ?

See [CHANGELOG](https://github.com/jcgay/maven-notifier/blob/master/CHANGELOG.md) to get latest changes.

##Notifiers

###Growl

Used by default on OS X [(paid app)](http://growl.info/) and Windows [(free)](http://www.growlforwindows.com/gfw/).

![Growl success](http://jeanchristophegay.com/images/notifier.growl_.success.png)  ![Growl fail](http://jeanchristophegay.com/images/notifier.growl_.fail_.png)

###notify-send

Used by default on linux. 

![notify-send success](http://jeanchristophegay.com/images/notifier.notify-send.success.png)  
![notify-send fail](http://jeanchristophegay.com/images/notifier.notify-send.error_.fail_.png)

###Notification center

Available only for OS X (at least Mountain lion).

![terminal-notifier](http://jeanchristophegay.com/images/notifier.notification-center.success.png)  ![terminal-notifier fail](http://jeanchristophegay.com/images/notifier.notification-center.failure.png)

###Sound

Play a success or failure sound when build ends.

###System tray

Use Java `SystemTray` to display notification.

![System Tray success](http://jeanchristophegay.com/images/notifier.system.tray_.success.png)  ![System Tray fail](http://jeanchristophegay.com/images/notifier.system.tray_.fail_.png)

###Snarl

Send notificiation to [Snarl](http://snarl.fullphat.net/) for Windows.

![Snarl](http://jeanchristophegay.com/images/notifier.snarl.success.png)  ![Snarl fail](http://jeanchristophegay.com/images/notifier.snarl.failure.png)

###Pushbullet

Use [Pushbullet](https://www.pushbullet.com/) online service.

![pushbullet success](http://jeanchristophegay.com/images/notifier.pushbullet.success.png)
![pushbullet failure](http://jeanchristophegay.com/images/notifier.pushbullet.failure.png)

## Kdialog

For KDE.

![Kdialog](http://jeanchristophegay.com/images/notifier.kdialog.success.png)
![Kdialog](http://jeanchristophegay.com/images/notifier.kdialog.fail.png)

## notifu

For Windows [notifu](http://www.paralint.com/projects/notifu/index.html).

![notifu](http://jeanchristophegay.com/images/notifier.notifu.success.png)
![notifu](http://jeanchristophegay.com/images/notifier.notifu.fail.png)

##Configuration

Go to [Wiki](https://github.com/jcgay/maven-notifier/wiki) to read full configuration guide for each notifier.

# Build status
[![Build Status](https://travis-ci.org/jcgay/maven-notifier.svg?branch=dev)](https://travis-ci.org/jcgay/maven-notifier)
[![Coverage Status](https://coveralls.io/repos/jcgay/maven-notifier/badge.svg?branch=master)](https://coveralls.io/r/jcgay/maven-notifier?branch=master)
