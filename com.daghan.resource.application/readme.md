# 

${Bundle-Description}

## Starting the application 

### Starting on remote raspberry pi
use the debugRemote.bndrun to run it on the remote raspberryPi. You need to start remote agent on the raspberry pi. 

Login to you raspberry pi using ssh (default password for pi is raspberry)
$ ssh pi@10.1.1.9
Obviously you have to change the IP number to the one you are using. And than run the osgi agent on the raspberry pi
$ sudo bndremote -a (See [2] below)

Go back to your eclipse and please change the -runremote configuration in debugRemote.bndrun based on your raspberryPi address e.g.

-runremote: \
	pi; \
		jdb=1044; \
		host=10.1.1.9; \
		shell=-1
 
right click on debugRemote.bndrun and select debug as -> BND native launcher

### Starting on the local machine
right click on debug.bndrun and select debug as -> Bnd OSGi run launcher. and 


find the management page at 

http://10.1.1.9:8080/com.daghan.resource/index.html#/configurations

web console at
http://10.1.1.9:8080/system/console/services

you can turn on/off an led using 
POST  http://10.1.1.9:8080/device/blueLed with body HIGH or LOW
POST  http://10.1.1.9:8080/device/redLed with body HIGH or LOW
POST  http://10.1.1.9:8080/device/greenLed with body HIGH or LOW

you can read the values on the pins 
GET http://10.1.1.9:8080/device/intensityPin
GET http://10.1.1.9:8080/device/blueLed
GET http://10.1.1.9:8080/device/redLed
GET http://10.1.1.9:8080/device/greenLed

See configuration for details

## Configuration

Led configuration is handled in configuration/configuration.json see DigitalPinConfiguration interface 

{
     "service.factoryPid":"com.company3.device.gpio.provider.GpioDeviceImpl",
     "service.pid":any unique pid e.g. blueLedPid,
     "name": any unique name e.g. blueLed,
     "pinNumber":number between 1-29 with prefix "pin" e.g. pin23 see http://pi4j.com/pins/model-b-plus.html,
     "pinType": input or output pin use one of [input,output] e.g. input,
     "pinLevel":pin level to be set on idle one of [low,high] e.g. low
  },


## References

1- Add our ssh key to raspberry so you dont need to write the password all the time
http://www.howtogeek.com/168147/add-public-ssh-key-to-remote-server-in-a-single-command/
2- Install jpm (instructions taken from http://enroute.osgi.org/tutorial_iot/100-prerequisites.html)
$ curl https://bndtools.ci.cloudbees.com/job/bnd.master/719/artifact/dist/bundles/biz.aQute.jpm.run/biz.aQute.jpm.run-3.0.0.jar>jpm.jar
$ sudo java -jar jpm.jar init
$ jpm version

Install remote debug agent
$ sudo jpm install -f biz.aQute.remote.main@*
3 - installing firewall on raspberrypi
sudo apt-get install ufw
a - disable sudo ufw disable
b- open a port
 sudo ufw default allow incoming
 sudo ufw default allow outgoing
 sudo ufw allow 1044/tcp