# Edge test framework

This application installs solution on ThingsBoard cloud and emulates telemetry on user devices.
Each solution consists of Edges, Assets, Devices and relations between them.
You can choose available solution, or write your own custom one.
Every solution supports three main actions:
* installation - installing different entities and relations between them on TB cloud;
* emulation - pushing data from emulator and comparing telemetry on cloud and on edge; 
* uninstallation - uninstalling solution when there is no need of it.

# Preparing your envinroment

Edge test framework uses dependencies of org.thingsboard.common.data and org.thingsboard.rest-client 
under version 2.5.1-SNAPSHOT.
You can find these versions here https://github.com/BohdanSmetanyuk/thingsboard and install them to your local repository using [Maven](https://maven.apache.org/):

```bash
mvn clean install -DskipTests
```
# Configure your application

Before launching the application you should set up environment variables depending on framework's task.

Here is an explanatory note on some of them:
* `ACTION` can have three value - `install`, `emulate` or `uninstall`;
* `SOLUTION_NAME` - this value is the same as the bean's name of solution class;
* `TELEMETRY_SEND_PROTOCOL` - you can push telemetry using MQTT and HTTP protocol. Value will be according to choosen protocol - `mqtt` or `http`;
* `EMULATION_TIME` - time of emulating process in milliseconds.

# Configuration examples
## installation

* ACTION=install
* SOLUTION_NAME=edge-test-solution
* CLOUD_HOST_NAME=://localhost:8080
* CLOUD_USER_USERNAME=tenant@thingsboard.org
* CLOUD_USER_PASSWORD=tenant

## emulation

#### http

* ACTION=emulate
* SOLUTION_NAME=edge-test-solution
* CLOUD_HOST_NAME=://localhost:8080
* CLOUD_USER_USERNAME=tenant@thingsboard.org
* CLOUD_USER_PASSWORD=tenant
* EDGE_HOST_NAME=://localhost:8090
* EDGE_USER_USERNAME=tenant@thingsboard.org
* EDGE_USER_PASSWORD=tenant
* TELEMETRY_SEND_PROTOCOL=http
* EMULATION_TIME=10000

#### mqtt 

* ACTION=emulate;
* SOLUTION_NAME=edge-test-solution
* CLOUD_HOST_NAME=://localhost:8080
* CLOUD_USER_USERNAME=tenant@thingsboard.org
* CLOUD_USER_PASSWORD=tenant
* EDGE_HOST_NAME=://localhost:8090
* EDGE_USER_USERNAME=tenant@thingsboard.org
* EDGE_USER_PASSWORD=tenant
* TELEMETRY_SEND_PROTOCOL=mqtt
* MQTT_PORT=1883
* EMULATION_TIME=10000

## uninstallation

* ACTION=uninstall
* SOLUTION_NAME=edge-test-solution
* CLOUD_HOST_NAME=://localhost:8080
* CLOUD_USER_USERNAME=tenant@thingsboard.org
* CLOUD_USER_PASSWORD=tenant

