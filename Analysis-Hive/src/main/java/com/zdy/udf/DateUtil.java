package com.zdy.udf;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class DateUtil {
	/**
	 * 得到指定date的零时刻.
	 */
	public static Date getZeroDate(Date d) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
			return sdf.parse(sdf.format(d));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
