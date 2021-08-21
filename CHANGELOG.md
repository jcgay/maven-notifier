# 2.1.2
***

- Fix sound not playing with SoundNotifier ([6b6b861](http://github.com/jcgay/maven-notifier/commit/6b6b8615376c94464a55360b8a9609e504b21c1d))
- Reset all state in EventSpy#init to works with mvnd ([db9c057](http://github.com/jcgay/maven-notifier/commit/db9c057417db976efb1aed30d55a526db83dbf5c))

# 2.1.1
***

- Reset build timer when closing the EventSpy ([ac1433a](http://github.com/jcgay/maven-notifier/commit/ac1433a978a29ad4a56b36ef227b0d8e58b6a018))
- Do not close notifier with mvnd ([1de061c](http://github.com/jcgay/maven-notifier/commit/1de061cb1fa8d42896eb40f61e4fd5419aece2bd))
- Build with GitHub actions ([eea0d96](http://github.com/jcgay/maven-notifier/commit/eea0d960b35e740a56e3f3f890b72254536981d4))

# 2.1.0
***

- Use send-notification 0.16.0 ([f1424e6](http://github.com/jcgay/maven-notifier/commit/f1424e6dee4ed299051f54400687b12e773cc362))
    * Disable auto-detect for BurntToast ([03952d5](http://github.com/jcgay/send-notification/commit/03952d5d8fbbd776c10b4e5ecd2d4ed94d53456d))

# 2.0.0
***

- Use send-notification 0.15.1 ([f1c6968](http://github.com/jcgay/maven-notifier/commit/f1c69685e95b927c5eaa16295d73445717a5b427))
  * Log a warning when a configured notifier is not valid ([4e227b4](http://github.com/jcgay/send-notification/commit/4e227b440cd1c87518b35a41400892580e3afcb2))
  * Do not set a default app activation for terminal-notifier ([61f142f](http://github.com/jcgay/send-notification/commit/61f142f0940542117a3494e034bb723797337faf))
  * Use jPowerShell 3.0.4 [Fixes [#10](https://github.com/jcgay/send-notification/issues/10)] ([b67f9bd](http://github.com/jcgay/send-notification/commit/b67f9bda4e8290468328ab10b97fc3c5a1b1c37b))
  * Removed no longer existing parameter for BurntNotificationToast '-AppId' [Fixes [#11](https://github.com/jcgay/send-notification/issues/11)] ([89c84e3](http://github.com/jcgay/send-notification/commit/89c84e3f0350c68163d8293c63134901df203d10))
- Set timeout when building send-notifier Application ([19f8521](http://github.com/jcgay/maven-notifier/commit/19f852161154627a82f42293e0d0df329cc641e1))
- Migrate to Java 8 ([6ca062f](http://github.com/jcgay/maven-notifier/commit/6ca062fc6018e0d67368a2fcd44900ae63f3289f))
- Log current maven-notifier version in DEBUG ([961ca93](http://github.com/jcgay/maven-notifier/commit/961ca9354c0b2c775d682370da327952c62ce907))
- Add a docker image with notify-send ([cebffa0](http://github.com/jcgay/maven-notifier/commit/cebffa03b90786e583edc6dc4a954ed83408355e))

# 1.10.1
***

- Get compatible with Guava 18 ([ac3de41](http://github.com/jcgay/maven-notifier/commit/ac3de41c0934c2398f519752caae842213a7df20))

# 1.10.0
***

- Use send-notification 0.11.0 ([b3217c4](http://github.com/jcgay/maven-notifier/commit/b3217c4542190be2303baafb0edfc067f5f36958))
  * Add Slack notifier ([613e0af](http://github.com/jcgay/send-notification/commit/613e0af8ad444b89f231a26e36e800efef8f26e2))
  * Add BurntToast notifier ([00af537](http://github.com/jcgay/send-notification/commit/00af5378207297374f8b9c42feb7ebd149a6498d))
  * Add Notify notifier ([f6c190d](http://github.com/jcgay/send-notification/commit/f6c190dddb8160996ae84372b11bd20cb1fc8e5a))

# 1.9.1
***

- Use send-notification 0.10.1 ([3e432dd](http://github.com/jcgay/maven-notifier/commit/3e432dd612db9c0dac2cdbf30afb8c2f1db8b0a4))
    * Escape argument when executing notifu ([41358dd](http://github.com/jcgay/send-notification/commit/41358ddc20125d35996ebba5545c00e2b66ff31f))

# 1.9
***

- Use send-notification 0.10-SNAPSHOT ([f387c10](http://github.com/jcgay/maven-notifier/commit/f387c10e23cec61849ea060230ee09b8e066be6b))
    * Prevent dock icon creation on OS X ([f7ba636](http://github.com/jcgay/send-notification/commit/f7ba63631fe6e1c9f2bbad126164eeca1cf2d7b5))

# 1.8
***

- Fix Windows user configuration loading ([9e4f933](http://github.com/jcgay/maven-notifier/commit/9e4f933e05c9478ca5efac2e60ddb89a5552aaa5))
- Better debug log messages ([9b10622](http://github.com/jcgay/maven-notifier/commit/9b106223427d7cd1237fe644b461a4abd5ce7e37))
- Default notifier implementation is chosen by send-notification ([a113649](http://github.com/jcgay/maven-notifier/commit/a113649dcf96e693b9fb14bc2143165572ef1097))
- Can launch multiple build with Snarl ([da0b38e](http://github.com/jcgay/maven-notifier/commit/da0b38eb7db12654003de7d0efc011a7f965ba78))

# 1.7
***

- Use notifier 'none' to not send notifications ([23eb400](http://github.com/jcgay/maven-notifier/commit/23eb40006ad8637f65693e89c31e9ed35ba91366))
- Log Growl errors in debug when used in auto discovery mode ([23eb400](http://github.com/jcgay/maven-notifier/commit/23eb40006ad8637f65693e89c31e9ed35ba91366))

# 1.6
***

- Always send notification when notifier is persistent ([d3a3d08](http://github.com/jcgay/maven-notifier/commit/d3a3d081401c862491c23aeb6108e1637bbb45de))
- Replace '...' by 'Build Failed' for short notification messages ([30310ae](http://github.com/jcgay/maven-notifier/commit/30310ae092f0d5313129ac57f8e6e41a18237e73))
- Can use multiple notifiers at once ([f8e0f90](http://github.com/jcgay/maven-notifier/commit/f8e0f90ae2d3a694a5cdeb4d6ef2fb541f3cb8e8))

# 1.5
***

- Smarter default notifier ([cab405b](http://github.com/jcgay/maven-notifier/commit/cab405b6071dee5df41168d4e0f2388f32ec9970))

# 1.4
***

- Add Toaster notifier ([97b395b](http://github.com/jcgay/maven-notifier/commit/97b395b1dd94c894e26c79ce5f8e0f3593436aac))
- Add notification center (with AppleScript) notifier ([97b395b](http://github.com/jcgay/maven-notifier/commit/97b395b1dd94c894e26c79ce5f8e0f3593436aac))
- Notification center (with terminal-notifier) now uses application icon ([97b395b](http://github.com/jcgay/maven-notifier/commit/97b395b1dd94c894e26c79ce5f8e0f3593436aac))

# 1.3
***

- Can pass configuration using system properties [view](http://github.com/jcgay/maven-notifier/commit/351c771187947ad995757e3211ffabfe25330156)
- Add AnyBar (SomeBar) notifier [view](http://github.com/jcgay/maven-notifier/commit/f1bbee89aa878739d0374341ece3600470bec521)

# 1.2
***

- Configuration location is now ~/.m2/maven-notifier.properties [view](http://github.com/jcgay/maven-notifier/commit/86ba7057adf1f148107b25f7fc2e6a6567a97e57)
- Compatibility with Maven 3.3.1 extension mechanism [view](http://github.com/jcgay/maven-notifier/commit/258d769c42e3531c0f45281c3ae6f8457595d922)

# 1.1
***

- No notification if build ends before configured threshold [Fixes #11] [view](http://github.com/jcgay/maven-notifier/commit/9b513b5)

# 1.0
***

- Use short description by default [view](http://github.com/jcgay/maven-notifier/commit/6be9df9bec4bfa043f60b5bd0df4154f22eda70c)
- Replace short description default message with '...' [view](http://github.com/jcgay/maven-notifier/commit/eb89d89359bbc867739bd2255c58b9b6db462e83)
- New notifiers: kdialog, notifu [view](http://github.com/jcgay/maven-notifier/commit/9eb59c48a29821f4f2e83b77e680dc263297cbad)
- Use send-notification v0.3 [Fixes #10] [view](http://github.com/jcgay/maven-notifier/commit/9eb59c48a29821f4f2e83b77e680dc263297cbad)
- Set notification level based on build status [view](http://github.com/jcgay/maven-notifier/commit/8690f63b7f16bd39fa7aa17689ef245563ccd22f)
- Can use -DnotifyWith when no configuration file is present [view](http://github.com/jcgay/maven-notifier/commit/eb9b1f0dbc81cd1ca2951b59db699b3b020c785f)

# 0.11
***

- Maven 3.2.5 compatibility [Fixes #9] [view](http://github.com/jcgay/maven-notifier/commit/d2c17389117167b64154859ab7bf2b80895f9b24)

# 0.10
***

- Use [send-notification](https://github.com/jcgay/send-notification)

# 0.9
***

- Can override notifier implementation with system property [view](http://github.com/jcgay/maven-notifier/commit/9096f4472d6ea939e4fab28e5fc2f8f874cbd3ea)
- Exclude SLF4J api from the Uber jar [Fixes #7] [view](http://github.com/jcgay/maven-notifier/commit/7df6bc9006eb6c1bfef62fcbd45bb1e3dab76150)
- Add Pushbullet notifier [view](http://github.com/jcgay/maven-notifier/commit/a19ea22af668af6c4feed2fd18ce804a0f942914)
- Add wiki link when Snarl notification fails [view](http://github.com/jcgay/maven-notifier/commit/b12b344482513f6a58da90fd3941595ec77e6ed4)
- Add wiki links when Growl notification fails [Fixes #6] [view](http://github.com/jcgay/maven-notifier/commit/77ba7213077d2dc7270c2ead7ad9924476e09474)
- Remove password from Configuration#toString [view](http://github.com/jcgay/maven-notifier/commit/2df057c68ea2ecbfeb82c3c6cffdcbfe713d905a)
- Remove SLF4J Gntp listener [view](http://github.com/jcgay/maven-notifier/commit/916932911a2973c5bd64d62f913738567d06301d)
- Add a Plexus Gntp listener to log Growl events [#6] [view](http://github.com/jcgay/maven-notifier/commit/fadc1eb4a793665e3b1d64052deb2d31a3123d1f)
- Send notification when build fails with error [Fixes #5] [view](http://github.com/jcgay/maven-notifier/commit/25dc1055d905ffe409a7401cdfe0a9ae6f5e2cc3)

# 0.8.1
***

- Do not fail when notification-center does not use sound [Fix #4] [view](http://github.com/jcgay/maven-notifier/commit/2e7c08adfe1edaabb1d5ef6c8f79d3779dc816d9)

# 0.8
***

- Add icon for notification center. [view](http://github.com/jcgay/maven-notifier/commit/91dcab8678b3cab6d19635a3b564a8b432b2282c)
- Short message notification when project contains only one module. [view](http://github.com/jcgay/maven-notifier/commit/d8f267df1b8ee2ba7f6d3337631c57fa8c034507)
- Can configure notification message type (short/full). [view](http://github.com/jcgay/maven-notifier/commit/f7467c5ca840b40ed9ef7c19196036de04a9117d)
- Can configure growl password. [view](http://github.com/jcgay/maven-notifier/commit/7d06186e3254b31370e986fcd83be0155998b8a6)
- Can configure growl host. [Fix #3] [view](http://github.com/jcgay/maven-notifier/commit/d168f2c080456574c72a269c8633d1e1cc3883a9)
- Can set a sound when using Apple Notification Center. [Fix #1] [view](http://github.com/jcgay/maven-notifier/commit/0a8e12a3c3d41c9d4963053a562ee0188f2210cd)
- Display total time spent building the project. [view](http://github.com/jcgay/maven-notifier/commit/639a63203d2bd07f1178348200a4bd69351ebf3f)
- Add Snarl notifier. [Fix #2] [view](http://github.com/jcgay/maven-notifier/commit/139cc7b345e11f9085b4c8637d55baf7d58442b6)
