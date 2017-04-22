package jp.co.teruuu.mycat.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.teruuu.mycat.config.AppProperties;


@Component
public class MyCatMain {
	@Autowired
	private AppProperties appProp;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	public void run() throws Exception{
		try(ServerSocket server = new ServerSocket(8001)){
			for(;;){
				Socket socket = server.accept();
				MyCatThread mycatThread = new MyCatThread(socket, appProp);
				Thread thread = new Thread(mycatThread);
				thread.start();
			}			
		}
	}
	

}
