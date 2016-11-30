package com.turingdi.adpluto.rpc;

import com.turingdi.adpluto.entity.MissionResponse;
import com.turingdi.adpluto.utils.Log4jUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class JsonEncoder extends ChannelOutboundHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof MissionResponse) {
			Log4jUtils.getLogger().debug(msg);
			byte[] out = ((MissionResponse)msg).getJsonBytes();
			ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(out)));
		}
		super.write(ctx, msg, promise);
	}
}
