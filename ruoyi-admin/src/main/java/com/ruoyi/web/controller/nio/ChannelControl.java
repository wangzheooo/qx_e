package com.ruoyi.web.controller.nio;

import com.ruoyi.web.controller.tool.QxTool;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.*;

/**
 * @Auther: auther
 * @Date: 2019/8/20 11:31
 * @Description:
 */
@RestController
public class ChannelControl {

    @GetMapping("/qx/getChannel")
    public String getChannel() {
        List list = new ArrayList();

        ChannelGroup channels = GlobalDevice.channelGlobalDevice;

        for (Channel channel : channels) {
            System.out.println(channel.toString());
            channel.writeAndFlush(Unpooled.copiedBuffer("999999999999999999999999999999".getBytes()));
        }

        //return new Gson().toJson(channels);
        return channels.toString();
    }

    @GetMapping("/qx/getChannelX")
    public String getChannelX() {
        int i = 0;
        for (Map.Entry<String, ChannelHandlerContext> entry : GlobalDevice.clients.entrySet()) {
            /*String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey+":"+mapValue);*/
            if (entry.getValue().channel().isActive()) {
                i++;
            }
        }
        return "" + i;
    }

    @GetMapping("/qx/isOnline")
    public boolean isOnline(String deviceId) {
        boolean flag = false;
        String returnIsOnlineInfo = "";//用来记录回复信息
        int returnIsOnlineInfoLastTime = 0;//用来记录回复信息的时间

        //先看看设备在不在线,当前先用map做判断
        if (!GlobalDevice.clients.containsKey(deviceId)) {
            //如果没有设备,直接退出
            System.out.println("检测不到设备,不能发送消息");
            return false;
        }

        //先看看设备有没有历史信息
        if (GlobalDevice.returnIsOnlineInfo.containsKey(deviceId)) {
            //有历史回复信息就记录下来
            returnIsOnlineInfo = GlobalDevice.returnIsOnlineInfo.get(deviceId);
            returnIsOnlineInfoLastTime = Integer.parseInt(GlobalDevice.returnIsOnlineInfoLastTime.get(deviceId));
        } else {
            //没有历史信息就造个初始值
            GlobalDevice.returnIsOnlineInfo.put(deviceId, returnIsOnlineInfo);
            GlobalDevice.returnIsOnlineInfoLastTime.put(deviceId, "" + returnIsOnlineInfoLastTime);
        }

        //获取channel
        ChannelHandlerContext ctx = GlobalDevice.clients.get(deviceId);
        //发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("isOnline".getBytes()));

        //循环次数
        int i = 1;
        //循环时间
        int whileTime = 5;
        //循环开始时间
        int whileStartTime = 0;
        //开始循环
        while (true) {
            if (i == 1) {
                System.out.println(QxTool.getCurrDate() + "isOnline开始循环");
                whileStartTime = Integer.parseInt(QxTool.timeStamp());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //如果历史值发送的最终时间发生变化就退出循环
            if (!GlobalDevice.returnIsOnlineInfoLastTime.get(deviceId).equals("" + returnIsOnlineInfoLastTime)) {
                flag = true;
                System.out.println(QxTool.getCurrDate() + "isOnline循环次数:" + i);
                break;
            }
            if ((Integer.parseInt(QxTool.timeStamp()) - whileStartTime) > whileTime) {
                //如果循环时间超时就直接退出
                System.out.println(QxTool.getCurrDate() + "isOnline循环超时,循环次数:" + i);
                return false;
            }
            i++;
        }

        if (flag) {
            //重新赋值
            returnIsOnlineInfo = GlobalDevice.returnIsOnlineInfo.get(deviceId);
            returnIsOnlineInfoLastTime = Integer.parseInt(GlobalDevice.returnIsOnlineInfoLastTime.get(deviceId));

            System.out.println("returnInfo:" + returnIsOnlineInfo);
            System.out.println("returnInfoLastTime:" + QxTool.stampToTime(returnIsOnlineInfoLastTime));
            System.out.println(QxTool.getCurrDate() + "设备在线,设备号:" + deviceId);
        } else {
            System.out.println(QxTool.getCurrDate() + "设备不在线,设备号:" + deviceId);
        }

        return flag;
    }

    /*@GetMapping("/qx/getQueryConnectedDevice")
    public String getQueryConnectedDevice() {
        int i = 0;
        ChannelGroup channels = GlobalDevice.channels;

        for (Channel channel : channels) {
            if (channel.isActive()) {
                i++;
            }
        }
        return "" + i;
    }*/
}
