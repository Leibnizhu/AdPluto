package com.turingdi.adpluto.rpc;

import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.MissionResponse;
import com.turingdi.adpluto.cheat.Cheater;
import com.turingdi.adpluto.utils.CommonUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/*
 * Created by leibniz on 16-11-29.
 */
public class MissionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MissionConfig) {
            MissionConfig missionConfig = (MissionConfig)msg;
            switch(missionConfig.getMission().getType()){
                case 1:
                    newCheatMission(ctx, missionConfig);
                    break;
                case 2:
                    checkMissionStatus(ctx, missionConfig);
                    break;
                case 3:
                    forceBreakMission(ctx, missionConfig);
                    break;
                default:

            }
/*            //创建一个Promise
            DefaultPromise<BidResponse> promise = new DefaultPromise<>(ctx.executor());
            //打包成Tanx任务对象并加入处理队列
            bidQueueStack.offer(new TanxBidMission((BidRequest) msg, promise));

            //增加监听器，等任务处理完成之后将BidResponse写入响应
            promise.addListener(new PromiseNotifier<BidResponse, DefaultPromise<BidResponse>>() {
                @Override
                public void operationComplete(DefaultPromise<BidResponse> future) throws Exception {
                    if (future.isSuccess()) {
                        ctx.writeAndFlush(future.get());
                    }
                    ctx.channel().close();
                    //super.operationComplete(future);
                }
            });*/
        }
    }

    private void newCheatMission(ChannelHandlerContext ctx, MissionConfig missionConfig) throws InterruptedException {
        Cheater cheater = new Cheater(missionConfig);
        cheater.startCheater();
        String cheaterID = CommonUtils.getRandomID();
        ctx.writeAndFlush(new MissionResponse(cheaterID));
        ctx.channel().close();
    }

    private void checkMissionStatus(ChannelHandlerContext ctx, MissionConfig missionConfig) {
    }

    private void forceBreakMission(ChannelHandlerContext ctx, MissionConfig missionConfig) {
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);//.addListener(ChannelFutureListener.CLOSE);
        super.channelReadComplete(ctx);
    }
}
