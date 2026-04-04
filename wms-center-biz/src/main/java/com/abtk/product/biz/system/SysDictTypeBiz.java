package com.abtk.product.biz.system;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.dao.entity.SysDictType;
import com.abtk.product.dao.mapper.SysDictTypeMapper;
import com.abtk.product.service.system.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 字典类型业务层
 *
 * @author lht
 * @since 2026-03-11
 */
@Component
public class SysDictTypeBiz {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    /**
     * 导入字典类型数据
     *
     * @param dictList 字典类型列表
     * @param updateSupport 是否支持更新
     * @param operName 操作人
     * @return 结果消息
     */
    @Transactional(rollbackFor = Exception.class)
    public String importDictType(List<SysDictType> dictList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dictList) || dictList.isEmpty()) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SysDictType dict : dictList) {
            try {
                // 设置创建人和更新人
                dict.setCreateBy(operName);
                dict.setUpdateBy(operName);
                dict.setUpdateTime(new Date());

                // 验证是否存在这个数据
                SysDictType existingDict = dictTypeMapper.selectDictTypeByType(dict.getDictType());
                if (StringUtils.isNull(existingDict)) {
                    // 新增
                    dictTypeMapper.insertDictType(dict);
                    successNum++;
                } else if (updateSupport) {
                    // 更新
                    dict.setDictId(existingDict.getDictId());
                    dictTypeMapper.updateDictType(dict);
                    successNum++;
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、字典 " + dict.getDictName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、字典 " + dict.getDictName() + " 导入失败：" + e.getMessage();
                failureMsg.append(msg);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
