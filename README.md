<h1>Iot for configuration driven development</h1>

<h2>Summary</h2>

This repo contains the configuration driven development for IoT systems. Goals are

- Device abstraction layer
- Device integration at runtime through configuration
- Flexible binding for many-to-many devices
- Security and Audit as primary architecture requirement
- Remote configuration, installation, and management
- Non intrusive framework for developers
- Easy configuration by users
- REST like communication architecture
- Event based automation
- Integrated anayltics and storage
- Easy clustering and inter cluster communication
- Stable error handling
- Support for light weight aspects e.g. performance timing, email errors etc.

See installation instructions below

<h2>Usage</h2>
After cloning the repo

1. Go to com.daghan.resource.application project
2. right click on debug.bndrun and select run as ->  BND Osgi Run launcher
	* WARNING: if the procejst do not build please go to Problems tab of eclipse (generally at the bottom of eclipse window) and manually delete errors by right click and delete.
	* WARNING: if the build stuck than check your network connection. It might be slow and it might take time to build. You might also get some error related to checksum of some libraries.
	* WARNING: if you still cannot build then go to the cnf project and delete folder called "local" under cnf project (this step is not verified in any way so any feedback is appreciated)
3. On your browser go to http://localhost:8080/system/console/configMgr (user:admin, Password:admin)
4. Press "+" on the right of "Reader Device Config company A" and click "Save", Note the Device Name e.g. myReader
5. Again press Press "+" on the right of Reader Device Config company A this time change the device name e.g. myReader2, click "Save"
6. Press "+" on the right of "Reader Device Config Company B" and click "Save", Note the Device Name e.g. myCompany
7. Click on the "Resource Command Config" and write the name of the first reader e.g. myReader
	* continue clicking + on the same pop up and add the next reader name, e.g. myReader2
	* continue clicking + on the same pop up and add the next reader name, e.g. myCompany
8. Go to ESGi console in eclipse and write 
get <string>

yous should see <string> is repeated three times in the console. try changing the configuratio of "Resource Command Config" from the browser. For example repeat step 4 and add the Device name. Also experiment with device configuration to see how the output is changed.

you can also delete "myReader" and run the "get <string>" later create another "myReader" and try again.


<h2>Installation</h2>

- Install Eclipse Mars -- tested with version 4.5.1
- Install BndTools plugin into eclipse Mars -- tested with version 3.0.0

<h2>Rest end points</h2>
management portal
http://localhost:8080/com.daghan.resource/index.html

after a device is created from the configuration page of the management portal.

GET POST PUT http://localhost:8080/device/[deviceId]
