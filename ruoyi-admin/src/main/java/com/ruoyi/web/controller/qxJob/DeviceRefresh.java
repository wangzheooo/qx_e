package com.ruoyi.web.controller.qxJob;

import com.ruoyi.system.domain.QxDeviceInfo;
import com.ruoyi.system.service.IQxDeviceInfoService;
import com.ruoyi.system.service.IQxDeviceRecordService;
import com.ruoyi.web.controller.nio.GlobalDevice;
import com.ruoyi.web.controller.tool.QxTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: wangzhe
 * @Date: 2020/4/2 9:51
 * @Description: 当服务器启动后的40S左右, 对数据库里所有设备进行网络状态刷新
 * 20200402,该功能彻底关闭,用定时器替代
 * 20200403,开启该功能,关闭定时器,因为更新心跳监测机制,不再用定时器刷新
 */


@Component
public class DeviceRefresh implements CommandLineRunner {

    //private static ExecutorService singlePool = Executors.newSingleThreadExecutor();
    @Autowired
    private IQxDeviceInfoService qxDeviceInfoService;

    @Autowired
    private IQxDeviceRecordService qxDeviceRecordService;

    @Override
    public void run(String... args) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                long time1 = System.currentTimeMillis();
                System.out.println(QxTool.getCurrDate() + "设备进行网络状态刷新开始");
                //System.out.println(qxDeviceInfoService);
                QxDeviceInfo qxDeviceInfo = new QxDeviceInfo();
                qxDeviceInfo.setNetworkStatus("001");
                List<QxDeviceInfo> list = qxDeviceInfoService.selectQxDeviceInfoList(qxDeviceInfo);
                //System.out.println(list.size());
                String channelId = "";
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        qxDeviceInfo = list.get(i);
                        //System.out.println(GlobalDevice.clients.containsKey(qxDeviceInfo.getDeviceId()));
//                        GlobalDevice.clients.containsKey(qxDeviceInfo.getDeviceId());
                        if (!GlobalDevice.clients.containsKey(qxDeviceInfo.getDeviceId())) {
                            channelId = qxDeviceInfo.getChannelId();
                            //System.out.println("1111");
                            qxDeviceInfo.setChannelId("------");
                            qxDeviceInfo.setNetworkStatus("002");
//                            qxDeviceInfo.setIsEnable("001");
                            qxDeviceInfo.setModifyDate(new Date());
                            qxDeviceInfo.setModifyUser("设备断开连接,网络状态重置");
                            int j = qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo);
                            if (!(j > 0)) {
                                System.out.println(QxTool.getCurrDate() + "发现数据库设备假连接,但数据库更新数据失败,请手动把该数据改成离线,设备号:" + qxDeviceInfo.getDeviceId() + ",channelId:" + channelId +
                                        "(数据库有该设备信息, 但无法更新)");
                            } else {
                                System.out.println(QxTool.getCurrDate() + "发现数据库设备假连接,已自动更新,设备号:" + qxDeviceInfo.getDeviceId() + ",channelId:" + channelId);
                                qxDeviceRecordService.insertQxDeviceRecordX(qxDeviceInfo.getDeviceId(), "设备断开");
                            }
                        }
                    }
                }
                long time2 = System.currentTimeMillis();
                System.out.println(QxTool.getCurrDate() + "设备进行网络状态刷新结束");
                System.out.println(QxTool.getCurrDate() + "设备网络状态刷新" + ",耗时:" + (time2 - time1) + "ms");
            }
        }, 1000 * 40);
    }
}