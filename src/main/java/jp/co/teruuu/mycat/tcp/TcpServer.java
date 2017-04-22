package jp.co.teruuu.mycat.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.teruuu.mycat.config.AppProperties;


@Component
public class TcpServer {
	@Autowired
	private AppProperties appProp;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	public void run() throws Exception{
		sdf.setTimeZone(appProp.getTimezone());
		try(ServerSocket server = new ServerSocket(appProp.getPort())){
				System.out.println("クライアントからの接続を待ちます:" + appProp.getPort());
				Socket socket = server.accept();
				System.out.println("クライアント接続");
				System.out.println(appProp.getName());
				System.out.println("access time:" + sdf.format(new Date(Calendar.getInstance().getTimeInMillis())));
				
				serverReceive(socket);
				serverSend( socket, appProp.getServersend());
				socket.close();
				System.out.println("通信を終了しました");
		}catch(Exception ex){
			ex.printStackTrace();
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
}
