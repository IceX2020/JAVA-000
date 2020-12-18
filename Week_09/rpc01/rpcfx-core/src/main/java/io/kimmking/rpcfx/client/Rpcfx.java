package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T> T create(final Class<T> serviceClass, final String url) {

        // 0. 替换动态代理 -> AOP
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));

    }

    public static class RpcfxInvocationHandler implements InvocationHandler {

        public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");
        public static String respJson = null;
        private final Class<?> serviceClass;
        private final String url;
        private final String host = "www.baidu.com";
        private final String port = "80";
        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);

            RpcfxResponse response = post(request, url);

            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException

            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(RpcfxRequest req, String url) throws IOException, InterruptedException {
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: "+reqJson);

            // 1.可以复用client
            // 2.尝试使用httpclient或者netty client
//            OkHttpClient client = new OkHttpClient();
//            final Request request = new Request.Builder()
//                    .url(url)
//                    .post(RequestBody.create(JSONTYPE, reqJson))
//                    .build();
//            String respJson = client.newCall(request).execute().body().string();

            //使用netty实现
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.remoteAddress("www.baidu.com",80);
                NettyClientHandler nettyhandle = new NettyClientHandler(reqJson);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(nettyhandle);
                    }
                });
                URI uri = URI.create("http://www.baidu.com/");
                System.out.println("客户端连接服务器，开始发送数据……");
                byte[] req1 = reqJson.getBytes("UTF-8");//消息
                ByteBuf firstMessage = Unpooled.copiedBuffer(req1);//发送类
                FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,uri.toASCIIString(),firstMessage);

                ChannelFuture f = bootstrap.connect().sync();
                f.channel().write(httpRequest);
                f.channel().flush();
                System.out.println("客户端连接服务器，开始发送数据……完成");
                f.channel().closeFuture().sync();
            } finally {
                workerGroup.shutdownGracefully();
            }
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }

    }
}
