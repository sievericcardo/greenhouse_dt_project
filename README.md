# Greenhouse Twin Project
Repository containing other repositories related to the greenhouse digital twin project


## Table of Contents
<!-- TODO: add simulations part -->
- [Project Overview](#project-overview)
- [Project Architecture](#project-architecture)
    - [Physical Architecture](#physical-architecture)
        - [Greenhouse](#greenhouse)
        - [Assets - Sensors](#assets---sensors)
        - [Data collectors](#data-collectors)
        - [Host computer](#host-computer)
    - [Tools Overview](#tools-overview)
        - [InfluxDB](#influxdb)
        - [Python influxdb-client](#python-influxdb-client)
        - [Python sensors libraries](#python-sensors-libraries)
        - [OWL](#owl)
        - [SMOL language](#smol-language)
    - [Software Components](#software-components)
        - [Sensors Scripts](#sensors-scripts)
        - [Data Collectors Python program](#data-collectors-python-program)
        - [Greenhouse asset model](#greenhouse-asset-model)
        - [SMOL Twinning program](#smol-twinning-program)
        - [SMOL scheduler](#SMOL-scheduler)
    - [Execution flow](#execution-flow)
- [Project setup](#project-setup)
    - [Data Controllers](#data-collectors-setup)
    - [Host Computer](#host-computer-setup)
- [How to run](#how-to-run)

<!-- For reports also: results, discussion, conclusion -->

<br>

## Project Overview
Digital twins have emerged as a promising technology that enables virtual replicas of physical assets, allowing for real-time monitoring, analysis, and simulation. These virtual replicas can be applied across various fields, including agriculture, manufacturing, healthcare, and more. In this research project, our focus is on building a digital twin for a greenhouse as an example to showcase the capabilities of this technology.

Our approach involves developing a digital twin for a greenhouse using a combination of Python programming, SMOL language, and Raspberry Pi. <br>
The Python program is designed to interact with InfluxDB, a time-series database, to collect and store sensor data, while the SMOL language is used to create a representation of the assets and a knowledge graph that captures the relationships between different components of the greenhouse. The sensors, connected to various Raspberry Pi, are set up to collect data on various environmental parameters such as temperature, humidity, light, and soil moisture, providing a rich dataset for analysis.

The primary objective of this research project is to create a functional example of a digital twin that can showcase its potential in monitoring and managing a greenhouse environment. By creating a virtual replica of the greenhouse, we can simulate and analyze its behavior, and gain insights into how different components interact with each other. This digital twin can serve as a valuable tool for optimizing greenhouse operations, improving resource utilization, and enhancing overall crop yield.

In this report, we will provide a detailed overview of the methodology used to develop the digital twin, present the results of our data analysis, discuss the implications of our findings, and highlight the potential applications of digital twins in various fields beyond agriculture. This research contributes to the growing body of knowledge on digital twins and serves as a practical example of their application in a real-world setting.

<br>

## Project Architecture


### **Physical Architecture**

#### **Greenhouse**
The specific greenhouse we are working with has the following characteristics:
- The greenhouse is divided in two shelves.
- Each shelf is composed by 2 groups of plants.
- Each group of plants is watered by a water pump.
- Each group of plants is composed by 2 plants.
- Each plant is put inside a pot.

#### **Assets - Sensors**
Here is a list of assets we are representing for our architecture, along with the sensors we are using to collect data on them:

- Greenhouse
  - <!-- Put sensor names for each sensor --> Light
- Shelves
  - Temperature
  - Air Humidity
- Pots
  - Soil Moisture
- Plants
  - Camera (measures plant health and growth)
- Water pumps
  - Water flow

#### **Data collectors**
The data collectors are Raspberry Pi devices that collect data from the sensors and send it to the DB.
Each data collector is associated to a greenhouse shelf and is responsible for collecting data on the assets that are located on that shelf (1 shelf, 4 pots, 4 plants, 2 water pumps).

#### **Host computer**
The host computer runs
- An InfluxDB instance that holds data retrieved from the [Data Collectors](#data-collectors) 
- A [Java program](#smol-scheduler) that periodically executes the [SMOL Twinning program](#smol-twinning-program), which is responsible for creating the digital twin of the greenhouse.

The user can interact with the digital twin though the host computer.
<!-- When we know: add also responsible for simulations (modelica) -->

<br>

### **Tools Overview**

#### **InfluxDB**
[InfluxDB](https://www.influxdata.com/products/influxdb-overview/) is a time-series database that is used to store the data collected by the data collectors.<br>
It is composed by buckets which contains measurements.
Each measurement is composed by:
- measurement name
- tags
  - Used as keys by us to identify the data
- fields
  - Used by us to store the actual data from sensors
- timestamp

and represents a single data point in a specific time.<br>
This particular structure allows to get data for a specific time range and perform aggregations on it (e.g. mean) <br>

<!--
TO ADD IN ANOTHER SECTION
There is a single bucket in the database that is used to store all the data collected by the data collectors.
Measurements from different assets have their own measurement name in the database. 
Each measurement has a set of fields that represent the data collected by the sensors, related to that asset. -->

#### **Python influxdb-client**
The [Python influxdb-client](https://influxdb-client.readthedocs.io/en/latest/) is a Python library that is used to interact with the InfluxDB database.<br>
It is used by the data collectors to send data to the influxDB instance running in the host computer.

#### **Python sensors libraries**
@eduardz1


#### **OWL**
[OWL](https://www.w3.org/TR/owl-ref/) is a knowledge representation language that is used to represent the asset model of the greenhouse. In other words is used to create a formal representation of the greenhouse physical structure.<br>
The asset model is used to represent the assets described in [Assets - Sensors](#assets---sensors) and the relationships between them.

#### **SMOL Language**
[SMOL](https://smolang.org/) (Semantic Modeling Object Language) is an object-oriented language that, among others, allows to
- Interact with influxDB to read data from the database
- Read and query a knowledge graph, mapping the data read to objects in the language
- Map the whole program state to a knowledge graph by mean of the *semantic lifting*. The program state can then be queried to extract information
- Represent and run simulations (FMO) and interact with modelica <!-- TODO: add information when we get it-->

In our case it is used to connect the asset model to the data collected by the data collectors, perform the semantic lifting of the program state and interact with simulations to create the digital twin of the greenhouse.

### **Software Components**

#### **Sensors Scripts**
@eduardz1

#### **Data collectors Python program**
Python program that is run on the data collectors and is responsible for collecting data from the sensors and sending them to the influxDB instance on the host computer.

It achieves this by:
- A virtual representation of the various assets in the greenhouse is created (E.g. Plants in the greenhouse).
- Each virtual asset is connected to a set of physical sensors (E.g. Virtual plants are connected to the appropriate camera that retrieves plant health and growth).
- Virtual assets periodically collect data from the sensors, create a data point containing the asset identifiers and the sensors detection and send it to the host computer.

### **Greenhouse Asset Model**

The Greenhouse Asset Model is an OWL representation of the physical structure of the greenhouse and the relationships between the various assets in the greenhouse.

The asset model individuals are used by the SMOL program as a starting point for twinning the greenhouse.

Here follows a picture of the asset model:
<!-- TODO: add picture of asset model -->

### **SMOL Twinning program**

The SMOL program is run by the host computer and is responsible for creating the digital twin of the greenhouse.

It achieves this by:
- Reads the asset model from the OWL file
- Generates SMOL objects from the asset model individuals
- For each asset object, retrieves sensor detections for that specific asset from the influxDB database (E.g. Retrieve moisture data for a specific pot).
- After retrieving the data, the program performs the semantic lifting of the program state, creating a knowledge graph that represents the state of the assets in the greenhouse. 

### **SMOL scheduler**

The SMOL program is run periodically by the host computer to retrieve the digital shadow of the greenhouse.
A simple Java program is used to schedule the execution of the SMOL program.

## **Execution Flow**

Assuming that the host computer and the data collectors are  already running as specified in the [Project Setup](#project-setup) section, the execution flow is the following:

1. The data collectors periodically collect data from the sensors and send them to the influxDB database.
2. the SMOL Scheduler periodically runs The SMOL program.
3. The SMOL program retrieves the asset model from the OWL file and generates SMOL objects from the asset model individuals.
4. For each asset object, the SMOL program retrieves sensor detections for that specific asset from the influxDB database.
5. After retrieving the data, the SMOL program performs the semantic lifting of the program state, creating a knowledge graph that represents the state of the assets in the greenhouse.
6. The SMOL scheduler retrieves the digital shadow of the greenhouse from the SMOL program and sends it to the user.

# **Project Setup**

## **Data Collectors Setup**

- **Hardware**
  @eduardz1
- **Software**


## **Host Computer Setup**

- **InfluxDB**
- **SMOL**
- **Java**

# **How to Run**

The entry point of the project is the [SMOL scheduler](#smol-scheduler), which is responsible for running the SMOL program and sending the digital shadow to the user (eventually through an interface).
<!-- TODO add info on how to run the SMOL scheduler -->


