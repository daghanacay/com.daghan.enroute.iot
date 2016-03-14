# 

Provides the bluetooth library for OSGi. it wraps the bluecove libraries. Currently it works with Linux variants. 

## Installation:

In order to use this bundle on linux systems you need to install the following packages

libbluetooth-dev on Ubuntu,Raspian. it can be installed by sudo apt-get install libbluetooth-dev 
bluez-libs-devel on Fedora
bluez-devel on openSUSE



## Example

On the osgi console write the following to find the bluetooth devices around
 
> bt:search

## Configuration

		
	
## References
building bluecove for Raspberrypi from https://www.raspberrypi.org/forums/viewtopic.php?f=29&t=89031

connect to your raspberry through ssh
$ ssh pi@hostIp

Download snapshot sources

$ mkdir bluecovelib
$ cd bluecovelib
bluecovelib$ wget http://snapshot.bluecove.org/distribution/download/2.1.1-SNAPSHOT/2.1.1-SNAPSHOT.63/bluecove-gpl-2.1.1-SNAPSHOT-sources.tar.gz
bluecovelib$ tar -zxvf bluecove-gpl-2.1.1-SNAPSHOT-sources.tar.gz

modify build xml
bluecovelib$ nano ~/bluecovelib/bluecove-gpl-2.1.0/build.xml

Delete text '-SNAPSHOT' on line 12 of build.xml:
from: <property name="product_version" value="2.1.0-SNAPSHOT"/>
to:   <property name="product_version" value="2.1.0"/>

Save file: `Ctrl+X` then `Y` and `Enter`.

Download bluecove.jar dependency 

bluecovelib$ mkdir -p bluecove/target
bluecovelib$ cd bluecove/target
bluecovelib$ wget http://snapshot.bluecove.org/distribution/download/2.1.1-SNAPSHOT/2.1.1-SNAPSHOT.63/bluecove-2.1.1-SNAPSHOT.jar

install required libraries
bluecovelib$ sudo apt-get install libbluetooth-dev 
bluecovelib$ sudo apt-get install ant

and build the library

bluecovelib$cd bluecove-gpl-2.1.0
bluecovelib/bluecove-gpl-2.1.0$ ant all

Finally copy your jar to desktop computer
scp  pi@10.1.1.9:/home/pi/bluecovelib/bluecove-gpl-2.1.0/target/bluecove-gpl-2.1.0.jar ~




