package jp.co.teruuu.mycat.tcp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.teruuu.mycat.config.AppProperties;


@Component
public class ServerModoki {
	@Autowired
	private AppProperties appProp;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	public void run() throws Exception{
		try(ServerSocket server = new ServerSocket(8001)){
			Socket socket = server.accept();
			InputStream input = socket.getInputStream();
			
			String line;
			String path = null;
			while ((line = readLine(input)) != null){
				if(line == "")
					break;
				if(line.startsWith("GET")){
					path = line.split(" ")[1];
				}
			}
			OutputStream output = socket.getOutputStream();
			// レスポンスヘッダを返す
			writeLine(output, "HTTP/1.1 200 OK");
			writeLine(output, "Date: " + this.getDateString());
			writeLine(output, "Server: MyCat/0.1");
			writeLine(output, "Connection: close");
			writeLine(output, "Content-type: text/html");
			writeLine(output, "");
			
			//レスポンスボディを返す
			try(FileInputStream fis = new FileInputStream(appProp.getDocumentRoot() + path);){
				int ch;
				while((ch = fis.read()) != -1){
					output.write(ch);
				}
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
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
	
	private void writeLine(OutputStream output, String str) throws Exception {
		for (char ch : str.toCharArray()) {
			output.write((int) ch);
		}
		output.write((int) '\r');
		output.write((int) '\n');
	}
	
	private String getDateString() {
		Calendar cal = Calendar.getInstance(appProp.getTimezone());
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime());
	}
}
