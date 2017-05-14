package jp.co.teruuu.mycat.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class MyUtil {
	public static String readLine(InputStream input) throws Exception {
		int ch;
		String ret = "";
		while((ch = input.read()) != -1){
			if(ch == '\r'){
				//何もしない
			} else if(ch == '\n'){
				break;
			}else {
				ret += (char)ch;
			}
			
		}
		if(ch == -1){
			return null;
		}else {
			return ret;
		}
	}
	
	public static void writeLine(OutputStream output, String str) throws Exception {
		for (char ch : str.toCharArray()) {
			output.write((int) ch);
		}
		output.write((int) '\r');
		output.write((int) '\n');
	}
	
	public static String getDateString(TimeZone tz) {
		Calendar cal = Calendar.getInstance(tz);
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime());
	}
	
	public static String getDateStringUgc() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime());
	}
	
	static final HashMap<String, String> contentTypeMap = 
			new HashMap<String, String>() {
				{put("html", "text/html");
				put("html", "text/html");
				put("txt", "text/plain");
				put("css", "text/css");
				put("png", "text/png");
				put("jpg", "text/jpeg");
				put("jpeg", "text/jpeg");
				put("gif", "text/gif");}};
	
	public static String getContentType(String ext) {
		String ret = contentTypeMap.get(ext.toLowerCase());
		if (ret == null){
			return "application/octet-stream";
		}else {
			return ret;
		}
	}

}
