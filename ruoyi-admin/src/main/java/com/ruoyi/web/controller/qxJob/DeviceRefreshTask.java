package com.ruoyi.web.controller.qxJob;

import com.ruoyi.system.domain.QxDeviceInfo;
import com.ruoyi.system.service.IQxDeviceInfoService;
import com.ruoyi.web.controller.nio.GlobalDevice;
import com.ruoyi.web.controller.tool.QxTool;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangzhe
 * @Date: 2020/4/2 14:27
 * @Description:设备网络状态刷新,每40秒执行一次,每次离线60秒内没有心跳的设备
 */
@Component("deviceRefreshTask")
public class DeviceRefreshTask {

    @Autowired
    private IQxDeviceInfoService qxDeviceInfoService;

    public void refreshTask() {
        long time1 = System.currentTimeMillis();
        System.out.println(QxTool.getCurrDate() + "设备网络状态刷新开始");
        QxDeviceInfo qxDeviceInfo = new QxDeviceInfo();
        List<QxDeviceInfo> list = new ArrayList<>();
        String channelId = "";
        String deviceId = "";
        Long deviceLastTime;

        //更新map里设备的网络状态,60秒内无心跳的设备就关闭
        for (Map.Entry<String, String> entry : GlobalDevice.deviceLastTime.entrySet()) {
            /*String mapKey = entry.getKey();//设备编号
            String mapValue = entry.getValue();//最后心跳时间
            System.out.println(mapKey+":"+mapValue);*/
            deviceLastTime = Long.parseLong(entry.getValue());
            deviceId = entry.getKey();
            if ((Long.parseLong(QxTool.timeStamp()) - deviceLastTime) > 60) {
                //60秒内没有发送过心跳,就让设备断开连接
                qxDeviceInfo.setDeviceId(deviceId);
                list = qxDeviceInfoService.selectQxDeviceInfoListFirstEnable(qxDeviceInfo);

                //channelId = qxDeviceInfo.getChannelId();
                channelId = "" + GlobalDevice.clients.get(deviceId).channel().id();
                if (list.size() > 0) {
                    qxDeviceInfo = list.get(0);

                    qxDeviceInfo.setChannelId("------");
                    qxDeviceInfo.setNetworkStatus("002");
//                qxDeviceInfo.setIsEnable("001");
                    qxDeviceInfo.setModifyDate(new Date());
                    qxDeviceInfo.setModifyUser("设备断开连接,网络状态刷新");
                    int i = qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo);
                    if (!(i > 0)) {
                        System.out.println(QxTool.getCurrDate() + "网络状态刷新失败,设备号:" + deviceId + ",channelId:" + channelId + "(数据库有该设备信息,但无法更新)");
                    } else {
                        System.out.println(QxTool.getCurrDate() + "网络状态刷新为离线,设备号:" + deviceId + ",channelId:" + channelId);
                        if (GlobalDevice.clients.containsKey(deviceId)) {
                            GlobalDevice.clients.get(deviceId).close();
                            GlobalDevice.clients.remove(deviceId);
                        }
                        if (GlobalDevice.clientsId.containsKey(channelId)) {
                            GlobalDevice.clientsId.remove(channelId);
                        }
                        if (GlobalDevice.deviceLastTime.containsKey(deviceId)) {
                            GlobalDevice.deviceLastTime.remove(deviceId);
                        }
                    }
                } else {
                    System.out.println(QxTool.getCurrDate() + "网络状态刷新失败,设备号:" + deviceId + ",channelId:" + channelId + "(数据库没有该设备信息,无法更新,但是map里的心跳时间超过60S)");
                }
            }
        }

        //更新数据库里设备的网络状态,把在线设备与map作比较,map里没有的设备,网络状态更新为离线
        qxDeviceInfo = new QxDeviceInfo();
        qxDeviceInfo.setNetworkStatus("001");
        list = qxDeviceInfoService.selectQxDeviceInfoList(qxDeviceInfo);
        //System.out.println(list.size());
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                qxDeviceInfo = list.get(i);
                channelId = qxDeviceInfo.getChannelId();
                //System.out.println(GlobalDevice.clients.containsKey(qxDeviceInfo.getDeviceId()));
//                        GlobalDevice.clients.containsKey(qxDeviceInfo.getDeviceId());
                if (!GlobalDevice.clients.containsKey(qxDeviceInfo.getDeviceId())) {
                    //System.out.println("1111");
                    qxDeviceInfo.setChannelId("------");
                    qxDeviceInfo.setNetworkStatus("002");
//                            qxDeviceInfo.setIsEnable("001");
                    qxDeviceInfo.setModifyDate(new Date());
                    qxDeviceInfo.setModifyUser("设备断开连接,网络状态重置");
                    int j = qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo);
                    if (!(j > 0)) {
                        System.out.println(QxTool.getCurrDate() + "网络状态重置失败,设备号:" + qxDeviceInfo.getDeviceId() + ",channelId:" + channelId + "(数据库有该设备信息,但无法更新)");
                    } else {
                        System.out.println(QxTool.getCurrDate() + "网络状态重置为离线,设备号:" + qxDeviceInfo.getDeviceId() + ",channelId:" + channelId);
                    }
                }
            }
        }

        //刷新channelId,把不发心跳的设备给断开
        /*ChannelGroup channels = GlobalDevice.channelGlobalDevice;

        for (Channel channel : channels) {
            if (!GlobalDevice.clientsId.containsKey("" + channel.id())) {
                channel.close();
                System.out.println(QxTool.getCurrDate() + "有设备不发心跳,已经强制断开!channelId:" + channel.id());
            }
        }*/
        long time2 = System.currentTimeMillis();
        System.out.println(QxTool.getCurrDate() + "设备网络状态刷新结束");
        System.out.println(QxTool.getCurrDate() + "设备网络状态刷新" + ",耗时:" + (time2 - time1) + "ms");
    }
}