package ink.cwblog.socketclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class SocketClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketClientApplication.class, args);
        while (true){
            System.out.print("输入：");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                final String next = scanner.next();
                client(next);
                if ("end".equals(next)){
                    break;
                }
            }
        }
    }


    public static void client(String message) {
        try {
            Socket socket = new Socket("127.0.0.1", 7788);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outputStream.write( message.getBytes("UTF-8"));
            outputStream.flush();
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            socket.shutdownOutput();
            //获取响应
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len, "UTF-8"));
            }
            log.info("response : {}",sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
