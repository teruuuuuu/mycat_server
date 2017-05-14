package jp.co.teruuu.mycat.servletimpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.Cookie;

import servletinterface.HttpServletRequest;


class HttpServletRequestImpl implements HttpServletRequest {
	private String method;
	private String characterEncoding;
	private Map<String, String[]> parameterMap;
	private Cookie[] cookies;


	public HttpServletRequestImpl(String method, Map<String, String[]> parameterMap) {
		this.characterEncoding = "UTF-8";
		this.method = method;
		this.parameterMap = parameterMap;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public String getParameter(String name) {
		String[] values = this.getParameterValues(name);
		if ( values == null){
			return null;
		}
		return values[0];
	}
	
	public String[] getParameterValues(String name) {
		String[] values = this.parameterMap.get(name);
		if ( values == null){
			return null;
		}
		String[] decoded = new String[values.length];
		try {
			for(int i = 0; i < values.length; i++){
				decoded[i] = URLDecoder.decode(values[i], this.characterEncoding);
			}
		} catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
		return decoded;
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		if (!Charset.isSupported(env)) {
			throw new UnsupportedEncodingException("encoding.." + env);
		}
		this.characterEncoding = env;
	}

	private static Cookie[] parseCookies(String cookieString) {
		if (cookieString == null) {
			return null;
		}
		String[] cookiePairArray = cookieString.split(";");
		Cookie[] ret = new Cookie[cookiePairArray.length];
		int cookieCount = 0;

		for (String cookiePair : cookiePairArray) {
			String[] pair = cookiePair.split("=", 2);

			ret[cookieCount] = new Cookie(pair[0], pair[1]);
			cookieCount++;
		}

		return ret;
	}
}