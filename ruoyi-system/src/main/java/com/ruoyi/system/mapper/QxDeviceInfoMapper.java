package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.QxDeviceInfo;

/**
 * 售货机管理Mapper接口
 * 
 * @author wangzhe
 * @date 2020-04-01
 */
public interface QxDeviceInfoMapper 
{
    /**
     * 查询售货机管理
     * 
     * @param seq 售货机管理ID
     * @return 售货机管理
     */
    public QxDeviceInfo selectQxDeviceInfoById(Integer seq);

    /**
     * 查询售货机管理列表
     * 
     * @param qxDeviceInfo 售货机管理
     * @return 售货机管理集合
     */
    public List<QxDeviceInfo> selectQxDeviceInfoList(QxDeviceInfo qxDeviceInfo);

    /**
     * 查询售货机管理列表,售货机首次心跳,查询表内是否有数据,无则添加,有则更新
     *
     * @param qxDeviceInfo 售货机管理
     * @return 售货机管理集合
     */
    public List<QxDeviceInfo> selectQxDeviceInfoListFirstEnable(QxDeviceInfo qxDeviceInfo);

    /**
     * 新增售货机管理
     * 
     * @param qxDeviceInfo 售货机管理
     * @return 结果
     */
    public int insertQxDeviceInfo(QxDeviceInfo qxDeviceInfo);

    /**
     * 修改售货机管理
     * 
     * @param qxDeviceInfo 售货机管理
     * @return 结果
     */
    public int updateQxDeviceInfo(QxDeviceInfo qxDeviceInfo);

    /**
     * 删除售货机管理
     * 
     * @param seq 售货机管理ID
     * @return 结果
     */
    public int deleteQxDeviceInfoById(Integer seq);

    /**
     * 批量删除售货机管理
     * 
     * @param seqs 需要删除的数据ID
     * @return 结果
     */
    public int deleteQxDeviceInfoByIds(String[] seqs);
}
