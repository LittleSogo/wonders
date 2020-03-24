package common.consts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class KqzlConfigConst {
	
	private Properties p; 
	
	public KqzlConfigConst() {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kqzl_config.properties");
		this.p = new Properties();
		try {
			this.p.load(new InputStreamReader(inputStream, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public KqzlConfigConst(String fileName) {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		this.p = new Properties();
		try {
			this.p.load(new InputStreamReader(inputStream, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getConstName(String key){
		String value = "";
		if(this.p.getProperty(key) != null){
			value = this.p.getProperty(key);
		}
		return value;
	}
	
}
