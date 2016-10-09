# AgentV Microservices for the Intel Edison IoT Starter Kit

[![Build Status](https://travis-ci.org/PrismTech/agentv-edison-microsvcs.svg?branch=master)](https://travis-ci.org/PrismTech/agentv-edison-microsvcs)

This repository contains a series of microservices that can be used to
absract behind topics the sensors provided as part of the Grove B+ kit. 

## Grove Sensors
The sensor currently supported are listed below:

- [Air Quality Sensor](https://software.intel.com/en-us/iot/hardware/sensors/grove-air-quality-sensor)

- [Ultrasonic Ranger](https://software.intel.com/en-us/iot/hardware/sensors/grove-ultrasonic-ranger)

- [3-Axis Digital Accelerometer](https://software.intel.com/en-us/iot/hardware/sensors/mma7660-3-axis-digital-accelerometer)

- [Touch Sensor](https://software.intel.com/en-us/iot/hardware/sensors/ttp223-touch-sensor)

- [Grove Button](https://software.intel.com/en-us/iot/hardware/sensors/grove-button)

- [Light Sensor](https://software.intel.com/en-us/iot/hardware/sensors/grove-light-sensor)

- [Temperature Sensor](https://software.intel.com/en-us/iot/hardware/sensors/grove-temperature-sensor)

- [Grove LED](https://software.intel.com/en-us/iot/hardware/sensors/grove-led)

- [Color LCD](https://software.intel.com/en-us/iot/hardware/sensors/jhd1313m1-display)

- [Servo](https://software.intel.com/en-us/iot/hardware/sensors/es08a-servo)

## Edison Microservices
A microservice controls a specific kind of sensor, display, or digital I/O. 

#### Analog Sensors
Analog sensors are connected to the edison analog imputs, those on the side of the board -- precisely on the same side of the USB ports and the power port. 

The microservices that control analog sensors take two parameters, the first is the number associated to the analog port (A0, A1, ..., A3), depending on yuor set-up it could be 0, 1, 2, or 3. The second parameter is the period with which the sensor will be sampled.

#### LED 
Led are controlled via GIOP digital input/output. The digital I/O are marked on the grove mezanine with D2, D3, ... D8. When starting a microservice controlling a LED or some other enity connected to a digital I/O you simply need to provide the integer representing the I/O port, such as 2,3, ..., or 8.


## Freeboard

You can use freeboard
[Freeboard.io](http://github.io/prismtech/freeboard) to very easily
create dashboards to visualise the data produced by the sensors on the
Edison board.

The Topics that you can use are described below:

#### Analog Sensors

	Topic: com.prismtech.node.edison/Temperature
	Topic: com.prismtech.node.edison/Luminosity
	Topic: com.prismtech.node.edison/AirQuality
	Type: com.prismtech.edison.sensor.types.AnalogSensor

#### LED 

	Topic: com.prismtech.node.edison/LED
	Type: com.prismtech.edison.led.types.LED
	
	