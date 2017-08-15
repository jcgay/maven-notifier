# Linux graphical docker image

This image is a debian build with [Xfce](https://xfce.org).

## Run with macOS

### Prerequesites

Install [XQuartz](https://www.xquartz.org):

    $> brew cask install xquartz
    
In the `Preferences > Security` tab, check that `Allow connections from network clients` is activated.  
In the `Preferences > Output` tab, activate `Full screen mode` to not mess your macOS and X11 display.

### Docker

    $> docker build -t maven-notifier .
    $> ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')
    $> xhost + $ip
    $> docker run -d -e DISPLAY=$ip:0 -v /tmp/.X11-unix:/tmp/.X11-unix -v /Users/jcgay/.m2:/root/.m2 -v /Users/jcgay/dev/maven-notifier/target:/usr/share/maven/lib/ext maven-notifier