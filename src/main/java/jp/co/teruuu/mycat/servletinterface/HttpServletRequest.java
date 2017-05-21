package jp.co.teruuu.mycat.servletinterface;

import java.io.UnsupportedEncodingException;
import jp.co.teruuu.mycat.servlet.http.Cookie;


public interface HttpServletRequest {
    String getMethod();
    String getParameter(String name);
    String[] getParameterValues(String name);
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;
    Cookie[] getCookie();
    HttpSession getSession();
    
}