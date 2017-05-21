package jp.co.teruuu.mycat.servletimpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

import jp.co.teruuu.mycat.servlet.http.Cookie;
import jp.co.teruuu.mycat.servletinterface.HttpServletRequest;
import jp.co.teruuu.mycat.servletinterface.HttpSession;


class HttpServletRequestImpl implements HttpServletRequest {
	private String method;
	private String characterEncoding;
	private Map<String, String[]> parameterMap;
	private Cookie[] cookies;
	private HttpSessionImpl session;
	private HttpServletResponseImpl response;
	private WebApplication webApp;
	private final String SESSION_COOKIE_ID = "JSESSIONID";
	
	@Override
	public HttpSession getSession() {
		return getSession(true);
	}
	
	public HttpSession getSession(boolean create) {
		if(! create ){
			return this.session;
		}
		if(this.session == null){
			SessionManager manager = this.webApp.getSessionManager();
			this.session = manager.createSession();
			addSessionCookie();
		}
		return this.session;
	}
	
	private HttpSessionImpl getSessionInternal() {
		if(this.cookies == null){
			return null;
		}
		Cookie cookie = null;
		for(Cookie tempCookie : this.cookies) {
			if(tempCookie.getName().equals(SESSION_COOKIE_ID)){
				cookie = tempCookie;
			}
		}
		SessionManager manager = this.webApp.getSessionManager();
		HttpSessionImpl ret = null;
		if(cookie != null){
			ret = manager.getSession(cookie.getValue());
		}
		return ret;	
	}
	
	private void addSessionCookie() {
		Cookie cookie = new Cookie(SESSION_COOKIE_ID, this.session.getId());
		cookie.setPath("/" + webApp.directory + "/");
		cookie.setHttpOnly(true);
		this.response.addCookie(cookie);
	}

	public HttpServletRequestImpl(String method, 
			Map<String, String> requestHeader, 
			Map<String, String[]> parameterMap,
			HttpServletResponseImpl resp, 
			WebApplication webApp) {
		this.characterEncoding = "UTF-8";
		this.method = method;
		this.parameterMap = parameterMap;
		this.cookies = parseCookies(requestHeader.get("COOKIE"));
		this.response = resp;
		this.webApp = webApp;
		this.session = getSessionInternal();
		if(this.session != null){
			addSessionCookie();
		}
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

	@Override
	public Cookie[] getCookie() {
		return this.cookies;
	}
}