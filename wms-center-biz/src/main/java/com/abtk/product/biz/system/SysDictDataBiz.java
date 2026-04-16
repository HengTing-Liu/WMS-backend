package com.abtk.product.biz.system;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.dao.entity.SysDictData;
import com.abtk.product.dao.mapper.SysDictDataMapper;
import com.abtk.product.service.security.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 字典数据业务层
 *
 * @author backend
 * @since 2026-04-16
 */
@Component
public class SysDictDataBiz {

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 导入字典数据
     *
     * @param dictList      字典数据列表
     * @param updateSupport 是否支持更新
     * @param operName      操作人
     * @return 结果消息
     */
    @Transactional(rollbackFor = Exception.class)
    public String importDictData(List<SysDictData> dictList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dictList) || dictList.isEmpty()) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SysDictData dict : dictList) {
            try {
                if (StringUtils.isEmpty(dict.getDictType()) || StringUtils.isEmpty(dict.getDictValue())) {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、字典类型和字典键值不能为空");
                    continue;
                }

                dict.setCreateBy(operName);
                dict.setUpdateBy(operName);
                dict.setUpdateTime(new Date());

                SysDictData existingData = dictDataMapper.selectDictDataByTypeAndValue(dict.getDictType(), dict.getDictValue());
                if (StringUtils.isNull(existingData)) {
                    dictDataMapper.insertDictData(dict);
                    successNum++;
                } else if (updateSupport) {
                    dict.setId(existingData.getId());
                    dictDataMapper.updateDictData(dict);
                    successNum++;
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、字典 ").append(dict.getDictLabel()).append(" 已存在");
                }

                // 刷新缓存
                List<SysDictData> cached = dictDataMapper.selectDictDataByType(dict.getDictType());
                DictUtils.setDictCache(dict.getDictType(), cached);
            } catch (Exception e) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、字典 ").append(dict.getDictLabel()).append(" 导入失败：").append(e.getMessage());
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条。");
        }
        return successMsg.toString();
    }
}
