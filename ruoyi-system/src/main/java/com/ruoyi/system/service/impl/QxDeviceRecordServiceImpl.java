package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.QxDeviceRecordMapper;
import com.ruoyi.system.domain.QxDeviceRecord;
import com.ruoyi.system.service.IQxDeviceRecordService;
import com.ruoyi.common.core.text.Convert;

/**
 * 设备连接记录Service业务层处理
 *
 * @author wangzhe
 * @date 2020-04-07
 */
@Service
public class QxDeviceRecordServiceImpl implements IQxDeviceRecordService {
    @Autowired
    private QxDeviceRecordMapper qxDeviceRecordMapper;

    /**
     * 查询设备连接记录
     *
     * @param seq 设备连接记录ID
     * @return 设备连接记录
     */
    @Override
    public QxDeviceRecord selectQxDeviceRecordById(Integer seq) {
        return qxDeviceRecordMapper.selectQxDeviceRecordById(seq);
    }

    /**
     * 查询设备连接记录列表
     *
     * @param qxDeviceRecord 设备连接记录
     * @return 设备连接记录
     */
    @Override
    public List<QxDeviceRecord> selectQxDeviceRecordList(QxDeviceRecord qxDeviceRecord) {
        return qxDeviceRecordMapper.selectQxDeviceRecordList(qxDeviceRecord);
    }

    /**
     * 新增设备连接记录
     *
     * @param qxDeviceRecord 设备连接记录
     * @return 结果
     */
    @Override
    public int insertQxDeviceRecord(QxDeviceRecord qxDeviceRecord) {
        return qxDeviceRecordMapper.insertQxDeviceRecord(qxDeviceRecord);
    }

    /**
     * 新增设备连接记录X
     *
     * @param deviceId 设备编号,type 设备状态
     * @return 结果
     */
    @Override
    public int insertQxDeviceRecordX(String deviceId, String type) {
        QxDeviceRecord qxDeviceRecord = new QxDeviceRecord();
        qxDeviceRecord.setDeviceId(deviceId);
        qxDeviceRecord.setType(type);
        qxDeviceRecord.setCreateDate(new Date());
        return qxDeviceRecordMapper.insertQxDeviceRecord(qxDeviceRecord);
    }

    /**
     * 修改设备连接记录
     *
     * @param qxDeviceRecord 设备连接记录
     * @return 结果
     */
    @Override
    public int updateQxDeviceRecord(QxDeviceRecord qxDeviceRecord) {
        return qxDeviceRecordMapper.updateQxDeviceRecord(qxDeviceRecord);
    }

    /**
     * 删除设备连接记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQxDeviceRecordByIds(String ids) {
        return qxDeviceRecordMapper.deleteQxDeviceRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除设备连接记录信息
     *
     * @param seq 设备连接记录ID
     * @return 结果
     */
    @Override
    public int deleteQxDeviceRecordById(Integer seq) {
        return qxDeviceRecordMapper.deleteQxDeviceRecordById(seq);
    }
}
