package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.QxDeviceRecord;

/**
 * 设备连接记录Service接口
 *
 * @author wangzhe
 * @date 2020-04-07
 */
public interface IQxDeviceRecordService {
    /**
     * 查询设备连接记录
     *
     * @param seq 设备连接记录ID
     * @return 设备连接记录
     */
    public QxDeviceRecord selectQxDeviceRecordById(Integer seq);

    /**
     * 查询设备连接记录列表
     *
     * @param qxDeviceRecord 设备连接记录
     * @return 设备连接记录集合
     */
    public List<QxDeviceRecord> selectQxDeviceRecordList(QxDeviceRecord qxDeviceRecord);

    /**
     * 新增设备连接记录
     *
     * @param qxDeviceRecord 设备连接记录
     * @return 结果
     */
    public int insertQxDeviceRecord(QxDeviceRecord qxDeviceRecord);

    /**
     * 新增设备连接记录X
     *
     * @param deviceId 设备编号,type 设备状态
     * @return 结果
     */
    public int insertQxDeviceRecordX(String deviceId, String type);

    /**
     * 修改设备连接记录
     *
     * @param qxDeviceRecord 设备连接记录
     * @return 结果
     */
    public int updateQxDeviceRecord(QxDeviceRecord qxDeviceRecord);

    /**
     * 批量删除设备连接记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQxDeviceRecordByIds(String ids);

    /**
     * 删除设备连接记录信息
     *
     * @param seq 设备连接记录ID
     * @return 结果
     */
    public int deleteQxDeviceRecordById(Integer seq);
}
