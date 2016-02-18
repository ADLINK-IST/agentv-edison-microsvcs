# AgentV Microservices for the Intel Edison IoT Starter Kit

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


## Freeboard

You can use freeboard
[Freeboard.io](http://github.io/prismtech/freeboard) to very easily
create dashboards to visualise the data produced by the sensors on the
Edison board.

The Topics that you can use are described below:

#### Analog Sensors
	
	Topic: com/prismtech/node/edison/Temperature
	Topic: com/prismtech/node/edison/Luminosity
	Topic: com/prismtech/node/edison/AirQuality
	Type: com.prismtech.edison.sensor.types.AnalogSensor

#### LED 
	Topic: LED
	Type: com.prismtech.edison.led.types.LED
	
	