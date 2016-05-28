package chapter03;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainNioServer {

    private static final Logger logger = LoggerFactory.getLogger(PlainNioServer.class);
    private static final AtomicInteger count = new AtomicInteger();
    
    public void start(int port){
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(true);
            ServerSocket serverSocket = serverChannel.socket();
            serverSocket.bind(new InetSocketAddress(port));
            logger.info(PlainNioServer.class.getName()+" started and listen on "+serverSocket);
            
            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            final ByteBuffer msg = ByteBuffer.wrap(("Hi, this is SimpleHandler!"
                    +count.incrementAndGet()+" client had been handle!\r\n").getBytes(Charset.forName("UTF-8")));
            while(true){
                selector.select();
                
                Iterator<SelectionKey> keyIt = selector.selectedKeys().iterator();
                while(keyIt.hasNext()){
                    SelectionKey key = keyIt.next();
                    keyIt.remove();
                    try{
                        if(key.isAcceptable()){
                            SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
                            clientChannel.configureBlocking(true);
                            clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, msg.duplicate());
                            logger.info("Acceped connection from "+clientChannel.socket());
                        }
                        if(key.isWritable()){
                            SocketChannel clientChannel = (SocketChannel)key.channel();
                            ByteBuffer buffer = (ByteBuffer)key.attachment();
                            while(buffer.hasRemaining()){
                                if(clientChannel.write(buffer) == 0){
                                    break;
                                }
                            }
                            clientChannel.close();
                        }
                    }catch(IOException e){
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new PlainOioServer().start(3004);
    }
    
}
