package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 售货机管理对象 qx_device_info
 * 
 * @author wangzhe
 * @date 2020-04-01
 */
public class QxDeviceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Integer seq;

    /** 设备ID,唯一 */
    @Excel(name = "设备ID,唯一")
    private String deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 通信ID,唯一 */
    @Excel(name = "通信ID,唯一")
    private String channelId;

    /** 设备描述 */
    @Excel(name = "设备描述")
    private String deviceDescription;

    /** 设备摆放地址 */
    @Excel(name = "设备摆放地址")
    private String deviceAddr;

    /** 经度 */
    @Excel(name = "经度")
    private Double longitude;

    /** 维度 */
    @Excel(name = "维度")
    private Double latitude;

    /** 网络状态.001在线,002离线,003停机,004报警 */
    @Excel(name = "网络状态.001在线,002离线,003停机,004报警")
    private String networkStatus;

    /** 停启用.001启用,002停用 */
    @Excel(name = "停启用.001启用,002停用")
    private String isEnable;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createUser;

    /** 修改时间 */
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifyDate;

    /** 修改人 */
    @Excel(name = "修改人")
    private String modifyUser;

    /** 版本号 */
    @Excel(name = "版本号")
    private String version;

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
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setChannelId(String channelId) 
    {
        this.channelId = channelId;
    }

    public String getChannelId() 
    {
        return channelId;
    }
    public void setDeviceDescription(String deviceDescription) 
    {
        this.deviceDescription = deviceDescription;
    }

    public String getDeviceDescription() 
    {
        return deviceDescription;
    }
    public void setDeviceAddr(String deviceAddr) 
    {
        this.deviceAddr = deviceAddr;
    }

    public String getDeviceAddr() 
    {
        return deviceAddr;
    }
    public void setLongitude(Double longitude) 
    {
        this.longitude = longitude;
    }

    public Double getLongitude() 
    {
        return longitude;
    }
    public void setLatitude(Double latitude) 
    {
        this.latitude = latitude;
    }

    public Double getLatitude() 
    {
        return latitude;
    }
    public void setNetworkStatus(String networkStatus) 
    {
        this.networkStatus = networkStatus;
    }

    public String getNetworkStatus() 
    {
        return networkStatus;
    }
    public void setIsEnable(String isEnable) 
    {
        this.isEnable = isEnable;
    }

    public String getIsEnable() 
    {
        return isEnable;
    }
    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }
    public void setCreateUser(String createUser) 
    {
        this.createUser = createUser;
    }

    public String getCreateUser() 
    {
        return createUser;
    }
    public void setModifyDate(Date modifyDate) 
    {
        this.modifyDate = modifyDate;
    }

    public Date getModifyDate() 
    {
        return modifyDate;
    }
    public void setModifyUser(String modifyUser) 
    {
        this.modifyUser = modifyUser;
    }

    public String getModifyUser() 
    {
        return modifyUser;
    }
    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("seq", getSeq())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("channelId", getChannelId())
            .append("deviceDescription", getDeviceDescription())
            .append("deviceAddr", getDeviceAddr())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("networkStatus", getNetworkStatus())
            .append("isEnable", getIsEnable())
            .append("remarks", getRemarks())
            .append("createDate", getCreateDate())
            .append("createUser", getCreateUser())
            .append("modifyDate", getModifyDate())
            .append("modifyUser", getModifyUser())
            .append("version", getVersion())
            .toString();
    }
}
