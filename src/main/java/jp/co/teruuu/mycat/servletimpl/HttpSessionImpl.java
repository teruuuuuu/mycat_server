package jp.co.teruuu.mycat.servletimpl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.teruuu.mycat.servletinterface.HttpSession;

public class HttpSessionImpl implements HttpSession{

	private String id;
	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	private volatile long lastAccessedTime;
	public String getId() {
		return this.id;
	}
	
	@Override
	public Object getAttributes(String name) {
		return this.attributes.get(name);
	}

	@Override
	public void setAttributes(String name, Object value) {
		if(value == null){
			removeAttribute(name);
			return;
		}
		this.attributes.put(name,  value);
	}
	
	@Override
	public Enumeration<String> getAttributeNames() {
		Set<String> names = new HashSet<String>();
		names.addAll(attributes.keySet());
		return Collections.enumeration(names);
	}
	
	@Override
	public void removeAttribute(String name) {
		this.attributes.remove(name);
	}
	
	
	synchronized void access() {
		this.lastAccessedTime = System.currentTimeMillis();
	}
	
	long getlastAccessedTime() {
		return this.lastAccessedTime;
	}
	
	public HttpSessionImpl(String id) {
		this.id = id;
		this.access();
	}

	

}
