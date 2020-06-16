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
Also you need to install [Thingsboard](https://thingsboard.io/docs/user-guide/install/ubuntu/) (with Edge) and ThingsBoard-Edge.

# ThingsBoard-Edge installation

Install Edge as service:
```bash
wget https://github.com/thingsboard/thingsboard-edge/releases/download/v1.0/tb-edge.deb //
sudo dpkg -i tb-edge.deb
```
Then you need to create database for ThingsBoard-Edge:
```bash
sudo su - postgres
psql
CREATE DATABASE tb_edge;
```
Configure `/etc/tb-edge/conf/tb-edge.conf` file:
```bash
sudo nano /etc/tb-edge/conf/tb-edge.conf
```
Add the following lines to the configuration file. Don’t forget to replace `PUT_YOUR_POSTGRESQL_PASSWORD_HERE` with your real postgres user password:
```bash
# DB Configuration 
export DATABASE_ENTITIES_TYPE=sql
export DATABASE_TS_TYPE=sql
export SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
export SPRING_DRIVER_CLASS_NAME=org.postgresql.Driver
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tb_edge
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=PUT_YOUR_POSTGRESQL_PASSWORD_HERE
export SPRING_DATASOURCE_MAXIMUM_POOL_SIZE=5
# Specify partitioning size for timestamp key-value storage. Allowed values: DAYS, MONTHS, YEARS, INDEFINITE.
export SQL_POSTGRES_TS_KV_PARTITIONING=MONTHS
```
Run install script:
```bash
sudo /usr/share/tb-edge/bin/install/install.sh
```
After that you can start your Edge using following command:
```bash
service tb-edge start
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
Add the following lines to the configuration file. Don’t forget to replace `PUT_YOUR_SECRET_HERE` and `PUT_YOUR_ROUTING_KEY_HERE` with your real secret and routing key:
```
export CLOUD_ROUTING_SECRET=PUT_YOUR_SECRET_HERE
export CLOUD_ROUTING_KEY=PUT_YOUR_ROUTING_KEY_HERE
export MQTT_BIND_PORT=1885
export COAP_ENABLED=false
export HTTP_BIND_PORT=8090
export RPC_PORT=9002
```
And start (or restart) edge service.
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
Add the following lines to the configuration file. Don’t forget to replace `PUT_YOUR_SECRET_HERE` and `PUT_YOUR_ROUTING_KEY_HERE` with your real secret and routing key:
```
export CLOUD_ROUTING_SECRET=PUT_YOUR_SECRET_HERE
export CLOUD_ROUTING_KEY=PUT_YOUR_ROUTING_KEY_HERE
export MQTT_BIND_PORT=1885
export COAP_ENABLED=false
export HTTP_BIND_PORT=8090
export RPC_PORT=9002
```
And start (or restart) edge service.
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
