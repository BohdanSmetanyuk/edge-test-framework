# Edge test framework

This application installs solution on ThingsBoard cloud and emulates telemetry on user devices.
Each solution consists of Edges, Assets, Devices and relations between them.
You can choose available solution, or write your own custom one.
Every solution supports three main actions:
* installation - installing different entities and relations between them on TB cloud;
* emulation - pushing data from emulator and comparing telemetry on cloud and on edge. There are two variants of emulation mode - pushing messages in the period of time and push amount of messages as soon as possible; 
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
Also you need to install [Thingsboard](https://thingsboard.io/docs/user-guide/install/ubuntu/) and ThingsBoard-Edge:
```bash
wget https://github.com/thingsboard/thingsboard-edge/releases/download/v1.0/tb-edge.deb
sudo dpkg -i tb-edge.deb
sudo nano /etc/tb-edge/conf/tb-edge.conf
sudo service tb-edge start
```

# Configure your application

Before launching the application you should set up params in configuration files depending on framework's task.

There are two `.conf` files in `configuration/` directory:
* `hosts.conf` - URLs, usernames and passwords;
* `action.conf` - params of action;

Here is an explanatory note on some of params:
* `action` can have three value - `install`, `emulate` or `uninstall`;
* `target` - target of action. Can have two value - `cloud`, or `edge`;
* `solution.name` - this value is the same as the bean's name of solution class. Enable two solutions: `cloud-solution` and `edge-solution`;
* `telemetry.send.protocol` - you can push telemetry using MQTT and HTTP protocol. Value will be according to choosen protocol - `mqtt` or `http`;
* `emulation.time` - time of emulating process in milliseconds. First variant of emulation mode;
* `emulation.messages.amount` - how many messages emulator should send. Second variant of emulation mode;
* `attempts` - attempts for telemetry comparison. Default value - `1`;
* `delay` - time in milliseconds between attempts. Default value - `0`;

# Run application

Get folder with latest release
```bash
wget https://github.com/BohdanSmetanyuk/edge-test-framework/raw/master/release/release-1.0/release-1.0.zip
unzip release-1.0.zip
```
Move inside that directory
```bash
cd release-1.0
```
Edit `configuration/hosts.conf` with your params
```bash
nano configuration/hosts.conf
```
Install Cloud solution. Cloud solution installs one Edge entity on your cloud.
```bash
java -jar edge-test-framework.jar --general=configuration/hosts.conf --additional=configuration/action.conf
``` 



Edit ThingsBoard-Edge configuration file:
```bash
sudo nano /usr/share/tb-edge/conf/tb-edge.conf
```
Replace params in lines `CLOUD_URL`, `SECRET` and `ROUTING_KEY` with your values to connect Edge to ThingsBoard Cloud. And start (or restart) edge service.
```bash
sudo service tb-edge start (restart)
```



After that you can start working with that Edge and execute another tests by configuring `configuration/action.conf`. You can find hits and configuration examples in that file.
```bash
nano configuration/action.conf
```

# Three action test

You can quickly test all three actions: install, emulate (in both sides) and uninstall, following this instruction:

In folder `three-action-configuration` you can find some `.conf` files:
* `general.conf`:
```
cloud.host.name=http://localhost:8080
cloud.user.username=tenant@thingsboard.org
cloud.user.password=tenant
edge.host.name=http://localhost:8090
edge.user.username=tenant@thingsboard.org
edge.user.password=tenant
solution.name=cloud-solution
target=cloud
``` 
* `install.conf`:
```
action=install
```
* `uninstall.conf`:
```
action=uninstall
```
* `emulate-*.conf` files:
```
action=emulate

# you can change target here
target=edge

telemetry.send.protocol=mqtt

# choose emulation mode
#emulation.time=10000
emulation.messages.amount=1000

# target mqtt port
mqtt.port=1885

#optional params. by default it's 1 attempt and 0 milliseconds delay
attempts=4
delay=5000
```

Test steps:
* Set up configuration files:
```bash
nano three-action-configuration/$FILE_NAME.conf
```
* Run installation:
```bash
java -jar edge-test-framework.jar --general=three-action-configuration/general.conf --additional=three-action-configuration/install.conf
``` 
* Set up and run Edge service (If it is cloud solution):



Edit ThingsBoard-Edge configuration file:
```bash
sudo nano /usr/share/tb-edge/conf/tb-edge.conf
```
Replace params in lines `CLOUD_URL`, `SECRET` and `ROUTING_KEY` with your values to connect Edge to ThingsBoard Cloud. And start (or restart) edge service.
```bash
sudo service tb-edge start (restart)
```



* Run emulation in one way:
```bash
java -jar edge-test-framework.jar --general=three-action-configuration/general.conf --additional=three-action-configuration/emulate-one-way.conf
``` 
* Run emulation in another way:
```bash
java -jar edge-test-framework.jar --general=three-action-configuration/general.conf --additional=three-action-configuration/emulate-another-way.conf
``` 
* Stop Edge service (If it is cloud solution):
```bash
sudo service tb-edge stop
```
* Run uninstallation:
```bash
java -jar edge-test-framework.jar --general=three-action-configuration/general.conf --additional=three-action-configuration/uninstall.conf
``` 
