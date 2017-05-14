package jp.co.teruuu.mycat.servletimpl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import servletinterface.HttpServletResponse;


class HttpServletResponseImpl implements HttpServletResponse {
	String contentType = "application/octet-stream";
	private String characterEncoding = "UTF-8";
	private OutputStream outputStream;
	PrintWriter printWriter;
	int status;
	String redirectLocation;
	
	public HttpServletResponseImpl(OutputStream output) {
		this.outputStream = output;
		this.status = SC_OK;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
		String[] temp = contentType.split(" *;");
		if (temp.length > 1) {
			String[] keyValue = temp[1].split("=");
			if (keyValue.length == 2 && keyValue[0].equals("charset")) {
				setCharacterEncoding(keyValue[1]);
			}
		}
	}

	@Override
	public void setCharacterEncoding(String charset) {
		this.characterEncoding = charset;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream, this.characterEncoding));
		return this.printWriter;
	}

	@Override
	public void sendRedirect(String location) {
		this.redirectLocation = location;
		setStatus(SC_FOUND);
	}
	
	@Override
	public void setStatus(int sc){
		this.status = sc;
	}
	
}
