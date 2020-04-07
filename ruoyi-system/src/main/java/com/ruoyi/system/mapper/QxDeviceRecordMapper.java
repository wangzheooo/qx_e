package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.QxDeviceRecord;

/**
 * 设备连接记录Mapper接口
 * 
 * @author wangzhe
 * @date 2020-04-07
 */
public interface QxDeviceRecordMapper 
{
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
     * 修改设备连接记录
     * 
     * @param qxDeviceRecord 设备连接记录
     * @return 结果
     */
    public int updateQxDeviceRecord(QxDeviceRecord qxDeviceRecord);

    /**
     * 删除设备连接记录
     * 
     * @param seq 设备连接记录ID
     * @return 结果
     */
    public int deleteQxDeviceRecordById(Integer seq);

    /**
     * 批量删除设备连接记录
     * 
     * @param seqs 需要删除的数据ID
     * @return 结果
     */
    public int deleteQxDeviceRecordByIds(String[] seqs);
}
