package jp.co.teruuu.mycat.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.teruuu.mycat.config.AppProperties;

public class SendResponse {
	
	public static void sendOkResponseHeader(OutputStream output, String contentType, AppProperties appProp, ResponseHeaderGenerator hg) throws Exception {
		MyUtil.writeLine(output, "HTTP/1.1 200 OK");
		MyUtil.writeLine(output, "Date: " + MyUtil.getDateStringUgc());
		MyUtil.writeLine(output, "Server: MyCat/0.2");
		MyUtil.writeLine(output, "Connection: close");
		MyUtil.writeLine(output, "Content-type: " + contentType);
		hg.generate(output);
		MyUtil.writeLine(output, "");
	}
	
	public static void sendOkResponse(OutputStream output, InputStream fis, String ext, AppProperties appProp) throws Exception {
		// レスポンスヘッダを返す
		MyUtil.writeLine(output, "HTTP/1.1 200 OK");
		MyUtil.writeLine(output, "Date: " + MyUtil.getDateStringUgc());
		MyUtil.writeLine(output, "Server: MyCat/0.2");
		MyUtil.writeLine(output, "Connection: close");
		MyUtil.writeLine(output, "Content-type: " + MyUtil.getContentType(ext));
		MyUtil.writeLine(output, "");
		
		int ch;
		while((ch = fis.read()) != -1){
			output.write(ch);
		}
	}
	
	public static void sendMovePermanentlyResponse(OutputStream output, String location, AppProperties appProp) throws Exception {
		MyUtil.writeLine(output, "HTTP/1.1 301 Moved Permanetly");
		MyUtil.writeLine(output, "Date: " + MyUtil.getDateStringUgc());
		MyUtil.writeLine(output, "Server: MyCat/0.2");
		MyUtil.writeLine(output, "Location: " + location);
		MyUtil.writeLine(output, "Connection: close");
		MyUtil.writeLine(output, "");
	}
	
	public static void sendFoundResponse(OutputStream output, String location, ResponseHeaderGenerator hg) throws Exception {
		MyUtil.writeLine(output, "HTTP/1.1 302 Found");
		MyUtil.writeLine(output, "Date: " + MyUtil.getDateStringUgc());
		MyUtil.writeLine(output, "Server: MyCat/0.2");
		MyUtil.writeLine(output, "Location: " + location);
		MyUtil.writeLine(output, "Connection: close");
		hg.generate(output);
		MyUtil.writeLine(output, "");
	}
	
	public static void sendNotFoundResponse(OutputStream output,AppProperties appProp) throws Exception {
		MyUtil.writeLine(output, "HTTP/1.1 404 Not Found");
		MyUtil.writeLine(output, "Date: " + MyUtil.getDateStringUgc());
		MyUtil.writeLine(output, "Server: MyCat/0.2");
		MyUtil.writeLine(output, "Connection: close");
		MyUtil.writeLine(output, "Content-Type: text/html");
		MyUtil.writeLine(output, "");
		
		try(InputStream fis = new BufferedInputStream(new FileInputStream(appProp.getErrorDocument() + "/404.html"))){
			int ch;
			while ((ch = fis.read()) != -1){
				output.write(ch);
			}
		
		}
	}
}
