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
import com.ruoyi.system.domain.QxDeviceInfo;
import com.ruoyi.system.service.IQxDeviceInfoService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 售货机管理Controller
 *
 * @author wangzhe
 * @date 2020-04-01
 */
@Controller
@RequestMapping("/system/device_info")
public class QxDeviceInfoController extends BaseController {
    private String prefix = "system/device_info";

    @Autowired
    private IQxDeviceInfoService qxDeviceInfoService;

    @RequiresPermissions("system:device_info:view")
    @GetMapping()
    public String device_info() {
        return prefix + "/device_info";
    }

    /**
     * 查询售货机管理列表
     */
    @RequiresPermissions("system:device_info:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(QxDeviceInfo qxDeviceInfo) {
        //System.out.println(qxDeviceInfoService);
        startPage();
        List<QxDeviceInfo> list = qxDeviceInfoService.selectQxDeviceInfoList(qxDeviceInfo);
        return getDataTable(list);
    }

    /**
     * 导出售货机管理列表
     */
    @RequiresPermissions("system:device_info:export")
    @Log(title = "售货机管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(QxDeviceInfo qxDeviceInfo) {
        List<QxDeviceInfo> list = qxDeviceInfoService.selectQxDeviceInfoList(qxDeviceInfo);
        ExcelUtil<QxDeviceInfo> util = new ExcelUtil<QxDeviceInfo>(QxDeviceInfo.class);
        return util.exportExcel(list, "device_info");
    }

    /**
     * 新增售货机管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存售货机管理
     */
    @RequiresPermissions("system:device_info:add")
    @Log(title = "售货机管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(QxDeviceInfo qxDeviceInfo) {
        return toAjax(qxDeviceInfoService.insertQxDeviceInfo(qxDeviceInfo));
    }

    /**
     * 修改售货机管理
     */
    @GetMapping("/edit/{seq}")
    public String edit(@PathVariable("seq") Integer seq, ModelMap mmap) {
        QxDeviceInfo qxDeviceInfo = qxDeviceInfoService.selectQxDeviceInfoById(seq);
        mmap.put("qxDeviceInfo", qxDeviceInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存售货机管理
     */
    @RequiresPermissions("system:device_info:edit")
    @Log(title = "售货机管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(QxDeviceInfo qxDeviceInfo) {
        return toAjax(qxDeviceInfoService.updateQxDeviceInfo(qxDeviceInfo));
    }

    /**
     * 删除售货机管理
     */
    @RequiresPermissions("system:device_info:remove")
    @Log(title = "售货机管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(qxDeviceInfoService.deleteQxDeviceInfoByIds(ids));
    }
}
