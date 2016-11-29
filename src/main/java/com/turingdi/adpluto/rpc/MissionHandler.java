package com.turingdi.adpluto.rpc;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.PromiseNotifier;

/*
 * Created by leibniz on 16-11-29.
 */
public class MissionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*            if (msg instanceof BidRequest) {
                //创建一个Promise
                DefaultPromise<BidResponse> promise = new DefaultPromise<>(ctx.executor()) ;
                //打包成Tanx任务对象并加入处理队列
                bidQueueStack.offer(new TanxBidMission((BidRequest)msg, promise));

                //增加监听器，等任务处理完成之后将BidResponse写入响应
                promise.addListener(new PromiseNotifier<BidResponse,DefaultPromise<BidResponse>>(){
                    @Override
                    public void operationComplete(DefaultPromise<BidResponse> future) throws Exception {
                        if(future.isSuccess()){
                            ctx.writeAndFlush(future.get());
                        }
                        ctx.channel().close();
                        //super.operationComplete(future);
                    }
                });
            }*/
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);//.addListener(ChannelFutureListener.CLOSE);
        super.channelReadComplete(ctx);
    }
}
