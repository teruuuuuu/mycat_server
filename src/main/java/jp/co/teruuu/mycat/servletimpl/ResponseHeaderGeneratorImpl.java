package jp.co.teruuu.mycat.servletimpl;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import jp.co.teruuu.mycat.servlet.http.Cookie;
import jp.co.teruuu.mycat.util.MyUtil;
import jp.co.teruuu.mycat.util.ResponseHeaderGenerator;

public class ResponseHeaderGeneratorImpl implements ResponseHeaderGenerator{
	private ArrayList<Cookie> cookies;
	
	ResponseHeaderGeneratorImpl(ArrayList<Cookie> cookies){
		this.cookies = cookies;
	}
	
	private static String getCookieDateString(Calendar cal) {
		DateFormat df = new SimpleDateFormat("EEE, dd-MM-yyy HH:mm:ss", Locale.US);
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime()) + " GMT";
	}
	
	@Override
	public void generate(OutputStream output) throws IOException{
		for(Cookie cookie: cookies) {
			String header;
			header = "Set-Cookie:" + cookie.getName() + "=" + cookie.getValue();
			if(cookie.getDomain() != null){
				header += "; Domain=" + cookie.getDomain();
			}
			if(cookie.getMaxAge() > 0){
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UC"));
				cal.add(Calendar.SECOND, cookie.getMaxAge());
				header += "; Expires=" + getCookieDateString(cal);
			}else if(cookie.getMaxAge() == 0){
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				cal.set(1970, 0, 1, 0, 0, 10);
				header += "; Expires=" + getCookieDateString(cal);
			}
			if(cookie.getPath() != null){
				header += "; Path=" + cookie.getPath();
			}
			if(cookie.getSecure()){
				header += "; Secure";
			}
			if(cookie.isHttpOnly()){
				header += "; HttpOnly";
			}
			MyUtil.writeLine(output, header);
		}
		
	}
}
