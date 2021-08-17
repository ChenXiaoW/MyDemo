package ink.cwblog.socketservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class SocketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketServiceApplication.class, args);
        socketService();
    }


    public static void socketService() {

        //如果使用多线程，那就需要线程池，防止并发过高时创建过多线程耗尽资源
      //  ExecutorService threadPool = Executors.newFixedThreadPool(100);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(32,100,60,TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        try (ServerSocket serverSocket = new ServerSocket(7788);
        ) {
            log.info("server将一直等待连接的到来");
            while (true){
                Socket socket = serverSocket.accept();
                SocketThread socketThread = new SocketThread(socket);
                threadPoolExecutor.execute(socketThread);
            }

//            while (true) {
//                Socket socket = serverSocket.accept();
//                //从socket中获取输入流，并简历缓冲区进行读取
//                InputStream inputStream = socket.getInputStream();
//                byte [] bytes = new byte[1024];
//                int len ;
//                StringBuilder sb = new StringBuilder();
//                while ((len = inputStream.read(bytes))!=-1){
//                    sb.append(new String(bytes,0,len,"UTF-8"));
//                }
//                log.info("[{}] - [client]：{}",Thread.currentThread().getName(), sb.toString());
//                OutputStream outputStream = socket.getOutputStream();
//                String resp = "[service]:your message [ "+sb.toString()+"]";
//                outputStream.write(resp.getBytes("UTF-8"));
//                outputStream.flush();
//                inputStream.close();
//                socket.close();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

@Slf4j
class SocketThread extends Thread {
    private Socket socket;

    public SocketThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
      try {
          InputStream inputStream = socket.getInputStream();
          byte[] bytes = new byte[1024];
          int len;
          StringBuilder sb = new StringBuilder();
          while ((len = inputStream.read(bytes)) != -1) {
              sb.append(new String(bytes, 0, len, "UTF-8"));
          }
          log.info("[{}] - [client]：{}", Thread.currentThread().getName(), sb.toString());
          OutputStream outputStream = socket.getOutputStream();
          String resp = "[service]:your message [ " + sb.toString() + "]";
          outputStream.write(resp.getBytes("UTF-8"));
          outputStream.flush();
          inputStream.close();
          socket.close();
      }catch (Exception e){
          e.printStackTrace();
      }
    }
}
