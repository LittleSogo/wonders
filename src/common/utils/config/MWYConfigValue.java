package common.utils.config;

import wfc.service.config.Config;
import wfc.service.config.ConfigValue;

/**
 * @author mywaystay
 */
public class MWYConfigValue extends ConfigValue{

	public MWYConfigValue() {
		
	}
	
	public static String getMWYLogDetailSwitch() {
		String name = "mwy.service.mwylog.detail.switch";
		String logDetailSwitch = Config.get(name);
		if (logDetailSwitch == null) logDetailSwitch = "ON";
		return logDetailSwitch;
	}
	
}
