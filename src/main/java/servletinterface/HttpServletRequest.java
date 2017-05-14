package servletinterface;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.Cookie;

public interface HttpServletRequest {
    String getMethod();
    String getParameter(String name);
    String[] getParameterValues(String name);
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;
    
}