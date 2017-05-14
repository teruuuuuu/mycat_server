package jp.co.teruuu.mycat.servletimpl;

import servletinterface.HttpServlet;

public class ServletInfo {
	WebApplication webApp;
	String urlPattern;
	String servletClassName;
	HttpServlet servlet;
	
	public ServletInfo(WebApplication webApp, String urlPattern, String servletClassName) {
		this.webApp = webApp;
		this.urlPattern = urlPattern;
		this.servletClassName = servletClassName;
	}
	
}
