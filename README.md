# Maven Notifier

Notifiers that can be used with Maven 3.x.
A status notification will be send at the end of a Maven build.

## Installation

`$M2_HOME` refers to maven installation folder.

```
.
├── bin
├── boot
├── conf
└── lib
``` 

### OS X ?

You can install a pre-packaged maven named [maven-deluxe](https://github.com/jcgay/homebrew-jcgay#maven-deluxe) using `brew`.  
It comes with [maven-color](https://github.com/jcgay/maven-color), [maven-notifier](https://github.com/jcgay/maven-notifier) and [maven-profiler](https://github.com/jcgay/maven-profiler).  
It is based on latest maven release.

    brew tap jcgay/jcgay
    brew install maven-deluxe

### Maven >= 3.3.x

Get [maven-notifier](http://dl.bintray.com/jcgay/maven/fr/jcgay/maven/maven-notifier/2.1.0/maven-notifier-2.1.0.jar) and copy it in `%M2_HOME%/lib/ext` folder.

*or*

Use the new [core extensions configuration mechanism](http://takari.io/2015/03/19/core-extensions.html) by creating a `${maven.multiModuleProjectDirectory}/.mvn/extensions.xml` file with:

```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<extensions>
	    <extension>
	      <groupId>fr.jcgay.maven</groupId>
	      <artifactId>maven-notifier</artifactId>
	      <version>2.1.0</version>
	    </extension>
	</extensions>
```

### Maven >= 3.1

Get [maven-notifier](http://dl.bintray.com/jcgay/maven/fr/jcgay/maven/maven-notifier/2.1.0/maven-notifier-2.1.0.jar) and copy it in your `$M2_HOME/lib/ext` folder.

### Maven < 3.1

Get [maven-notifier](http://dl.bintray.com/jcgay/maven/fr/jcgay/maven/maven-notifier/2.1.0/maven-notifier-2.1.0.zip) and extract it in your `$M2_HOME/lib/ext` folder.

## What's new ?

See [CHANGELOG](https://github.com/jcgay/maven-notifier/blob/master/CHANGELOG.md) to get latest changes.

## Notifiers

| Notifier | Screenshot |
|:--------:|-----------------|
| **Growl**, for [Windows](http://www.growlforwindows.com/gfw/) and [OS X](http://growl.info/).    | ![Growl](http://jeanchristophegay.com/images/notifier.growl_.success.png) |
| **[Snarl](http://snarl.fullphat.net/)**, for Windows | ![Snarl](http://jeanchristophegay.com/images/notifier.snarl.success.png) |
| **[terminal-notifier](https://github.com/alloy/terminal-notifier)**, OS X | ![terminal-notifier](http://jeanchristophegay.com/images/notifier.notification-center.success.png) |
| **notification center** OS X (since Mavericks) | ![notification-center](http://jeanchristophegay.com/images/notifier.simplenc.thumbnail.png) |
| **notify-send** for Linux | ![notify-send](http://jeanchristophegay.com/images/notifier.notify-send.success.png) |
| **SystemTray** since Java 6 | ![System Tray](http://jeanchristophegay.com/images/notifier.system.tray_.success.png) |
| **[Pushbullet](https://www.pushbullet.com/)** | ![pushbullet](http://jeanchristophegay.com/images/notifier.pushbullet.success.png) |
| **Kdialog** for KDE | ![Kdialog](http://jeanchristophegay.com/images/notifier.kdialog.fail.png) |
| **[notifu](http://www.paralint.com/projects/notifu/index.html)** for Windows | ![notifu](http://jeanchristophegay.com/images/notifier.notifu.success.png) |
| **AnyBar** for [OS X](https://github.com/tonsky/AnyBar) and [Linux](https://github.com/limpbrains/somebar) | ![anybar](http://jeanchristophegay.com/images/notifier.anybar_maven.png) |
| **[Toaster](https://github.com/nels-o/toaster)** for Windows 8 | ![Toaster](http://jeanchristophegay.com/images/notifier.toaster.success.png) |
| **[Notify](https://github.com/dorkbox/Notify)** since Java 6 | ![Notify](http://jeanchristophegay.com/images/notifier.notify.png) |
| **[BurntToast](https://github.com/Windos/BurntToast)** for Windows 10 | ![BurntToast](http://jeanchristophegay.com/images/notifier.burnttoast.png) |
| **[Slack](https://slack.com)** | ![Slack](http://jeanchristophegay.com/images/slack-success.png) |

### Sound

Play a success or failure sound when build ends.

## Configuration

Go to [Wiki](https://github.com/jcgay/maven-notifier/wiki) to read full configuration guide for each notifier.

# Build status
[![Build Status](https://travis-ci.org/jcgay/maven-notifier.svg?branch=master)](https://travis-ci.org/jcgay/maven-notifier)
[![Coverage Status](https://coveralls.io/repos/jcgay/maven-notifier/badge.svg?branch=master)](https://coveralls.io/r/jcgay/maven-notifier?branch=master)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=fr.jcgay.maven%3Amaven-notifier&metric=alert_status)](https://sonarcloud.io/dashboard?id=fr.jcgay.maven%3Amaven-notifier)
[![Technical debt ratio](https://sonarcloud.io/api/project_badges/measure?project=fr.jcgay.maven%3Amaven-notifier&metric=sqale_index)](https://sonarcloud.io/dashboard?id=fr.jcgay.maven%3Amaven-notifier)

# Release

    mvn -B release:prepare release:perform
