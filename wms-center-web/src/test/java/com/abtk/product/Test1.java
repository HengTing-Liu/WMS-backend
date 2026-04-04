package com.abtk.product;

import com.abtk.product.dao.entity.SysMenu;
import com.abtk.product.service.system.ISysMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Test1 extends TestBase {

    @Autowired
    private ISysMenuService menuService;

    @Test
    public void Test3(){
        SysMenu menu = new SysMenu();
        menu.setMenuName("分析页");
        menu.setParentId(2000L);
        menu.setOrderNum(0);
        menu.setPath("/analytics");
        menu.setComponent("/dashboard/analytics/index");
        menu.setIsFrame("1");
        menu.setIsCache("");
        menu.setMenuType("C");
        menu.setVisible("0");
        menu.setStatus("0");
        int i = menuService.insertMenu(menu);
        System.out.println(i);
    }
}
