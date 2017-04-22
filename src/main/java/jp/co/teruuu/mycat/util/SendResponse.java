package jp.co.teruuu.mycat.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.teruuu.mycat.config.AppProperties;

public class SendResponse {
	public static void sendOkResponse(OutputStream output, InputStream fis, String ext, AppProperties appProp) throws Exception {
		// レスポンスヘッダを返す
		Util.writeLine(output, "HTTP/1.1 200 OK");
		Util.writeLine(output, "Date: " + Util.getDateString(appProp.getTimezone()));
		Util.writeLine(output, "Server: MyCat/0.2");
		Util.writeLine(output, "Connection: close");
		Util.writeLine(output, "Content-type: " + Util.getContentType(ext));
		Util.writeLine(output, "");
		
		int ch;
		while((ch = fis.read()) != -1){
			output.write(ch);
		}
	}
	
	public static void sendMovePermanentlyResponse(OutputStream output, String location, AppProperties appProp) throws Exception {
		Util.writeLine(output, "HTTP/1.1 301 Moved Permanetly");
		Util.writeLine(output, "Date: " + Util.getDateString(appProp.getTimezone()));
		Util.writeLine(output, "HTTP/1.1 301 Moved Permanetly");
		Util.writeLine(output, "Server: MyCat/0.2");
		Util.writeLine(output, "Connection: " + location);
		Util.writeLine(output, "");
	}
	
	public static void sendNotFoundResponse(OutputStream output, AppProperties appProp) throws Exception {
		Util.writeLine(output, "HTTP/1.1 301 Moved Permanetly");
		Util.writeLine(output, "Date: " + Util.getDateString(appProp.getTimezone()));
		Util.writeLine(output, "HTTP/1.1 301 Moved Permanetly");
		Util.writeLine(output, "Server: MyCat/0.2");
		Util.writeLine(output, "Connection: close");
		Util.writeLine(output, "Content-Type: text/html");
		Util.writeLine(output, "");
		
		try(InputStream fis = new BufferedInputStream(new FileInputStream(appProp.getErrorDocument() + "/404.html"))){
			int ch;
			while ((ch = fis.read()) != -1){
				output.write(ch);
			}
		
		}
	}
}
