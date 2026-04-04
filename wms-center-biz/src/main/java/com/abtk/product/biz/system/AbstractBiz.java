package com.abtk.product.biz.system;

import com.abtk.product.common.web.page.TableDataInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/7 10:50
 *
 */
public class AbstractBiz {

    protected TableDataInfo getPageList(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }
}
