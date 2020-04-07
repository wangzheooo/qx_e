package com.ruoyi.web.controller.nio;

import com.ruoyi.system.controller.QxDeviceInfoController;
import com.ruoyi.system.domain.QxDeviceInfo;
import com.ruoyi.system.mapper.QxDeviceInfoMapper;
import com.ruoyi.system.service.IQxDeviceInfoService;
import com.ruoyi.system.service.IQxDeviceRecordService;
import com.ruoyi.system.service.impl.QxDeviceInfoServiceImpl;
import com.ruoyi.web.controller.tool.QxTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wangzhe
 * @Date: 2019/8/20 10:15
 * @Description:
 */
@Controller
@Component
public class ServerHandlerOne extends ChannelHandlerAdapter {

    @Autowired
    private IQxDeviceInfoService qxDeviceInfoService;

    @Autowired
    private IQxDeviceRecordService qxDeviceRecordService;

    public static ServerHandlerOne serverHandlerOne;

    @PostConstruct
    public void init() {
        serverHandlerOne = this;

        serverHandlerOne.qxDeviceInfoService = this.qxDeviceInfoService;

        serverHandlerOne.qxDeviceRecordService = this.qxDeviceRecordService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(QxTool.getCurrDate() + "有设备连接,channelId:" + ctx.channel().id());
        GlobalDevice.channelGlobalDevice.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String msg_accept = new String(req, "utf-8");
            String channelId = "" + ctx.channel().id();
            if (msg_accept.indexOf("CR,") != -1) {
                //例:CR,868575027057605
                msg_accept = msg_accept.substring(msg_accept.indexOf("CR,") + 3, msg_accept.indexOf("CR,") + 18);
                ChannelHandlerContext chc = GlobalDevice.clients.get(msg_accept);
                if (chc == null) {
                    //String deviceId=GlobalDevice.clientsId.get(channelId);
                    if (GlobalDevice.clientsId.get(channelId) != null) {
                        System.out.println(QxTool.getCurrDate() + "设备更新失败,设备号:" + msg_accept + ",channelId:" + channelId + "(同一个设备cahnnelId检测到心跳数据不一样)");
                        return;
                    }
                    //GlobalDevice.channelGlobalDevice.clientsIds.add(ctx.channel());
                    GlobalDevice.clients.put(msg_accept, ctx);//记录设备号对应的channel地址
                    GlobalDevice.clientsId.put(channelId, msg_accept);//channelId和设备编号绑定
                    GlobalDevice.deviceLastTime.put(msg_accept, QxTool.timeStamp());//记录最后心跳时间

                    QxDeviceInfo qxDeviceInfo = new QxDeviceInfo();
                    qxDeviceInfo.setDeviceId(msg_accept);

                    //List<QxDeviceInfo> list=new ArrayList<>();

                    //System.out.println(serverHandlerOne.qxDeviceInfoService);
                    List<QxDeviceInfo> list = serverHandlerOne.qxDeviceInfoService.selectQxDeviceInfoListFirstEnable(qxDeviceInfo);

                    if (list.size() > 0) {
                        //有则更新
                        qxDeviceInfo = list.get(0);
                        qxDeviceInfo.setChannelId(channelId);
                        qxDeviceInfo.setNetworkStatus("001");
//                        qxDeviceInfo.setIsEnable("001");
                        qxDeviceInfo.setModifyDate(new Date());
                        qxDeviceInfo.setModifyUser("设备重新连接");
                        int i = serverHandlerOne.qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo);
                        if (!(i > 0)) {
                            System.out.println(QxTool.getCurrDate() + "设备更新失败,设备号:" + msg_accept + ",channelId:" + channelId);
                        } else {
                            System.out.println(QxTool.getCurrDate() + "设备连接成功,设备号:" + msg_accept + ",channelId:" + channelId);
                            serverHandlerOne.qxDeviceRecordService.insertQxDeviceRecordX(msg_accept, "设备连接");
                        }
                    } else {
                        //无则新增
                        qxDeviceInfo.setChannelId(channelId);
                        qxDeviceInfo.setNetworkStatus("001");
                        qxDeviceInfo.setIsEnable("002");
                        qxDeviceInfo.setCreateDate(new Date());
                        qxDeviceInfo.setCreateUser("设备首次连接");
                        int i = serverHandlerOne.qxDeviceInfoService.insertQxDeviceInfo(qxDeviceInfo);
                        if (!(i > 0)) {
                            System.out.println(QxTool.getCurrDate() + "设备首次连接失败,设备号:" + msg_accept + ",channelId:" + channelId);
                        } else {
                            System.out.println(QxTool.getCurrDate() + "设备首次连接成功,设备号:" + msg_accept + ",channelId:" + channelId);
                            serverHandlerOne.qxDeviceRecordService.insertQxDeviceRecordX(msg_accept, "设备连接");
                        }
                    }

                } else {
                    //如果内存里有对应的设备编号,就要判断一下channelId是否一样,一样就没问题,不一样就判断一下心跳时间,如果40秒内无心跳就替换,有心跳就说明有异常设备进行连接
                    //判断channelId是否和clients记录里的一样
                    String clientsChannelId = "" + chc.channel().id();//新
                    if (!clientsChannelId.equals(channelId)) {
                        //如果新连接的channelId和clients记录里的不一样,可能是另一个设备模仿已有的设备编码进行连接,也有可能是客户端断网后重新连接
                        //判断设备最后的心跳时间是不是在一分钟内
                        Long deviceLastTime = Long.parseLong(GlobalDevice.deviceLastTime.get(msg_accept));
                        if ((Long.parseLong(QxTool.timeStamp()) - deviceLastTime) > 40) {
                            //如果当前时间减去最后心跳时间,大于40秒,,,说明设备已经断开了,,就开始更新信息
                            GlobalDevice.clients.put(msg_accept, ctx);//记录设备号对应的channel地址
                            GlobalDevice.clientsId.remove(clientsChannelId);
                            GlobalDevice.clientsId.put(channelId, msg_accept);//channelId和设备编号绑定
                            GlobalDevice.deviceLastTime.put(msg_accept, QxTool.timeStamp());//记录最后心跳时间
                            QxDeviceInfo qxDeviceInfo = new QxDeviceInfo();
                            qxDeviceInfo.setDeviceId(msg_accept);
                            List<QxDeviceInfo> list = serverHandlerOne.qxDeviceInfoService.selectQxDeviceInfoListFirstEnable(qxDeviceInfo);

                            if (list.size() > 0) {
                                //更新
                                qxDeviceInfo = list.get(0);
                                qxDeviceInfo.setChannelId(channelId);
                                qxDeviceInfo.setNetworkStatus("001");
//                                qxDeviceInfo.setIsEnable("001");
                                qxDeviceInfo.setModifyDate(new Date());
                                qxDeviceInfo.setModifyUser("设备断网后重新连接");
                                int i = serverHandlerOne.qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo);
                                if (!(i > 0)) {
                                    System.out.println(QxTool.getCurrDate() + "设备断网后更新失败,设备号:" + msg_accept + ",新channelId:" + channelId + ",旧channelId:" + clientsChannelId);
                                } else {
                                    System.out.println(QxTool.getCurrDate() + "设备断网后连接成功,设备号:" + msg_accept + ",新channelId:" + channelId + ",旧channelId:" + clientsChannelId);
                                    serverHandlerOne.qxDeviceRecordService.insertQxDeviceRecordX(msg_accept, "设备连接");
                                }
                            }
                        } else {
                            System.out.println(QxTool.getCurrDate() + "有设备连接失败,设备号:" + msg_accept + ",channelId:" + channelId + "(1.请过60秒后再试,2.请检查是否有其他设备模拟已连接设备)");
                        }
                    } else {
                        //正常接收心跳
                        GlobalDevice.deviceLastTime.put(msg_accept, QxTool.timeStamp());//记录最后心跳时间
                        System.out.println(QxTool.getCurrDate() + "收到心跳,设备号:" + msg_accept + ",channelId:" + channelId);
                    }
                }
            } else if (msg_accept.indexOf("Online,") != -1) {
                //例:Online,868575027057605
                String deviceId = msg_accept.substring(7, 22);

                //先判断设备是否连接中

                GlobalDevice.returnIsOnlineInfo.put(deviceId, msg_accept);
                GlobalDevice.returnIsOnlineInfoLastTime.put(deviceId, QxTool.timeStamp());
//                System.out.println(QxTool.getCurrDate() + "设备确认在线,设备号:" + deviceId + ",channelId:" + channelId + ",内容:" + msg_accept);
            } else {
                System.out.println(QxTool.getCurrDate() + "数据错误!(内容:" + msg_accept + ")" + ",channelId:" + ctx.channel().id());
            }
            //处理完毕，关闭服务端
            //ctx.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("server: 读完了");
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {//超时事件
            IdleStateEvent idleEvent = (IdleStateEvent) evt;
            if (idleEvent.state() == IdleState.READER_IDLE) {//读
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /*@Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("toString:" + ctx.toString());
        System.out.println("name:" + ctx.name());
        System.out.println("channel:" + ctx.channel());
        System.out.println("channel().id():" + ctx.channel().id());
        System.out.println("channel().toString():" + ctx.channel().toString());
        try {
            //这里执行客户端断开连接后的操作
            String channelId = "" + ctx.channel().id();//获取断开连接的channelId

            if (!GlobalDevice.clientsId.containsKey(channelId)) {
                System.out.println(QxTool.getCurrDate() + "无设备编码的设备断开连接,channelId:" + channelId + "(1.心跳数据异常或无心跳信息,2.设备断网后连接,3.有不明设备连接服务器)");
                return;
            }

            String deviceId = GlobalDevice.clientsId.get(channelId);//获取断开连接的deviceId

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

            QxDeviceInfo qxDeviceInfo = new QxDeviceInfo();
            qxDeviceInfo.setDeviceId(deviceId);
            List<QxDeviceInfo> list = serverHandlerOne.qxDeviceInfoService.selectQxDeviceInfoListFirstEnable(qxDeviceInfo);

            if (list.size() > 0) {
                qxDeviceInfo = list.get(0);
                qxDeviceInfo.setChannelId("------");
                qxDeviceInfo.setNetworkStatus("003");
//                qxDeviceInfo.setIsEnable("001");
                qxDeviceInfo.setModifyDate(new Date());
                qxDeviceInfo.setModifyUser("设备断开连接");
                int i = serverHandlerOne.qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo);
                if (!(i > 0)) {
                    System.out.println(QxTool.getCurrDate() + "设备断开连接失败,设备号:" + deviceId + ",channelId:" + channelId + "(数据库有该设备信息,但无法更新)");
                } else {
                    System.out.println(QxTool.getCurrDate() + "设备断开连接,设备号:" + deviceId + ",channelId:" + channelId);
                }
            } else {
                System.out.println(QxTool.getCurrDate() + "设备断开连接失败,设备号:" + deviceId + ",channelId:" + channelId + "(数据库没有该设备信息,无法更新)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //channel失效处理,客户端下线或者强制退出等任何情况都触发这个方法
        //System.out.println(QxTool.getCurrDate() + "关闭这个不活跃通道！");
        String channelId = "" + ctx.channel().id();
        String deviceId = "";
        if (GlobalDevice.clientsId.containsKey(channelId)) {
            deviceId = GlobalDevice.clientsId.get(channelId);

            if (GlobalDevice.clients.containsKey(deviceId)) {
                GlobalDevice.clients.remove(deviceId);
            }
            if (GlobalDevice.clientsId.containsKey(channelId)) {
                GlobalDevice.clientsId.remove(channelId);
            }
            if (GlobalDevice.deviceLastTime.containsKey(deviceId)) {
                GlobalDevice.deviceLastTime.remove(deviceId);
            }

            QxDeviceInfo qxDeviceInfo = new QxDeviceInfo();
            qxDeviceInfo.setDeviceId(deviceId);
            List<QxDeviceInfo> list = serverHandlerOne.qxDeviceInfoService.selectQxDeviceInfoListFirstEnable(qxDeviceInfo);

            if (list.size() > 0) {
                qxDeviceInfo = list.get(0);
                qxDeviceInfo.setChannelId("------");
                qxDeviceInfo.setNetworkStatus("002");
                qxDeviceInfo.setModifyDate(new Date());
                qxDeviceInfo.setModifyUser("设备断开连接");
                int i = serverHandlerOne.qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo);
                if (i > 0) {
                    System.out.println(QxTool.getCurrDate() + "设备断开连接,设备号:" + deviceId + ",channelId:" + channelId);
                    serverHandlerOne.qxDeviceRecordService.insertQxDeviceRecordX(deviceId, "设备断开");
                } else {
                    System.out.println(QxTool.getCurrDate() + "设备断开连接失败,设备号:" + deviceId + ",channelId:" + channelId + "(数据库有该设备信息,但无法更新,请手动把网络状态改为离线)");
                }
            } else {
                System.out.println(QxTool.getCurrDate() + "设备断开连接失败,设备号:" + deviceId + ",channelId:" + channelId + "(数据库没有该设备信息,无法更新)");
            }

        } else {
            System.out.println(QxTool.getCurrDate() + "关闭没有心跳的通道" + ",channelId:" + channelId);
        }
        super.channelInactive(ctx);
    }

    /*@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(QxTool.getCurrDate() + "异常断开exceptionCaught,channelId:" + ctx.channel().id() + "Throwable:" + cause);
        super.exceptionCaught(ctx, cause);
        //ctx.close();
    }*/

}