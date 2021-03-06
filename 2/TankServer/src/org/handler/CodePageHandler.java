package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class CodePageHandler extends ChannelInboundHandlerAdapter {
    private int i;
    public CodePageHandler(int i) {
        this.i=i;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChannelInitializerImp ch=new ChannelInitializerImp();
        FullHttpRequest fhr = (FullHttpRequest) msg;
        String uri = fhr.uri();
        FullHttpResponse response = null;
        Charset charset = Charset.forName("utf-8");
        String json= "{\"status\":\"0\"}";
        if (uri.equals("/start")) {
            if (ch.getI()>=2){
                json = "{\"status\":\"1\"}";
                System.out.println("json----------------->"+1);
            }
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
        }
        else if (uri.equals("/assemble_tank.html")) {
            String html = "";
            FileInputStream fin = new FileInputStream("/home/pp/IdeaProjects/TankServer/src/org/webPage/new_tank/html/assemble_tank.html");
            FileChannel fc = fin.getChannel();
            ByteBuffer bf = ByteBuffer.allocate(2048);
            while ((fc.read(bf)) != -1) {
                bf.flip();
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer cb = decoder.decode(bf);
                bf.clear();
                html += String.valueOf(cb);
            }
//            System.out.println("uri====>"+uri);
            response = new DefaultFullHttpResponse(HTTP_1_1,
                    OK, Unpooled.wrappedBuffer(html.getBytes()));
            response.headers().set(CONTENT_TYPE, "text/html");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
//            response.release();
            System.out.println("assemble写出响应");
        }
        else if (uri.contains("assemble")) {
                String text = "";
                byte[] imgbyte = null;
                if (uri.contains("jpeg")) {
//                System.out.println("uri====>"+uri);
                    FileInputStream fileInputStream = new FileInputStream("/home/pp/IdeaProjects/TankServer/src/org/webPage/new_tank" + uri);
                    int img_size = fileInputStream.available();
                    imgbyte = new byte[img_size];
                    fileInputStream.read(imgbyte);
                    HttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(imgbyte));
                    res.headers().set(CONTENT_TYPE, "image/*");
                    res.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) res).content().readableBytes());
                    ctx.writeAndFlush(res);
//                    ((DefaultFullHttpResponse) res).release();
//                    ((DefaultFullHttpResponse) res).retain();
                }
                else {
                    String path="/home/pp/IdeaProjects/TankServer/src/org/webPage/new_tank" + uri;
                    if (fhr.uri().contains("png")){
                        FileInputStream fileInputStream = new FileInputStream("/home/pp/IdeaProjects/TankServer/src/org/webPage/new_tank" + uri);
                            int img_size = fileInputStream.available();
                            imgbyte = new byte[img_size];
                            fileInputStream.read(imgbyte);
                            HttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(imgbyte));
                            res.headers().set(CONTENT_TYPE, "image/png");
                            res.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) res).content().readableBytes());
                            ctx.writeAndFlush(res);
                    }else {
                        FileInputStream inputStream = new FileInputStream(path);
                        File f = new File(path);
                        System.out.println(uri + "===========================");
                        System.out.println("log----------->" + f.exists());
                        System.out.println("\n" + f.length() + "\n");

                        FileChannel fileChannel = inputStream.getChannel();
                        ByteBuffer buffer = ByteBuffer.allocateDirect(102400);
                        while ((fileChannel.read(buffer)) != -1) {
                            buffer.flip();
                            CharsetDecoder decoder = charset.newDecoder();
                            CharBuffer cb = decoder.decode(buffer);
                            buffer.clear();
                            text += String.valueOf(cb);
                        }
                        HttpResponse httpresponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(text.getBytes("utf-8")));
                        httpresponse.headers().set(CONTENT_TYPE, "text/html,application/x-javascript,image/gif; charset=utf-8");
                        httpresponse.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) httpresponse).content().readableBytes());
                        ctx.writeAndFlush(httpresponse);
//                        ((DefaultFullHttpResponse) httpresponse).release();
                    }
                }
        }
        else {
            ctx.fireChannelRead(msg);
        }

    }
}
