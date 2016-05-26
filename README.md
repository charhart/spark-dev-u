# spark-dev-u
Spark - Regenstrief Developer University

## Background
This repo is meant to work with a VirtualBox instance installed with Spark for Developer University at Regenstrief. 
This gives you some basic tools to run and test Spark.

## Setup
If you want to setup on your own, or don't have internal access, here's what I did. 
Some of these are derived from Zecevic, P., & Bonaci, M. (2016). Spark in Action. Manning Pubns., so buy their book!


1. Setup a new VirtualBox image with Ubuntu. (This link)[http://www.wikihow.com/Install-Ubuntu-on-VirtualBox] might be helpful if you're new to VirtualBox.
  1. Give yourself some decent memory, if you can spare it. I gave mine about 8GB.
  2. Create user named 'spark' (Your home directory (~) will be /home/spark), create a password
2. Install java, I used openjdk-8
  1. From a terminal, `sudo apt-get install openjdk-8-jdk`
3. Install the latest (Spark)[http://spark.apache.org/downloads.html]
  1. Pick the latest Spark release
  2. Pick the latest pre-built Hadoop version
  3. Pick mirror and download
4. Untar the Spark download
  1. `tar xvf spark-[version-etc-etc].tgz`
5. Make a Spark home
  1. `mkdir -p ~/bin/spark-home`
6. Move Spark to its home
  1. `mv spark-[version-etc-etc] ~/bin/spark-home`
7. Create a symbolic link for spark (this is if you want to run multiple versions of Spark, then you can just update the link to whatever version)
  1. `cd ~/bin`
  2. `ln -a spark-home/spark-[version-etc-etc] spark`
8. Install git and whatever other stuff you might want (e.g. your favorite text editor)
  1. `sudo apt-get install git`
  2. `sudo apt-get install vim`
9. Create a Spark Shell desktop icon, this should open straight up to the Spark Shell
  1. Open up your favorite text editor in the ~/Desktop directory
  2. Create new file called spark-shell.desktop
  3. Put the following in the file (the icon is optional)
  ```
  [Desktop Entry]
  Name=Spark Shell
  Exec=/home/spark/bin/spark/bin/spark-shell
  Terminal=true
  Type=Application
  Icon=/usr/share/app-install/icons/spark_logo.png
  ```
  4. You might also have to give it permissions
    1. Right Click icon, Properties > Permissions
    2. Check 'Allow executing file as program'
10. Update log4j, so it doesn't spit out a ton of messages
  1. `cd ~/bin/spark`
  2. `vim conf/log4j.properties` (or use whatever editor your like)
  3. Put the following in the file
  ```
  log4j.rootCategory=INFO, console, file
  # console config (restrict only to ERROR and FATAL)
  log4j.appender.console=org.apache.log4j.ConsoleAppender
  log4j.appender.console.target=System.err
  log4j.appender.console.threshold=ERROR
  log4j.appender.console.layout=org.apache.log4j.PatternLayout
  log4j.appender.console.layout.ConversionPattern=%d{yy/MM/dd HH:mm:ss} %p %c{1}: %m%n
  # file config
  log4j.appender.file=org.apache.log4j.RollingFileAppender
  log4j.appender.file.File=logs/info.log
  log4j.appender.file.MaxFileSize=5MB
  log4j.appender.file.MaxBackupIndex=10
  log4j.appender.file.layout=org.apache.log4j.PatternLayout
  log4j.appender.file.layout.ConversionPattern=%d{yy/MM/dd HH:mm:ss} %p %c{1}: %m%n
  # Settings to quiet third party logs that are too verbose
  log4j.logger.org.eclipse.jetty=WARN
  log4j.logger.org.eclipse.jetty.util.component.AbstractLifeCycle=ERROR
  log4j.logger.org.apache.spark.repl.SparkIMain$exprTyper=INFO
  log4j.logger.org.apache.spark.repl.SparkILoop$SparkILoopInterpreter=INFO
  ```
11. Check out this repo, under your home directory, e.g. '/home/spark/spark-dev-u'


At this point, you should be ready to start executing some Spark commands. See exercises, for some examples.

### If you want to run PySpark
  1. [Download](https://www.python.org/downloads/release/python-2711/) the latest tarball (I stuck with 2.7.x)
  2. Untar it, `tar xvf Python-2.7.[version-etc-etc].tgz`
  3. Install what you need to compile and configure Python
    1. `sudo apt-get install build-essential checkinstall`
    2. `sudo apt-get install libreadline-gplv2-dev libncursesw5-dev libssl-dev libsqlite3-dev tk-dev libgdbm-dev libc6-dev libbz2-dev`
  4. Go the directory where you untarred Python, `cd Python-2.7.[version-etc-etc]/`
  5. Execute configure
    1. `.configure`
  6. Make and check install
    1. `sudo make install`
    2. `sudo checkinstall`
  7. Execute `python` which should open a Python shell
  8. Create a PySpark desktop icon, this should open straight up to PySpark
    1. Open up your favorite text editor in the ~/Desktop directory
    2. Create new file called spark-shell.desktop
    3. Put the following in the file (the icon is optional)
    ```
    [Desktop Entry]
    Name=PySpark
    Exec=/home/spark/bin/spark/bin/pyspark
    Terminal=true
    Type=Application
    Icon=/usr/share/app-install/icons/spark_logo.png
    ```
