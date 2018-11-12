package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.EventExecutorGroup;
import org.dao.HouseDao;

import java.io.UnsupportedEncodingException;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HouseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        String uri = fhr.uri();
        FullHttpResponse response=null;
        HouseDao houseDao =new HouseDao();
        String json = ""+houseDao.search();
        System.out.println(json);
        if (uri.substring(1).equals("house")){
            try {
                response = new DefaultFullHttpResponse(HTTP_1_1,
                        OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().setInt(CONTENT_LENGTH,
                        response.content().readableBytes());
                System.out.println("获取请求：\n"+msg+"\n");
                ctx.writeAndFlush(response);
                response.release();
//            System.out.println("写出响应");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
}
