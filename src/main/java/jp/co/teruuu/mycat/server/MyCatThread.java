package jp.co.teruuu.mycat.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.teruuu.mycat.config.AppProperties;
import jp.co.teruuu.mycat.servletimpl.ServletInfo;
import jp.co.teruuu.mycat.servletimpl.ServletService;
import jp.co.teruuu.mycat.servletimpl.WebApplication;
import jp.co.teruuu.mycat.util.MyURLDecoder;
import jp.co.teruuu.mycat.util.MyUtil;
import jp.co.teruuu.mycat.util.SendResponse;

public class MyCatThread implements Runnable{
	private Socket socket;
	private AppProperties appProp;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public MyCatThread(Socket socket, AppProperties appProp){
		this.socket = socket;
		this.appProp = appProp;
	}
	
	

	public void serverReceive(Socket socket) throws IOException{
		int ch;
		String text = "";
		//クライアントから受け取った内容をserver_recv.txtに出力
		InputStream input = socket.getInputStream();
		// クライアントは、終了のマークとして0を送付してくる
		while((ch = input.read()) != 0){
			text += (char)ch;
		}
		System.out.println("server receive:" + text);
	}

	public void serverSend(Socket socket, String sendText) throws IOException{
		OutputStream output = socket.getOutputStream();
		for (char ch: sendText.toCharArray()) {
			output.write(ch);
		}
		output.write(0);
		System.out.println("server send:" + sendText);
	}
	
	private static void addRequestHeader(Map<String, String> requestHeader, String line){
		int colonPos = line.indexOf(":");
		if(colonPos == -1) 
			return;
		
		String headerName = line.substring(0,  colonPos).toUpperCase();
		String headerValue = line.substring(colonPos + 1).trim();
		requestHeader.put(headerName, headerValue);
		
	}
	
	@Override
	public void run() {
		OutputStream output;
		try {
			InputStream input = socket.getInputStream();
			StringBuilder sb = new StringBuilder();
			String host = null;
			
			String method = null;
			String line;
			String requestLine = null;
			Map<String, String> requestHeader = new HashMap<String, String>();
			while ((line = MyUtil.readLine(input)) != null){
				if(line == "")
					break;
				if(line.toUpperCase().startsWith("GET")){
					method = "GET";
					requestLine = line;
				}else if(line.toUpperCase().startsWith("POST")){
					method = "POST";
					requestLine = line;
				}else {
					addRequestHeader(requestHeader, line);
				}
				sb.append(line + "\r\n");
			}
			if(requestLine == null)
				return;
			logger.info(sb.toString());
			
			String reqUri = MyURLDecoder.decode(requestLine.split(" ")[1], appProp.getURIEncoding());
			String[] pathAndQuery = reqUri.split("\\?");
			String path = pathAndQuery[0];
			String query = null;
			if (pathAndQuery.length > 1){
				query = pathAndQuery[1];
			}
			output = new BufferedOutputStream(socket.getOutputStream());
			
			String appDir = path.substring(1).split("/")[0];
			WebApplication webApp = WebApplication.serchWebApplication(appDir);
			if(webApp != null ) {
				ServletInfo servletInfo = webApp.serchServlet(path.substring(appDir.length() + 1));
				if(servletInfo != null){
					ServletService.doService(method, query, servletInfo, requestHeader, input, output);
					output.close();
					return;
				}
			}
			
			
			String ext = null;
			String[] tmp = reqUri.split("\\.");
			ext = tmp[tmp.length -1];
			if(path.endsWith("/")){
				path += "index.html";
				ext = "html";
			}
			
			FileSystem fs = FileSystems.getDefault();
			File documentRootFile = new File(appProp.getDocumentRoot());
			Path pathObj = fs.getPath(appProp.getDocumentRoot() + path);
			Path realPath;
			try{
				realPath = pathObj.toRealPath();
			}catch(NoSuchFileException ex){
				SendResponse.sendNotFoundResponse(output, appProp);
				output.close();
				return;
			}
			if(!realPath.startsWith(documentRootFile.getAbsolutePath())){
				SendResponse.sendNotFoundResponse(output, appProp);
				output.close();
				return;
			}else if(Files.isDirectory(realPath)){
				SendResponse.sendNotFoundResponse(output, appProp);
				output.close();
				return;
			}else if(Files.isDirectory(realPath)){
				String location = "http://" 
						+ ((appProp.getHost() != null ) ? host : appProp.getHost())
					    + path;
				SendResponse.sendMovePermanentlyResponse(output, location, appProp);
				output.close();
				return;
			}
			
			try(InputStream fis = new BufferedInputStream(Files.newInputStream(realPath))){
				SendResponse.sendOkResponse(output, fis, ext, appProp);
				output.close();
			}catch(FileNotFoundException ex){
				SendResponse.sendNotFoundResponse(output, appProp);
				output.close();
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		} finally{
			try{
				socket.close();
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

}
