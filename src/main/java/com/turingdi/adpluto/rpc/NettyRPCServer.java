package com.turingdi.adpluto.rpc;

import com.turingdi.adpluto.entity.SystemConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;

/*
 * Created by leibniz on 16-11-29.
 */
public class NettyRPCServer {
    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(SystemConfig.getInstance().getServerPort()))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpResponseEncoder());
//                            pipeline.addLast(new TanxProtobufEncoder());

                            pipeline.addLast(new HttpRequestDecoder());
                            pipeline.addLast(new HttpObjectAggregator(4096));
//                            pipeline.addLast(new TanxProtobufDecoder());
//                            pipeline.addLast(new BusinessHandler(bidQueueStack));
                        }
                    });
            ChannelFuture cf = sbs.bind().sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                bossGroup.shutdownGracefully().sync();
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
