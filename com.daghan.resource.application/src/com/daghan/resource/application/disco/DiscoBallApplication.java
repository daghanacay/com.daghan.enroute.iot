package com.daghan.resource.application.disco;

import java.lang.reflect.InvocationTargetException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.resource.manager.ResourceRunner;
import com.daghan.resource.application.disco.DiscoBallApplication.StartStopEnum;

@ObjectClassDefinition(name = "Disco ball configuration")
@interface DiscoballConfig {
	@AttributeDefinition(name = "Stop/Start", description = "Stops or start the disco ball.")
	StartStopEnum start() default StartStopEnum.START;

	@AttributeDefinition(name = "Frequency", description = "Frequency in milliseconds.", required = true, min = "100")
	int frequency() default 100;

	@AttributeDefinition(name = "Red color output", description = "POST on the resource while it is time for red to turn on.")
	String redColorResource() default "na";

	@AttributeDefinition(name = "Green color output", description = "POST on the resource while it is time for green to turn on.")
	String greenColorResource() default "na";

	@AttributeDefinition(name = "Blue color output", description = "POST on the resource while it is time for blue to turn on.")
	String blueColorResource() default "na";
}

@Component(service = DiscoBallApplication.class, immediate = true)
@Designate(ocd = DiscoballConfig.class)
public class DiscoBallApplication {
	public enum StartStopEnum {
		START, STOP;
	}

	@Reference
	private ResourceRunner rm;

	private DiscoballConfig config;
	private static final Logger s_logger = LoggerFactory.getLogger(DiscoBallApplication.class);
	private volatile boolean runThread = true;

	@Activate
	public void activate(DiscoballConfig config) {
		this.config = config;
		new DiscoThread().start();
	}

	@Modified
	public void modified(DiscoballConfig config) {
		this.config = config;
	}
	
	@Deactivate
	public void deactivate() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		runThread = false;
		rm.activateResource(config.redColorResource(), "LOW", String.class, MethodTypeEnum.POST);
		rm.activateResource(config.greenColorResource(), "LOW", String.class, MethodTypeEnum.POST);
		rm.activateResource(config.blueColorResource(), "LOW", String.class, MethodTypeEnum.POST);
		s_logger.info("stopping now");
	}

	private class DiscoThread extends Thread {
		@Override
		public void run() {
			int colorScheme = 0;
			while (runThread) {
				// disco ball!!!!

				try {
					updateColor(colorScheme);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | InterruptedException e) {
					e.printStackTrace();
				}

				colorScheme += 1;
				if (colorScheme % 3 == 0) {
					colorScheme = 0;
				}
			}
		}

		private void updateColor(int colorScheme) throws IllegalAccessException, IllegalArgumentException,
				InvocationTargetException, NoSuchMethodException, InterruptedException {
			switch (colorScheme) {
			case 0:
				rm.activateResource(config.redColorResource(), "HIGH", String.class, MethodTypeEnum.POST);
				rm.activateResource(config.greenColorResource(), "LOW", String.class, MethodTypeEnum.POST);
				rm.activateResource(config.blueColorResource(), "LOW", String.class, MethodTypeEnum.POST);
				break;
			case 1:
				rm.activateResource(config.redColorResource(), "LOW", String.class, MethodTypeEnum.POST);
				rm.activateResource(config.greenColorResource(), "HIGH", String.class, MethodTypeEnum.POST);
				rm.activateResource(config.blueColorResource(), "LOW", String.class, MethodTypeEnum.POST);
				break;
			case 2:
				rm.activateResource(config.redColorResource(), "LOW", String.class, MethodTypeEnum.POST);
				rm.activateResource(config.greenColorResource(), "LOW", String.class, MethodTypeEnum.POST);
				rm.activateResource(config.blueColorResource(), "HIGH", String.class, MethodTypeEnum.POST);
				break;
			}
			Thread.sleep(config.frequency());
		}
	}
}
