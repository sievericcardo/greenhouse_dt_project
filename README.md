# Greenhouse Twin Project
Repository containing other repositories related to the greenhouse digital twin project


## Table of Contents
<!-- TODO: add simulations part -->
- [Project Overview](#project-overview)
- [Project Architecture](#project-architecture)
    - [Physical Architecture](#physical-architecture)
        - [Greenhouse](#greenhouse)
        - [Sensors](#sensors)
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


