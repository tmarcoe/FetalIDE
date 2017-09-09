package com.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFile {
	public static Properties getProperties(String file) {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(file);
			prop.load(input);
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return prop;
		
	}
}
