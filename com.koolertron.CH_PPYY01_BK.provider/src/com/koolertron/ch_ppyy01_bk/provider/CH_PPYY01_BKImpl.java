package com.koolertron.ch_ppyy01_bk.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.MethodTypeEnum.PostMethod;

@ObjectClassDefinition(name = "SPP Bluetooth Configuration")
@interface CH_PPYY01_BKConfiguration {
	@AttributeDefinition(name = "Device Name", description = "Name of this device", required = true)
	String name() default "my_CH_PPYY01_BK";

	@AttributeDefinition(name = "Device Bluetooth Address", description = "Specifies the bluetooth address for this device", required = true)
	String btAddress() default "";

	@AttributeDefinition(name = "SPP Service Id", description = "Gives the SPP service ID for this device.", required = true)
	String sppServiceNumber() default "";
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
	public String sendDataThroughSPP(String msg) throws IOException {
		String serverURL = String.format("btspp://%s:%s", config.btAddress(), config.sppServiceNumber());
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
