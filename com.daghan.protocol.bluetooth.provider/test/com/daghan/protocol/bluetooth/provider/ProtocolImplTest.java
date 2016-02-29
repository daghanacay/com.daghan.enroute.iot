package com.daghan.protocol.bluetooth.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;

import junit.framework.TestCase;

public class ProtocolImplTest extends TestCase {
	
	public static final Vector devicesDiscovered = new Vector();
	public static final Vector serviceFound = new Vector();

	static final UUID OBEX_OBJECT_PUSH = new UUID(0x1107);

	// Test put @Activate to test writing to bluetooth device
	void searchDevice() throws IOException, InterruptedException {

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
			}
		};

		synchronized (inquiryCompletedEvent) {
			boolean started = false;
			try {
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
		// start searchng for services fro each discovered device
		UUID serviceUUID = OBEX_OBJECT_PUSH;
		UUID[] searchUuidSet = new UUID[] { serviceUUID };
		int[] attrIDs = new int[] { 0x0100 // Service name
		};

		for (Enumeration en = devicesDiscovered.elements(); en.hasMoreElements();) {
			RemoteDevice btDevice = (RemoteDevice) en.nextElement();

			synchronized (serviceSearchCompletedEvent) {
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

	// Test put @Activate to test writing to bluetooth device
	public void putData() {
		String serverURL = "btgoep://10D38ADC5EF6:12";

		System.out.println("Connecting to " + serverURL);

		try {
			ClientSession clientSession = (ClientSession) Connector.open(serverURL);
			HeaderSet hsConnectReply = clientSession.connect(null);
			if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
				System.out.println("Failed to connect");
				return;
			}

			HeaderSet hsOperation = clientSession.createHeaderSet();
			hsOperation.setHeader(HeaderSet.NAME, "Hello.txt");
			hsOperation.setHeader(HeaderSet.TYPE, "text");

			// Create PUT Operation
			Operation putOperation = clientSession.put(hsOperation);

			// Send some text to server
			byte data[] = "Hello world!".getBytes("iso-8859-1");
			OutputStream os = putOperation.openOutputStream();
			os.write(data);
			os.close();

			putOperation.close();

			clientSession.disconnect(null);

			clientSession.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
