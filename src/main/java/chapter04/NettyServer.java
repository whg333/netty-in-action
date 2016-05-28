package chapter04;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

public class NettyServer {
    
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private static final AtomicInteger count = new AtomicInteger();

    public void start(boolean isNio, int port){
        EventLoopGroup group = isNio ? new NioEventLoopGroup() : new OioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
             .channel(isNio ? NioServerSocketChannel.class : OioServerSocketChannel.class)
             .localAddress(new InetSocketAddress(port))
             .childHandler(new ChannelInitializer<SocketChannel>(){
                 @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            logger.info("Acceped connection from "+ctx.channel());
                            ByteBuf buf = Unpooled.unreleasableBuffer(
                                    Unpooled.copiedBuffer(("Hi, this is SimpleHandler!"+count.incrementAndGet()
                                        +" client had been handle!\r\n").getBytes(Charset.forName("UTF-8"))));
                            ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                        }
                    });
                }
             });
            
            ChannelFuture f = b.bind().sync();
            logger.info(NettyServer.class.getName()+" started and listen on "+b);
            f.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        boolean isNio = true;
        new NettyServer().start(isNio, 3005);
    }
    
}
