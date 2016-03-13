package com.daghan.protocol.bluetooth.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.intel.bluetooth.BluetoothConsts;

import osgi.enroute.debug.api.Debug;

/**
 * Porvides some commands related to bluetooth
 * 
 * @author daghan
 *
 */

@Component(service = ProtocolCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=bt",
		Debug.COMMAND_FUNCTION + "=search", Debug.COMMAND_FUNCTION + "=write"})
public class ProtocolCommand {

	public static final Vector devicesDiscovered = new Vector();
	public static final Vector serviceFound = new Vector();

	/**
	 * provides a command for searching bluetooth enabled devices
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void search() throws IOException, InterruptedException {

		final Object inquiryCompletedEvent = new Object();
		final Object serviceSearchCompletedEvent = new Object();

		devicesDiscovered.clear();

		DiscoveryListener listener = new DiscoveryListener() {

			public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
				System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
				devicesDiscovered.addElement(btDevice);
				try {
					System.out.println("     name " + btDevice.getFriendlyName(false));
				} catch (IOException cantGetDeviceName) {
				}
			}

			public void inquiryCompleted(int discType) {
				System.out.println("Device Inquiry completed!");
				synchronized (inquiryCompletedEvent) {
					inquiryCompletedEvent.notifyAll();
				}
			}

			public void serviceSearchCompleted(int transID, int respCode) {
				System.out.println("service search completed!");
				synchronized (serviceSearchCompletedEvent) {
					serviceSearchCompletedEvent.notifyAll();
				}
			}

			public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
				for (int i = 0; i < servRecord.length; i++) {
					String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
					if (url == null) {
						continue;
					}
					serviceFound.add(url);
					DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
					if (serviceName != null) {
						System.out.println("service " + serviceName.getValue() + " found " + url);
					} else {
						System.out.println("service found " + url);
					}
				}
			}
		};

		synchronized (inquiryCompletedEvent) {
			boolean started = false;
			try {
				devicesDiscovered.clear();
				started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (started) {
				System.out.println("wait for device inquiry to complete...");
				inquiryCompletedEvent.wait();
				System.out.println(devicesDiscovered.size() + " device(s) found");
			}
		}

		// start searchng for services fro each discovered device
		UUID[] searchUuidSet = new UUID[] { // new UUID(0x0003), // RFCOMM,
				BluetoothConsts.SERIAL_PORT_UUID,
				// new UUID(0x1000),//ServiceDiscoveryServerServiceClassID
				// new UUID(0x0001) // SDP protocol
		};

		int[] attrIDs = new int[] { 0x0100 // Service name
		};

		for (Enumeration en = devicesDiscovered.elements(); en.hasMoreElements();)

		{
			RemoteDevice btDevice = (RemoteDevice) en.nextElement();

			synchronized (serviceSearchCompletedEvent) {
				serviceFound.clear();
				System.out.println(
						"search services on " + btDevice.getBluetoothAddress() + " " + btDevice.getFriendlyName(false));

				LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, btDevice,
						listener);
				serviceSearchCompletedEvent.wait();
				System.out.println(
						serviceFound.size() + " services found for device on " + btDevice.getFriendlyName(false));
			}
		}
	}

	/**
	 * Writes message to printer
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public void write(String msg) throws IOException {
		String serverURL = "btspp://00195D255C10:1";
		StreamConnection streamConnection = (StreamConnection) Connector.open(serverURL);
		// send string
		OutputStream outStream = streamConnection.openOutputStream();
		PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
		pWriter.write(msg);
		pWriter.write("\n");
		pWriter.write("\n");
		pWriter.flush();
		outStream.close();
		streamConnection.close();
	}

}
