package jp.co.teruuu.mycat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jp.co.teruuu.mycat.tcp.ServerModoki;

@SpringBootApplication
public class ServerApp {

	@Autowired
	ServerModoki server;
	public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(ServerApp.class, args)) {
        	ServerApp app = ctx.getBean(ServerApp.class);
            app.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void run(String... args) throws Exception {
    	System.out.println("処理開始" + this.getClass().getName());
        //アプリの処理
		server.run();
        System.out.println("処理終了");
    }
}
