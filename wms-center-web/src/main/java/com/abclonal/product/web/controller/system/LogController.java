package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.page.PageResult;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.dao.entity.LogIn;
import com.abclonal.product.service.system.ILogInService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录日志管理
 * 
 * @author wms
 */
@RestController
@RequestMapping("/log/in")
public class LogController extends BaseController
{
    @Autowired
    private ILogInService logInService;

    /**
     * 获取登录日志列表
     */
    @GetMapping("/list")
    public R<PageResult<LogIn>> list(LogInQuery query)
    {
        startPage();
        LogIn logIn = new LogIn();
        logIn.setUserName(query.getUserName());
        logIn.setIpAddr(query.getIpAddr());
        logIn.setStatus(query.getStatus());
        logIn.setParams(query.getParams());
        List<LogIn> list = logInService.selectLogInList(logIn);
        long total = new PageInfo(list).getTotal();
        return R.ok(new PageResult<LogIn>(list, total));
    }
}
