package com.koolertron.ch_ppyy01_bk.provider;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.MethodTypeEnum.PostMethod;

@ObjectClassDefinition(name = "Digital Pin Configuration")
@interface CH_PPYY01_BKConfiguration {
	@AttributeDefinition(name = "Device Name", description = "Name of this device", required = true)
	String name() default "my_CH_PPYY01_BK";

	@AttributeDefinition(name = "Device Bluetooth Address", description = "Specifies the bluetooth address for this device", required = true)
	String btAddress() default "";

	@AttributeDefinition(name = "Obex Service Id", description = "Gives the OBEX service ID for this device.", required = true)
	String obexServiceNumber() default "";
}

/**
 * Bluetooth device provider for koolertron model CH-PPYY01-BK. Details can be
 * found here
 * http://www.koolertron.com/koolertron-58mm-mini-portable-bluetooth-40-wireless
 * -receipt-thermal-printer-compatible-with-apple-and-android-p-648.html.
 * 
 */
@Component(name = "com.koolertron.CH_PPYY01_BK")
@Designate(ocd = CH_PPYY01_BKConfiguration.class, factory = true)
public class CH_PPYY01_BKImpl implements Device {
	private CH_PPYY01_BKConfiguration config;

	@PostMethod
	public String sendDataThroughObex(String value) {
		String serverURL = String.format("btgoep://%s:%s", config.btAddress(), config.obexServiceNumber());

		System.out.println("Connecting to " + serverURL);

		try {
			ClientSession clientSession = (ClientSession) Connector.open(serverURL);
			HeaderSet hsConnectReply = clientSession.connect(null);
			if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
				System.out.println("Failed to connect");
				return "Failed to connect";
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
		return "Success";

	}

	@Activate
	public void activate(CH_PPYY01_BKConfiguration config) {
		this.config = config;
	}

	// Override to make it more user friendly and make the configuration easy
	@Override
	public String getId() {
		return config.name();
	}

}
