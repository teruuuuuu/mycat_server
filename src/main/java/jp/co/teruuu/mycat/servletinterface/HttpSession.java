package jp.co.teruuu.mycat.servletinterface;

import java.util.Enumeration;

public interface HttpSession {
	Object getAttributes(String name);
	void setAttributes(String name, Object value);
	Enumeration<String> getAttributeNames();
	void removeAttribute(String name);
}
