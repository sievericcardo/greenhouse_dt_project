## **Project Setup**

We assume that Raspberry Pi OS is installed on all three systems

## **Data Collectors Setup**

For a complete guide on how to set up the controllers,
refer to the [controller setup](setup-instructions/controller-instructions.md)

## **Actuator Setup**

For a complete guide on how to set up the actuators,
refer to the [actuator setup](setup-instructions/actuators-setup.md)

## **Host Computer Setup**

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

## **How to Run**

### **Run Demo**

#### Configuration files

These are all the configuration files needed by the different components. Please prepare them before moving on to the next steps. You can use the samples provided.

**SMOL Scheduler**

The configuration files need to stay in the same folder as the SMOL Scheduler JAR file.

The templates are available in the `smol_scheduler/src/main/resources` folder

- `config_local.yml`: used by the SMOL program to access to influxDB
  - **NOTE**: it is used by the SMOL program and as now it's hardcoded in the SMOL program. You could need to change it.
  - `url`: URL of the influxDB database (e.g., http://localhost:8086)
  - `token`: The token you saved while setting up influxDB
  - `org`: The name of the organization you created while setting up influxDB (**sirius_local**)
  - `bucket`: The name of the bucket you created while setting up influxDB (**greenhouse**)

<br>

- `config_scheduler.yml`: used by the SMOL scheduler to get the following information:
    - `smol_path`: Path of the SMOL program
    - `lifted_state_output_path`: Path of the lifted state output directory (no longer used)
    - `lifted_state_output_file`: Name of the lifted state output file (no longer used)
    - `greenhouse_asset_model_file`: Path of the asset model file (.ttl file)
    - `domain_prefix_uri`: Domain prefix URI
    - `interval_seconds`: Seconds between every execution of the SMOL program
    - `local_shelf_1_data_collector_config_path`: Local path of data-collectors config file for shelf 1 (used by the self-adaptation task)
    - `local_shelf_2_data_collector_config_path`: Local path of data-collectors config file for shelf 2 (used by the self-adaptation task)
    - `shelf_1_data_collector_config_path`: Remote path of data-collectors config file for shelf 1 (used by the self-adaptation task)
    - `shelf_2_data_collector_config_path`: Remote path of data-collectors config file for shelf 2 (used by the self-adaptation task)

<br>

- `config_ssh.yml`: used by the SMOL scheduler to get the following information:
    - IP address (host), username and password for
        - Testing
        - Actuator
        - Each data-collector

<br>

- `config_shelf_1`: used by the data-collector on the first shelf to map sensor-data with assets and upload it in influxDB. It is structured as following (sample):
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

- `config_shelf_2`: same as before but for data collector in second shelf. It is needed because we assume there will be 2 shelves in the greenhouse, each one with its own data collector.








#### **Data collector**

To execute a demo of the interaction between data collectors and host machine, first pull the [data-collector repository](https://github.com/N-essuno/greenhouse-data-collector) from GitHub

```bash
git clone https://github.com/N-essuno/greenhouse-data-collector.git
```

Then run the following command from the root of the data-collector project:

```bash
python3 -m collector --demo
```

The demo will create a bucket named `demo` and will populate it with:

- Pot measurements with decreasing moisture, simulating a real life scenario which triggers the actuator to water the pot.
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

#### **Actuator**

The actuator script is a python script used to physically trigger different components of the greenhouse (as the moment only the pump).

First you need to pull the [actuator repository](https://github.com/MarcoAmato/greenhouse_actuator) from GitHub
```bash
git clone https://github.com/MarcoAmato/greenhouse_actuator.git
```

**Run the script**
It takes as input the following parameters:

- `command`: the command to execute. At the moment only `water` is supported to trigger the water pump
- if `command = water`:
  - `GPIO_pin`: the GPIO pin activate for starting the pump
  - `seconds`: the number of seconds to keep the pump on

> **Note:** the mapping between GPIO_pin and actuator component is modeled in the asset model and based on this information the right component will always be activated. 

<br>

#### **SMOL scheduler**

To run a demo of the SMOL scheduler system

1. Pull the [SMOL scheduler repository](https://github.com/N-essuno/smol_scheduler) from GitHub

```bash
git clone https://github.com/N-essuno/smol_scheduler.git
```

1. Setup the configuration files in `smol_scheduler/demo/` according to your network setup
    - More information about the configuration files are available further down
2. Run the following commands from the root of the `smol_scheduler` project:

Execute:

```bash
./gradlew demo
```

> **Note:** if `./gradlew demo` fails it's possible to proceed doing the following:
> 1. Run `./gradlew build`
> 2. Copy the jar generated from `smol_scheduler/build/libs/` to your folder of choice (e.g., `smol_scheduler/demo/`)
> 3. Proceed setting up the configuration files as described further down
> 4. Run using `java -jar <jar_name>` from the folder where the jar is located

The smol_scheduler will periodically run a SMOL program, which analyzes the data collected by the data collectors and triggers the actuation system when needed.

When the moisture of a pot is below a certain threshold, the actuator will be triggered and the pot will be watered.
The threshold is fixed in the asset model.

In particular it will repeat the following steps every `n` seconds (`n` is fixed in the configuration file):

- Run the SMOL program to get the plants to be watered
- Run a SPARQL query on the SMOL program state
  - The program state is a knowledge graph representing, among other information, also the state of the greenhouse
  - It will contain some triples with the predicate `PlantToWater_plantId`. The object of each of this triple is the id of a plant to be watered.
- Retrieve, using the SPARQL query, the `PlantToWater_plantId` objects from the lifted state (which are the ids of the plants to be watered)
- If there are plants to be watered it will trigger the actuation system for each of them. The trigger is done by:
  - Connecting via SSH to the actuator controlling the pump in the greenhouse
  - Executing the command to start the pump

**Self-adaptation**

This program will also run periodically a task which:

1. Checks if the asset model has been changed
2. If it has been changed then updates the configuration files used by the data-collectors
    - E.g. information about which plant is in which pot and which sensor (pin/channel) is used to measure the pot moisture
3. Sends the updated files to the data-collectors

<br>

**SMOL scheduler needed files**

To run the SMOL Scheduler you need to provide also

- The SMOL file to be run
- The asset model (Turtle file)
- 3 configuration files for the scheduler
- 2 configuration files used by the data-collectors (one for each)
    - They will be overwritten from the SMOL schduler in case of self-adaptation

> **Note:** the data-collector configuration files are not used if running the data-collector demo version but they are needed always to run the SMOL scheduler

More information on each point further down in this document.

**SMOL program**

The SMOL program run in the demo is the `test_check_moisture.smol` file.

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

<br>

**Asset model**

The asset model used in the demo is the `greenhouse.ttl` file. <br>
As the moment it contains more information than needed. The relevant and used information for the demo is the following:

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
  - `pump`: the pump used to water the pot
- `Pump`
  - `pumpGpioPin`: the GPIO pin used to activate the pump

Individuals:

- `basilicum1`
  - `plantId = 1`
  - `idealMoisture = 50`
- `pot1`
  - `shelfFloor = 1`
  - `groupPosition = left`
  - `potPosition = left`
  - `plantId = 1`
- `pump1`
  - `pumpGpioPin = 18`

<br>



<br>

---

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

<!-- TODO add info on how to run the SMOL scheduler, implement JAR creation and instructions on how to run JAR, add info about JRE installation on host computer-->