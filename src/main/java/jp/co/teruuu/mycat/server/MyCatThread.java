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

import jp.co.teruuu.mycat.config.AppProperties;
import jp.co.teruuu.mycat.util.MyURLDecoder;
import jp.co.teruuu.mycat.util.SendResponse;
import jp.co.teruuu.mycat.util.Util;

public class MyCatThread implements Runnable{
	private Socket socket;
	private AppProperties appProp;
	
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
	
	private static String readLine(InputStream input) throws Exception {
		int ch;
		String ret = "";
		while((ch = input.read()) != -1){
			if(ch == '\r') {
				
			}else if(ch == '\n') {
				break;
			} else {
				ret += (char)ch;
			}
		}
		if (ch == -1){
			return null;
		}else {
			return ret;
		}
	}
	
	@Override
	public void run() {
		OutputStream output;
		try {
			InputStream input = socket.getInputStream();
			
			String line;
			String path = null;
			String ext = null;
			String host = null;
			while ((line = Util.readLine(input)) != null){
				if(line == "")
					break;
				if(line.startsWith("GET")){
					path = MyURLDecoder.decode(line.split(" ")[1], "UTF-8");
					String[] tmp = path.split("\\.");
					ext = tmp[tmp.length -1];
				}else if(line.startsWith("Host:")) {
					host = line.substring("Host: ".length());
				}
			}
			
			if(path == null){
				return;
			}
			if(path.endsWith("/")){
				path += "index.html";
				ext = "html";
			}
			
			output = new BufferedOutputStream(socket.getOutputStream());
			
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
