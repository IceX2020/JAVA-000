package io.kimmking.rpcfx.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class NettyClientHandler extends SimpleChannelInboundHandler<String>{
    private String reqJson;

    public NettyClientHandler(String reqJson) {
        this.reqJson = reqJson;
    }

    // 客户端连接服务器后被调用
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("客户端连接服务器，开始发送数据……");
//        byte[] req = reqJson.getBytes("UTF-8");//消息
//        ByteBuf firstMessage = Unpooled.copiedBuffer(req);//发送类
////        firstMessage.writeBytes(req);//发送
//        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/",firstMessage);
//        ctx.writeAndFlush(httpRequest);//flush
//        System.out.println("客户端连接服务器，开始发送数据……完成");
//    }

    // • 从服务器接收到数据后调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String response)
            throws Exception {
        System.out.println("client 读取server数据..");
        // 服务端返回消息后
        String msg = response.toString();

        System.out.println("服务端数据为 :" + msg);
        Rpcfx.RpcfxInvocationHandler.respJson = msg;
    }

    // • 发生异常时被调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("client exceptionCaught..");
        // 释放资源
        ctx.close();
    }
}