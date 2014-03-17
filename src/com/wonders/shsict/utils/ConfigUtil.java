package com.wonders.shsict.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.os.Environment;

public class ConfigUtil {
	 
	/**
	 * 
	 * @return eg http://1.1.1.1:8080
	 */
	public static String getShsictServiceURLString() {
		String defaultUrl = "http://www.douban.com";
//		return defaultUrl;
		String configPath = Environment.getExternalStorageDirectory().getPath()+"/Shsict.config";
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(configPath));
			String ip = br.readLine();
			br.close();
			if(ip.trim().isEmpty()){
				return defaultUrl;
			}else{
				return "http://"+ip;				
			}
		} catch (IOException e) {
			e.printStackTrace();
			return defaultUrl;
		}
	}
}
