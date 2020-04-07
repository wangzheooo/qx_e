package com.ruoyi.system.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.QxDeviceRecord;
import com.ruoyi.system.service.IQxDeviceRecordService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备连接记录Controller
 * 
 * @author wangzhe
 * @date 2020-04-07
 */
@Controller
@RequestMapping("/system/record")
public class QxDeviceRecordController extends BaseController
{
    private String prefix = "system/record";

    @Autowired
    private IQxDeviceRecordService qxDeviceRecordService;

    @RequiresPermissions("system:record:view")
    @GetMapping()
    public String record()
    {
        return prefix + "/record";
    }

    /**
     * 查询设备连接记录列表
     */
    @RequiresPermissions("system:record:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(QxDeviceRecord qxDeviceRecord)
    {
        startPage();
        List<QxDeviceRecord> list = qxDeviceRecordService.selectQxDeviceRecordList(qxDeviceRecord);
        return getDataTable(list);
    }

    /**
     * 导出设备连接记录列表
     */
    @RequiresPermissions("system:record:export")
    @Log(title = "设备连接记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(QxDeviceRecord qxDeviceRecord)
    {
        List<QxDeviceRecord> list = qxDeviceRecordService.selectQxDeviceRecordList(qxDeviceRecord);
        ExcelUtil<QxDeviceRecord> util = new ExcelUtil<QxDeviceRecord>(QxDeviceRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 新增设备连接记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存设备连接记录
     */
    @RequiresPermissions("system:record:add")
    @Log(title = "设备连接记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(QxDeviceRecord qxDeviceRecord)
    {
        return toAjax(qxDeviceRecordService.insertQxDeviceRecord(qxDeviceRecord));
    }

    /**
     * 修改设备连接记录
     */
    @GetMapping("/edit/{seq}")
    public String edit(@PathVariable("seq") Integer seq, ModelMap mmap)
    {
        QxDeviceRecord qxDeviceRecord = qxDeviceRecordService.selectQxDeviceRecordById(seq);
        mmap.put("qxDeviceRecord", qxDeviceRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存设备连接记录
     */
    @RequiresPermissions("system:record:edit")
    @Log(title = "设备连接记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(QxDeviceRecord qxDeviceRecord)
    {
        return toAjax(qxDeviceRecordService.updateQxDeviceRecord(qxDeviceRecord));
    }

    /**
     * 删除设备连接记录
     */
    @RequiresPermissions("system:record:remove")
    @Log(title = "设备连接记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(qxDeviceRecordService.deleteQxDeviceRecordByIds(ids));
    }
}
