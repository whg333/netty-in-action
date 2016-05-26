package chapter02;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

    private static final Logger logger = LoggerFactory.getLogger(EchoClient.class);
    
    private final String host;
    private final int port;
    
    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void start() throws InterruptedException{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .remoteAddress(new InetSocketAddress(host, port))
             .handler(new ChannelInitializer<SocketChannel>(){
                 @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new EchoClientHandler());
                }
             });
            
            ChannelFuture f = b.connect().sync();
            logger.info(EchoClient.class.getName()+" started and connect to "+f.channel().remoteAddress());
            f.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully().sync();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 3002;
        if(args.length > 1){
            host = args[0];
            port = Integer.parseInt(args[1]);
        }
        new EchoClient(host, port).start();
    }
    
}
