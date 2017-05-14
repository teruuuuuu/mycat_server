package jp.co.teruuu.mycat.servletimpl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

public class WebApplication {
	private static String WEBAPPS_DIR = "src/main/webApps";
	private static Map<String, WebApplication> webAppCollection = new HashMap<String, WebApplication>();
	String directory;
	ClassLoader classLoader;
	private Map<String, ServletInfo> servletCollection = new HashMap<String, ServletInfo>();

	private WebApplication(String webAPPS_DIR, String dir) throws MalformedURLException {
		this.directory = dir;
		FileSystem fs = FileSystems.getDefault();

		Path pathObj = fs.getPath(webAPPS_DIR + File.separator + dir);
		this.classLoader = URLClassLoader.newInstance(new URL[] { pathObj.toUri().toURL() });
	}

	public static WebApplication createInstance(String dir) throws MalformedURLException {
		WebApplication newApp = new WebApplication(WEBAPPS_DIR, dir);
		webAppCollection.put(dir, newApp);

		return newApp;
	}

	public void addServlet(String urlPattern, String servletClassName)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.servletCollection.put(urlPattern,
				new ServletInfo(this, urlPattern, servletClassName));
	}

	public ServletInfo serchServlet(String path) {
		return servletCollection.get(path);
	}

	public static WebApplication serchWebApplication(String dir) {
		return webAppCollection.get(dir);
	}
}
