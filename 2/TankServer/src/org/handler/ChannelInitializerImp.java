package org.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class ChannelInitializerImp extends ChannelInitializer<SocketChannel> {
    private static int i=0;

    public void setI(int i) {
        this.i=i;
    }
    public int getI(){
        return i;
    }


    private static int j=0;

    public void setJ(int j) {
        this.j=j;
    }
    public int getJ(){
        return j;
    }

    @Override
    protected void initChannel(SocketChannel sc)  {

        sc.pipeline().addLast(new HttpRequestDecoder());//inbound
        sc.pipeline().addLast(new HttpObjectAggregator(65536));//inbound
        sc.pipeline().addLast(new HttpResponseEncoder());//outbound

        sc.pipeline().addLast(new WelcomePageHandler());   //返回首页
        sc.pipeline().addLast(new RegisterPageHandler());        //返回注册页面
//        sc.pipeline().addLast(new TestHandler());        //
        sc.pipeline().addLast(new LoginHandler());             //处理登录请求
        sc.pipeline().addLast(new HallPageHandler());           //返回大厅页面
        sc.pipeline().addLast(new WaitPageHandler(i));           //匹配双人
        sc.pipeline().addLast(new CodePageHandler(i));            //
        sc.pipeline().addLast(new SendCodeHandler());
        sc.pipeline().addLast(new StartBattleHandler(j));           //战斗匹配
        sc.pipeline().addLast(new BattleHandler());
//        sc.pipeline().addLast(new RoomPageHandler());      //返回选择房间的页面
//        sc.pipeline().addLast(new CreateHouseHandler());          //创建房间落库
        sc.pipeline().addLast(new RegisterHandler());          //处理注册请求
//        sc.pipeline().addLast(new HouseHandler());//处理房间请求



    }
}
