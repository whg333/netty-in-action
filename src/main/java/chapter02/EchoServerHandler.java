package chapter02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Server "+ctx.channel().hashCode()+" connected!");
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        logger.info("Server "+ctx.channel().hashCode()+" received: "+in.toString(CharsetUtil.UTF_8));
        ctx.write(in);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Server "+ctx.channel().hashCode()+" disconnected!");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    
}