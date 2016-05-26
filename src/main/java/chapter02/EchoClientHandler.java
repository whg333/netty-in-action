package chapter02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(EchoClientHandler.class); 
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client "+ctx.channel().hashCode()+" connected!");
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks!", CharsetUtil.UTF_8));
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        logger.info("Client "+ctx.channel().hashCode()+" received: "+msg.toString(CharsetUtil.UTF_8));
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client "+ctx.channel().hashCode()+" disconnected!");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
