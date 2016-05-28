package chapter04;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainOioServer {

    private static final Logger logger = LoggerFactory.getLogger(PlainOioServer.class);
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final AtomicInteger count = new AtomicInteger();
    
    public void start(int port){
        try{
            final ServerSocket serverSocket = new ServerSocket(port);
            logger.info(PlainOioServer.class.getName()+" started and listen on "+serverSocket);
            while(true){
                final Socket clientSocket = serverSocket.accept();
                logger.info("Acceped connection from "+clientSocket);
                executor.execute(new SimpleHandler(clientSocket));
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private static final class SimpleHandler implements Runnable{
        private final Socket clientSocket;
        private SimpleHandler(Socket clientSocket){
            this.clientSocket = clientSocket;
        }
        public void run() {
            OutputStream out;
            try {
                out = clientSocket.getOutputStream();
                out.write(("Hi, this is SimpleHandler!"+count.incrementAndGet()
                    +" client had been handle!\r\n").getBytes(Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        new PlainOioServer().start(3003);
    }
    
}
