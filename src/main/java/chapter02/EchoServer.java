package chapter02;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);
    
    private final int port;
    
    public EchoServer(int port) {
        this.port = port;
    }
    
    public void start() throws InterruptedException{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
             .channel(NioServerSocketChannel.class)
             .localAddress(new InetSocketAddress(port))
             .childHandler(new ChannelInitializer<SocketChannel>(){
                 @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new EchoServerHandler());
                }
             });
            
            ChannelFuture f = b.bind().sync();
            logger.info(EchoServerHandler.class.getName()+" started and listen on "+f.channel().localAddress());
            f.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully().sync();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        int port = 3002;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        new EchoServer(port).start();
    }
    
}
