package com.ruoyi.web.controller.nio;

import com.ruoyi.web.controller.tool.QxTool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangzhe
 * @Date: 2019/8/20 9:52
 * @Description:
 */
//@Order(value = 1000)
@Component
public class NettyServer implements CommandLineRunner {

    private Integer port = 9876;
    private static ExecutorService singlePool = Executors.newSingleThreadExecutor();

    /*public NettyServer(Integer port) {
        this.port = port;
    }*/

    @Override
    public void run(String... args) {
        singlePool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(QxTool.getCurrDate()+"socket服务端开始启动...");

                EventLoopGroup pGroup = new NioEventLoopGroup(); //线程组：用来处理网络事件处理（接受客户端连接）
                EventLoopGroup cGroup = new NioEventLoopGroup(); //线程组：用来进行网络通讯读写
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(pGroup, cGroup)
                            .channel(NioServerSocketChannel.class) //注册服务端channel
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                protected void initChannel(SocketChannel sc) throws Exception {
                                    //sc.pipeline().addLast(new IdleStateHandler(20, 0, 0));
                                    sc.pipeline().addLast(new IdleStateHandler(40, 0, 0, TimeUnit.SECONDS));//超时就会触发userEventTriggered方法
                                    sc.pipeline().addLast(new ServerHandlerOne());   //服务端业务处理类
                                }
                            });

                    System.out.println(QxTool.getCurrDate()+"socket服务端启动成功...");
                    ChannelFuture cf = null;
                    cf = b.bind(port).sync();
                    cf.channel().closeFuture().sync();

                } catch (Exception e) {
                    System.out.println(QxTool.getCurrDate() + "socket服务端启动失败!");
                    pGroup.shutdownGracefully();
                    cGroup.shutdownGracefully();
                    e.printStackTrace();
                }
            }
        });
    }
}