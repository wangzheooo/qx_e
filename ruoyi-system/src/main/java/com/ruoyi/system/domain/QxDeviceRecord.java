package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 设备连接记录对象 qx_device_record
 * 
 * @author wangzhe
 * @date 2020-04-07
 */
public class QxDeviceRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Integer seq;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String deviceId;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String type;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    public void setSeq(Integer seq) 
    {
        this.seq = seq;
    }

    public Integer getSeq() 
    {
        return seq;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("seq", getSeq())
            .append("deviceId", getDeviceId())
            .append("type", getType())
            .append("createDate", getCreateDate())
            .toString();
    }
}
