package com.turingdi.adpluto.rpc;


import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.utils.Log4jUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class JsonDecoder extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);//.addListener(ChannelFutureListener.CLOSE);
		super.channelReadComplete(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		if(!req.decoderResult().isSuccess()){
			return;
		}
		int readableSize = req.content().readableBytes();
		if(readableSize > 0){
			byte[] in = new byte[readableSize];
			req.content().readBytes(in);
			MissionConfig bidreq = MissionConfig.parseFrom(in);
			ctx.fireChannelRead(bidreq);
		}
		//super.channelRead(ctx, req);
	}

	/* (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Log4jUtils.getLogger().error("抛出异常", cause);
		super.exceptionCaught(ctx, cause);
	}
}
