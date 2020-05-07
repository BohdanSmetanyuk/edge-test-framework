# Edge test framework

This application installs solution on ThingsBoard cloud and emulates telemetry on user devices.
Each solution consists of Edges, Assets, Devices and relations between them.
You can choose available solution, or write your own custom one.
Every solution supports three main actions:
* installation - installing different entities and relations between them on TB cloud;
* emulation - pushing data from emulator and comparing telemetry on cloud and on edge; 
* uninstallation - uninstalling solution when there is no need of it.

# Preparing your envinroment

Edge-test-framework is running on Java 8. Follow this instructions to install OpenJDK 8:
```bash
sudo apt update
sudo apt install openjdk-8-jdk
```
You can check the installation using the following command:
```bash
java -version
```
Also you need to install [Subversion](http://subversion.apache.org) to get folder with configurations, data and executable .jar files.
```bash
sudo apt install subversion
```
# Configure your application

Before launching the application you should set up environment variables depending on framework's task.

Here is an explanatory note on some of them:
* `action` can have three value - `install`, `emulate` or `uninstall`;
* `target` ;
* `solution.name` - this value is the same as the bean's name of solution class;
* `telemetry.send.protocol` - you can push telemetry using MQTT and HTTP protocol. Value will be according to choosen protocol - `mqtt` or `http`;
* `emulation.time` - time of emulating process in milliseconds.

You can find all possible configurations in `configuration` folder. If you need more information about configuration, you can find it in `description` field in every .conf file. 

# Run application

Get folder with latest release
```bash
svn export https://github.com/BohdanSmetanyuk/edge-test-framework/trunk/release/release-1.0
```
Move inside that directory
```bash
cd release-1.0
```
Choose configuration and execute command
```bash
java -jar edge-test-framework.jar --configuration=configuration/$FILE_NAME.conf
```
