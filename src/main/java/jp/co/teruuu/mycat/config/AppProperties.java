package jp.co.teruuu.mycat.config;

import java.util.TimeZone;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
	private String name;
	private TimeZone timezone;
	private String serversend;
	private String clientsend;
	private String documentRoot;
	
	private String host;
	private int port;
    

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TimeZone getTimezone() {
		return timezone;
	}
	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
	}
	public String getServersend() {
		return serversend;
	}
	public void setServersend(String serversend) {
		this.serversend = serversend;
	}
	public String getClientsend() {
		return clientsend;
	}
	public void setClientsend(String clientsend) {
		this.clientsend = clientsend;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDocumentRoot() {
		return documentRoot;
	}
	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
	}
}
