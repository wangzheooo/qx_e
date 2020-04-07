package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.QxDeviceInfoMapper;
import com.ruoyi.system.domain.QxDeviceInfo;
import com.ruoyi.system.service.IQxDeviceInfoService;
import com.ruoyi.common.core.text.Convert;

/**
 * 售货机管理Service业务层处理
 *
 * @author wangzhe
 * @date 2020-04-01
 */
@Service
public class QxDeviceInfoServiceImpl implements IQxDeviceInfoService {

    @Autowired
    private QxDeviceInfoMapper qxDeviceInfoMapper;

    /*@Bean
    public QxDeviceInfoMapper qxDeviceInfoMapper() {
        return new QxDeviceInfoMapper();
    }*/
    /**
     * 查询售货机管理
     *
     * @param seq 售货机管理ID
     * @return 售货机管理
     */
    @Override
    public QxDeviceInfo selectQxDeviceInfoById(Integer seq) {
        return qxDeviceInfoMapper.selectQxDeviceInfoById(seq);
    }

    /**
     * 查询售货机管理列表
     *
     * @param qxDeviceInfo 售货机管理
     * @return 售货机管理
     */
    @Override
    public List<QxDeviceInfo> selectQxDeviceInfoList(QxDeviceInfo qxDeviceInfo) {
        return qxDeviceInfoMapper.selectQxDeviceInfoList(qxDeviceInfo);
    }

    /**
     * 查询售货机管理列表,售货机首次心跳,查询表内是否有数据,无则添加,有则更新
     *
     * @param qxDeviceInfo 售货机管理
     * @return 售货机管理
     */
    @Override
    public List<QxDeviceInfo> selectQxDeviceInfoListFirstEnable(QxDeviceInfo qxDeviceInfo) {
        //System.out.println(qxDeviceInfoMapper);
        List<QxDeviceInfo> list = qxDeviceInfoMapper.selectQxDeviceInfoListFirstEnable(qxDeviceInfo);
        return list;
    }

    /**
     * 新增售货机管理
     *
     * @param qxDeviceInfo 售货机管理
     * @return 结果
     */
    @Override
    public int insertQxDeviceInfo(QxDeviceInfo qxDeviceInfo) {
        return qxDeviceInfoMapper.insertQxDeviceInfo(qxDeviceInfo);
    }

    /**
     * 修改售货机管理
     *
     * @param qxDeviceInfo 售货机管理
     * @return 结果
     */
    @Override
    public int updateQxDeviceInfo(QxDeviceInfo qxDeviceInfo) {
        return qxDeviceInfoMapper.updateQxDeviceInfo(qxDeviceInfo);
    }

    /**
     * 删除售货机管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQxDeviceInfoByIds(String ids) {
        return qxDeviceInfoMapper.deleteQxDeviceInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除售货机管理信息
     *
     * @param seq 售货机管理ID
     * @return 结果
     */
    @Override
    public int deleteQxDeviceInfoById(Integer seq) {
        return qxDeviceInfoMapper.deleteQxDeviceInfoById(seq);
    }
}
