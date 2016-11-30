package com.turingdi.adpluto.rpc;

import com.turingdi.adpluto.cheat.MissionManager;
import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.MissionResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/*
 * Created by leibniz on 16-11-29.
 */
public class RPCMissionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MissionConfig) {
            MissionResponse result = MissionManager.getInstance().addOneMission((MissionConfig) msg);
            ctx.writeAndFlush(result);
            ctx.channel().close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);//.addListener(ChannelFutureListener.CLOSE);
        super.channelReadComplete(ctx);
    }
}
