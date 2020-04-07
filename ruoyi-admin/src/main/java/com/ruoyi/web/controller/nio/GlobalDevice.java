package com.ruoyi.web.controller.nio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: wangzhe
 * @Date: 2019/8/20 11:22
 * @Description:
 */
public class GlobalDevice {
    //保存全局的  连接上服务器的客户
    public static ChannelGroup channelGlobalDevice = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);



    //socket连接信息<设备15位编码,channel地址>
    public static Map<String, ChannelHandlerContext> clients = new LinkedHashMap<>();

    //channelID与设备编码绑定<channelId,设备15位编码>
    public static Map<String, String> clientsId = new HashMap<>();

    //记录心跳时间<设备15位编码,时间戳>,时间戳精确到秒
    public static Map<String, String> deviceLastTime = new HashMap<>();



    //记录设备isOnline信息<设备15位编码,返回信息>
    public static Map<String, String> returnIsOnlineInfo = new HashMap<>();

    //记录设备isOnline返回值时间<设备15位编码,时间戳>
    public static Map<String, String> returnIsOnlineInfoLastTime = new HashMap<>();

    //记录设备最新返回值信息<设备15位编码,返回信息>
    public static Map<String, String> returnInfo = new HashMap<>();

    //记录返回值时间<设备15位编码,时间戳>
    public static Map<String, String> returnInfoLastTime = new HashMap<>();

}
