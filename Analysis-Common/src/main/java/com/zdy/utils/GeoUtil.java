package com.zdy.utils;


import java.io.InputStream;
import java.net.InetAddress;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.Reader;
import com.zdy.entity.GeoInfo;

/**
 * 地理工具类，实现通过ip查找地址区域
 */
public class GeoUtil {
	private static InputStream in ;
	private static Reader reader ;
	static{
		try {
			in = ClassLoader.getSystemResourceAsStream("GeoLite2-City.mmdb");
			reader = new Reader(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *获得国家数据
	 */
	public static String getCountry(String ip){
		try{
			JsonNode node = reader.get(InetAddress.getByName(ip));
			return node.get("country").get("names").get("zh-CN").textValue();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return "" ;
	}
	/**
	 *获得国家数据
	 */
	public static String getProvince(String ip){
		try{
			JsonNode node = reader.get(InetAddress.getByName(ip));
			return node.get("subdivisions").get(0).get("names").get("zh-CN").textValue();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return "" ;
	}

	/**
	 *获得国家数据
	 */
	public static String getCity(String ip){
		try{
			JsonNode node = reader.get(InetAddress.getByName(ip));
			return node.get("city").get("names").get("zh-CN").textValue();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return "" ;
	}

	/**
	 *获得国家数据
	 */
	public static GeoInfo getGeoInfo(String ip){
		try{
			JsonNode node = reader.get(InetAddress.getByName(ip));
			GeoInfo info = new GeoInfo();
			info.setCountry(getCountry(ip));
			info.setProvince(getProvince(ip));
			return info ;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null ;
	}
}
