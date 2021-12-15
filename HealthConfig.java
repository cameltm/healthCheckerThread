import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.OutputStream;
import java.util.Properties;

public class HealthConfig {

	private Properties prop = new Properties();

	HealthConfig() {
		try (InputStream input = new FileInputStream("config.properties")) {
			prop.load(input);
//			System.out.println(prop);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Properties getProperties() {
		return prop;
	}
}
