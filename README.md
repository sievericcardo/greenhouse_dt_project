# Greenhouse Twin Project
Repository containing other repositories related to the greenhouse digital twin project


## Table of Contents
<!-- TODO: add simulations part -->
- [Project Overview](#project-overview)
- [Project Architecture](#project-architecture)
    - [Physical Architecture](#physical-architecture)
        - [Greenhouse](#greenhouse)
        - [Sensors](#assets---sensors)
        - [Data collectors](#data-collectors)
        - [Host computer](#host-computer)
    - [Tools Overview](#tools-overview)
        - [InfluxDB](#influxdb)
        - [Python influxdb-client](#python-influxdb-client)
        - [Python sensors libraries](#python-sensors-libraries)
        - [OWL](#owl)
        - [SMOL language](#smol-language)
    - [Project Components](#project-components)
        - [Sensors](#sensors)
        - [Python sensor-data controller](#python-sensor-data-controller)
            - [Sensor controller](#sensor-controller)
            - [InfluxDB controller](#influxdb-controller)
        - [Greenhouse asset model](#greenhouse-asset-model)
        - [Twinning the greenhouse with SMOL](#twinning-the-greenhouse-with-smol)
        - [Scheduled run using Java](#scheduled-runner-using-java)
    - [Execution flow](#execution-flow)
- [Project setup](#project-setup)
    - [Raspberry Pi](#raspberry-pi-setup)
        - [Sensor connections](#raspberry-pi-sensor-connections)
        - [Software](#raspberry-pi-software-setup)
    - [InfluxDB](#influxdb-setup)
    - [Python](#python-setup)
    - [SMOL](#smol-setup)
    <!-- - [Java](#java-setup) -->
    - [Host computer](#host-computer-setup)
- [How to run](#how-to-run)
    - [Data collectors](#data-collectors-run)
    - [Host computer](#host-computer-run)
        - [InfluxDB](#influxdb-run)
        - [Java SMOL runner](#java-smol-runner-run)

<!-- For reports also: results, discussion, conclusion -->



## Project Overview
Digital twins have emerged as a promising technology that enables virtual replicas of physical assets, allowing for real-time monitoring, analysis, and simulation. These virtual replicas can be applied across various fields, including agriculture, manufacturing, healthcare, and more. In this research project, our focus is on building a digital twin for a greenhouse as an example to showcase the capabilities of this technology.

Our approach involves developing a digital twin for a greenhouse using a combination of Python programming, SMOL language, and Raspberry Pi. <br>
The Python program is designed to interact with InfluxDB, a time-series database, to collect and store sensor data, while the SMOL language is used to create a representation of the assets and a knowledge graph that captures the relationships between different components of the greenhouse. The sensors, connected to various Raspberry Pi, are set up to collect data on various environmental parameters such as temperature, humidity, light, and soil moisture, providing a rich dataset for analysis.

The primary objective of this research project is to create a functional example of a digital twin that can showcase its potential in monitoring and managing a greenhouse environment. By creating a virtual replica of the greenhouse, we can simulate and analyze its behavior, and gain insights into how different components interact with each other. This digital twin can serve as a valuable tool for optimizing greenhouse operations, improving resource utilization, and enhancing overall crop yield.

In this report, we will provide a detailed overview of the methodology used to develop the digital twin, present the results of our data analysis, discuss the implications of our findings, and highlight the potential applications of digital twins in various fields beyond agriculture. This research contributes to the growing body of knowledge on digital twins and serves as a practical example of their application in a real-world setting.

## Project Architecture

### Physical Architecture

#### Greenhouse
The specific greenhouse we are working with has the following characteristics:
- The greenhouse is divided in two shelves.
- Each shelf is composed by 2 groups of plants.
- Each group of plants is watered by a water pump.
- Each group of plants is composed by 2 plants.
- Each plant is put inside a pot.

#### Assets - Sensors
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

#### Data collectors
The data collectors are the Raspberry Pi that are collecting data from the sensors and sending them to the host computer.
They run a Python program that collects data from the sensors and sends them to the host computer.
Each data collector is connected to a greenhouse shelf and is responsible for collecting data on the assets that are 
located on that shelf (1 shelf, 4 pots, 4 plants, 2 water pumps).

#### Host computer
The host computer is the computer that is running the InfluxDB database and the SMOL program.
The host computer is responsible for storing the data collected by the data collectors and for running the SMOL program 
that is responsible for creating the digital twin of the greenhouse.

### Tools Overview

#### InfluxDB
InfluxDB is a time-series database that is used to store the data collected by the data collectors.
There is a single bucket in the database that is used to store all the data collected by the data collectors.
Measurements from different assets have their own measurement name in the database. 
Each measurement has a set of fields that represent the data collected by the sensors, related to that asset.

#### Python influxdb-client
The Python influxdb-client is a Python library that is used to interact with the InfluxDB database.
It is used by the data collectors to send data to the database.

#### Python sensors libraries

#### OWL
OWL is a knowledge representation language that is used to represent the asset model of the greenhouse.
The asset model is used to represent the assets described in [Assets - Sensors](#assets---sensors) and the relationships 
between them.

#### SMOL
SMOL is an object-oriented language that is used to connect the asset model to the data collected by the data 
collectors.
It is used to generate the semantic lifting of the program state, which is then used to create the digital twin of the
greenhouse.

