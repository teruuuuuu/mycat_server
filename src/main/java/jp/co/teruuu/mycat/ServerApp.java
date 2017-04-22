package jp.co.teruuu.mycat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jp.co.teruuu.mycat.server.MyCatMain;

@SpringBootApplication
public class ServerApp {

	@Autowired
	MyCatMain myCatMain;
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
    	myCatMain.run();
        System.out.println("処理終了");
    }
}
