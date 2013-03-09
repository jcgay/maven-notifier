#Maven Notifier

Just notifiers for Maven 3.x.
A status will be send at the end of a Maven build.

##Installation

Get [maven-growl-notifier](http://repository-jcgay.forge.cloudbees.com/release/com/github/jcgay/maven/maven-growl-notifier/0.4/maven-growl-notifier-0.4.zip) and extract it in your %M2_HOME%/lib/ext folder.

##Growl

Used by default on Mac OS and Windows.

Growl must listen for incoming notifications. The option is available in the network section (Mac OS) or the security section of Growl (Windows).

You cannot setup anything for now, the GNTP client is connecting to Growl on localhost:23053 without password.

##notify-send

Used by default on linux.