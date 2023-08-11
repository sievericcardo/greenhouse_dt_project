## Table of Contents

<!-- TODO: add simulations part -->

- [Project setup](#project-setup)
  - [Data collector setup](#data-collectors-setup)
  - [Actuator setup](#actuator-setup)
  - [Host computer setup](#python-influxdb-client)
- [How to run](#how-to-run)
  - [Configuration files](#configuration-files)
  - [Data collector](#data-collector)
    - [Run demo](#run-demo)
    - [Manually run the main program](#manually-run-the-main-program)
  - [Actuator](#actuator)
    - [Manually run the Actuator](#manually-run-the-actuator)
  - [SMOL scheduler](#smol-scheduler)
    - [SMOL scheduler needed files](#smol-scheduler-needed-files)
    - [Run the SMOL scheduler](#run-the-smol-scheduler)
- [Brief descriptions](#brief-functioning-descriptions)
  - [Brief Data collector demo program functioning](#brief-data-collector-demo-program-functioning)
  - [Brief Data collector main program functioning](#brief-data-collector-main-program-functioning)
  - [Brief SMOL scheduler functioning](#brief-smol-scheduler-functioning)
    - [Self-adaptation](#self-adaptation)
    - [Brief asset model description](#brief-assets-model-description)
    - [SMOL program description](#smol-program-description)

# Project setup

Please refer to [this](physical-setup/readme.md) for the physical setup.

## Data collectors setup

For a complete guide on how to set up the controllers,
refer to the [controller setup](physical-setup/controller-instructions.md)

## Actuator setup

For a complete guide on how to set up the Actuators,
refer to the [Actuator setup](physical-setup/actuator-instructions.md)

## **Host computer setup**

- **InfluxDB**
  - You can install influxDB on your host computer by following the [official guide](https://docs.influxdata.com/influxdb/v2.7/install/?t=Linux).
  - You can run the following command:

```bash
wget https://dl.influxdata.com/influxdb/releases/influxdb2-2.7.0-arm64.deb
sudo dpkg -i influxdb2-2.7.0-arm64.deb
```

- After you have installed influxDB you will create an account and a token will be displayed.
  - Save the token, it will be needed for the configuration files later.
  - Create an organization named "**sirius_local**"
  - Create a bucket named "**greenhouse**"

- **Java**
    - Install JAVA jdk and jre using the following command:

```bash
sudo apt install openjdk-17-jdk openjdk-17-jre
```

# How to Run

## Configuration files

These are all the configuration files needed by the different components. Please prepare them before moving on to the next steps. You can use the samples provided.

The configuration files need to stay in the same folder as the **SMOL Scheduler JAR file**. How to create the JAR and run the SMOL Scheduler is explained in the [SMOL Scheduler section](#smol-scheduler) (see later).

You can create the configuration files assuming that you will have a folder in the Host computer with:
- SMOL scheduler JAR
- All configuration files
- Asset model (.ttl file)
- SMOL program (.smol file)

The configuration templates with all the other files needed (asset model and SMOL program) are available in the `smol_scheduler/src/main/resources` [folder](https://github.com/N-essuno/smol_scheduler/tree/master/src/main/resources)

- `config_local.yml`: used by the SMOL program to access to influxDB
  - **NOTE**: it is used by the SMOL program and as now it's hardcoded in the SMOL program.
  - `url`: URL of the influxDB database (e.g., http://localhost:8086)
  - `token`: The token you saved while setting influxDB
  - `org`: The name of the organization you created while setting up influxDB (**sirius_local**)
  - `bucket`: The name of the bucket you created while setting up influxDB (**greenhouse**)

<br>

- `config_scheduler.yml`: used by the SMOL scheduler to get the following information:
    - `smol_path`: Path of the SMOL program
    - `lifted_state_output_path`: Path of the lifted state output directory (no longer used)
    - `lifted_state_output_file`: Name of the lifted state output file (no longer used)
    - `greenhouse_asset_model_file`: Path of the asset model file (.ttl file)
    - `domain_prefix_uri`: Domain prefix URI (e.g., http://www.semanticweb.org/gianl/ontologies/2023/1/sirius-greenhouse#)
    - `interval_seconds`: Seconds between every execution of the SMOL program
    - `local_shelf_1_data_collector_config_path`: The path in which the shelf 1 Data collector configuration file is stored on the host computer
    - `local_shelf_2_data_collector_config_path`: The path in which the shelf 2 Data collector configuration file is stored on the host computer
    - `shelf_1_data_collector_config_path`: The path in which the shelf 1 Data collector configuration is stored in the data collector raspberry itself
    - `shelf_2_data_collector_config_path`: The path in which the shelf 2 data collector configuration is stored in the data collector raspberry itself

> The `shelf_1_data_collector_config_path` (same for shelf_2) must point to a file named `config.ini` in the `collector` folder on the Data collector raspberry (`.../collector/config.ini`)



<br>

- `config_ssh.yml`: used by the SMOL scheduler to get the following information:
    - IP address (host), username and password for
        - Testing
        - Actuator
        - Each Data collector

<br>

- `config_shelf_1.ini`: used by the Data collector on the first shelf to map sensor-data with assets and upload it in influxDB. It is structured as following:
  - `[influx2]`
    - `url`: URL of the influxDB database (e.g., http://localhost:8086)
    - `org`: organization name (the one you saved while setting up influxDB)
    - `token`: token to access the database (the one you saved while setting up influxDB)
  - `[sensor_switches]`
    - `use_infrared_sensor`: boolean value to enable/disable the infrared sensor (in case not present)
    - `use_light_sensor`: boolean value to enable/disable the light sensor (in case not present)
  - `[moisture_values]`
    - `XP`: array that can contain up to 100 values for the mapping between voltage registered by the sensor and moisture percentage
  - `[light_level_values]`
    - `XP`: array that can contain up to 100 values for the mapping to light level percentage (add more information)
  - `[shelves]`
    - `shelf_1`: JSON dictionary containing the mapping between a shelf and the GPIO pins corresponding to humidity and temperature sensors for that shelf
      - Note: if a data collector controls more than one shelf, there will be a dictionary for each shelf
  - `[pots]`
    - `pot_1`: JSON dictionary containing the mapping between a pot (position), the ADC channel corresponding to the moisture sensor for that pot and the plant id in that pot
      - Note: if a data collector controls more than one pot, there will be a dictionary for each pot
  - `[plants]`
    - `plant_1`: JSON dictionary containing the plant id of a plant on the shelf of the data collector
      - Note: if a data collector controls more than one plant, there will be a dictionary for each plant

<br>

- `config_shelf_2.ini`: same as before but for data collector in second shelf. It is needed because we assume there will be 2 shelves in the greenhouse, each one with its own data collector. <br>
However, for simplicity, we deactivated in the software the second data collector. This means that:
  - You **must** provide a configuration for the second data collector, but it will not be used by the software.
    - The second data collector won't be started, won't collect data and its configuration won't be updated based on asset model changes.



## Data collector

- Clone the [Data collector repository](https://github.com/N-essuno/greenhouse-data-collector) from GitHub

```bash
git clone https://github.com/N-essuno/greenhouse-data-collector.git
```

### Run DEMO
- If not present create a configuration file named `config.ini` into the `collector` folder.
  - The file must contain at least the `influx2` section as described in the [configuration files section](#configuration-files) for the Data collector configuration.
    - `[influx2]`
    - `url`: URL of the influxDB database (e.g., http://localhost:8086)
    - `org`: organization name (the one you saved while setting up influxDB)
    - `token`: token to access the database (the one you saved while setting up influxDB)
  - An example of the configuration file (`config.ini.example`) can be found in the project
- Run the following command from the root of the Data collector project to start the demo program:

```bash
python3 -m collector --demo
```

You can find [here](#brief-data-collector-demo-program-functioning) a brief description of the Demo program functioning


### Manually run the main program

> **NOTE:** With the actual architectural configuration the Data collectors should not be manually run. They are automatically started by the SMOL scheduler (host).
We will write here how to run it manually just for completeness of information.

> Please ignore this section if you are trying to run the system how it is supposed to be run.

- If not present create a configuration file named `config.ini` into the `collector` folder.
  - The file must contain all the information as described in the [configuration files section](#configuration-files) for the Data collector configuration.
  - An example of the configuration file (`config.ini.example`) can be found in the project
- Run the following command from the root of the Data collector project to start the main program:

```bash
python3 -m collector
```

You can find [here](#brief-data-collector-main-program-functioning) a brief description of the main program functioning


## Actuator

- Clone the [Actuator repository](https://github.com/MarcoAmato/greenhouse_actuator) from GitHub
```bash
git clone https://github.com/MarcoAmato/greenhouse_actuator.git
```

### Manually run the Actuator

> **NOTE:** With the actual architectural configuration the Actuators should not be manually run. Thery are automatically started by the SMOL scheduler (host).
We will write here how to run it manually just for completeness of information.

> Please ignore this section if you are trying to run the system how it is supposed to be run.

It takes as input the following parameters:

- `command`: the command to execute. At the moment only `water` is supported to trigger the water pump
- if `command = "water"`:
  - `GPIO_pin`: the GPIO pin activate for starting the pump
  - `seconds`: the number of seconds to keep the pump on


> Example:
>
>  `python3 -m actuator <command> <parameters>`
> ```bash
>  python3 -m actuator water 18 5
>  ```

> **Note:** the mapping between GPIO_pin and Actuator component is modeled in the asset model and based on this information the right component will always be activated. 

You can find [here](#brief-actuator-functioning) a brief description of the Actuator functioning


## SMOL scheduler


### SMOL scheduler needed files

To run the SMOL Scheduler you need to have:

- The SMOL file to be run (`src/main/resources/test_check_moisture.smol`)
- The asset model (`src/main/resources/greenhouse.ttl`)
- Configuration files as described in the [configuration files section](#configuration-files)
  - 3 configuration files for the scheduler
  - 2 configuration files used by the Data collectors (one for each)

### Run the SMOL scheduler

- Clone the [SMOL scheduler repository](https://github.com/N-essuno/smol_scheduler) from GitHub

```bash
git clone https://github.com/N-essuno/smol_scheduler.git
```

- If not done yet, setup the configuration according to your network setup as explained in the [configuration files section](#configuration-files).
- Build the project using gradle (from the root)

```bash
./gradlew demo
```
- The JAR will be gerated in the folder `smol_scheduler/build/libs/`. You can copy it to the folder of your choice (containing all the [needed files](#smol-scheduler-needed-files)) and run it from there.
- Run the SMOL scheduler JAR

```bash
java -jar <jar_name>
```

You can find [here](#brief-smol-scheduler-functioning) a brief description of the SMOL scheduler functioning


<br>

---
---

<br>


# Brief descriptions

## Brief Data collector DEMO program functioning

The demo program will create a bucket named `demo` and will populate it with:

- Pot measurements with decreasing moisture, simulating a real life scenario which triggers the Actuator to water the pot.
- Plant measurements

The pot measurements refer to a pot with

- shelf_floor = 1
- group_position = left
- pot_position = right
- plant_id = 1

The plant measurements refer to a plant with

- plant_id = 1
- group_position = left
- pot_position = right

<br>

## Brief Data collector main program functioning

The main program will:

- If not present, create a bucket named `greenhouse` in influxDB
- Read periodically sensor data and load it into the bucket
- If the configuration changes: self-adapt the Data collector to the new configuration

<br>

## Brief Actuator functioning

The Actuator script will be run from the SMOL scheduler (host) and will activate the pump to water the pot for the given number of seconds.

<br>

## Brief SMOL scheduler functioning

The smol_scheduler will periodically run a SMOL program, which analyzes the data collected by the data collectors and triggers the actuation system when needed.

When the moisture of a pot is below a certain threshold, the Actuator will be triggered and the pot will be watered.
The threshold is fixed in the asset model.

In particular it will repeat the following steps every `n` seconds (`n` is fixed in the configuration file):

- Run the SMOL program to get the plants to be watered
- Run a SPARQL query on the SMOL program state
  - The program state is a knowledge graph representing, among other information, also the state of the greenhouse
  - It will contain some triples with the predicate `PlantToWater_plantId`. The object of each of this triple is the id of a plant to be watered.
- Retrieve, using the SPARQL query, the `PlantToWater_plantId` objects from the lifted state (which are the ids of the plants to be watered)
- If there are plants to be watered it will trigger the actuation system for each of them. The trigger is done by:
  - Connecting via SSH to the Actuator controlling the pump in the greenhouse
  - Executing the command to start the pump

### Self-adaptation

This program will also run periodically a task which:

1. Checks if the asset model has been changed
2. If it has been changed then updates the configuration files used by the Data collectors
    - E.g. information about which plant is in which pot and which sensor (pin/channel) is used to measure the pot moisture
3. Sends the updated files to the Data collectors

### Brief asset model description

The asset model used is `greenhouse.ttl`. <br>
As the moment it contains more information than needed. The relevant and used information for the is the following:

Classes:

- `Plant`
  - `plantId`: the id of the plant
  - `idealMoisture`: the ideal moisture of the plant
  - Subclasses
    - `Basilicum`
- `Pot`
  - `shelfFloor`: the shelf floor in which the pot is placed (1 or 2)
  - `groupPosition`: the group on the shelf in which the pot is placed (left or right)
  - `potPosition`: the pot position with respect to the group in which the pot is placed (left or right)
  - `plantId`: the id of the plant placed in the pot
  - `moistureAdcChannel`: the ADC channel used by the sensor to measure the moisture of the pot
  - `wateredBy`: the pump used to water the pot
- `Pump`
  - `pumpGpioPin`: the GPIO pin used to activate the pump

Individuals:

- `basilicum1`
  - `plantId = 1`
  - `idealMoisture = 50`
- `basilicum2`
  - `plantId = 2`
  - `idealMoisture = 50`
- `pot1`
  - `wateredBy = pump1`
  - `shelfFloor = 1`
  - `groupPosition = left`
  - `potPosition = left`
  - `plantId = 1`
  - `moistureAdcChannel = 1`
- `pot2`
  - `wateredBy = pump1`
  - `shelfFloor = 1`
  - `groupPosition = left`
  - `potPosition = right`
  - `plantId = 2`
  - `moistureAdcChannel = 2`
- `pump1`
  - `pumpId = 1`
  - `pumpGpioPin = 18`

### SMOL program description

The SMOL program run in the demo is `test_check_moisture.smol`.

It will:

- Retrieve the plants from the asset model creating `Plant` objects with the following fields:
  - `plantId`
  - `idealMoisture`
- This first step is done using an `AssetModel` object
  - It contains a `getPlants()` method which runs a SPARQL query and returns a list of `Plant` objects structured as described above
- Retrieve from influxDB database the last `moisture` measurement of the pot in which the plant is placed
  - This is done calling the `getPotMoisture()` method of the `Plant` object. It runs a Flux query to get the last measurement.
- For each plants which has `moisture < idealMoisture`:
  - Create a `PlantToWater` object representing a plant to be watered. The object contains just the id of the plant.

> NOTE: the `PlantToWater` object is created in order to be represented in the knowledge graph once the semantical lifting of the program state is performed. <br>
> It will be used by the SMOL scheduler to trigger the actuation system.




<!--
OLD PART FOR RUNNING SMOL SCHEDULER DEMO

- Run the following commands from the root of the `smol_scheduler` project:

```bash
./gradlew demo
```

> **Note:** if `./gradlew demo` fails it's possible to proceed doing the following:
> 1. Run `./gradlew build`
> 2. Copy the jar generated from `smol_scheduler/build/libs/` to your folder of choice (e.g., `smol_scheduler/demo/`)
> 3. Proceed setting up the configuration files as described further down
> 4. Run using `java -jar <jar_name>` from the folder where the jar is located
-->



<!--
OLD INFO ABOUT PROJECT RUN

### **Run Project**

#### **Run Data Collectors**

For the reference on how to set up and run the data collectors refer to the [greenhouse-data-collector repository](https://github.com/N-essuno/greenhouse-data-collector)

#### **Run Host Computer**

##### **InfluxDB**

If you are running a linux distribution, there is no need to start influxDB manually;
it will start automatically after the installation.

##### **SMOL Scheduler**

The entry point of the project is the [SMOL scheduler](#smol-scheduler), which is responsible for running the SMOL program and sending the digital shadow to the user (eventually through an interface).
Use the following commands to build and run the SMOL scheduler:

  ```bash
  cd ./smol_runner
  ./gradlew build
  java -jar ./build/libs/smol_scheduler.jar
  ```

-->