package com.abclonal.product.web.controller.system;

import com.abclonal.product.api.domain.request.DeleteUserRequest;
import com.abclonal.product.api.domain.request.SysUserRequest;
import com.abclonal.product.api.domain.response.LoginUserResponse;
import com.abclonal.product.api.domain.response.SysUserResponse;
import com.abclonal.product.biz.system.SysDeptBiz;
import com.abclonal.product.biz.system.SysUserBiz;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.common.text.Convert;
import com.abclonal.product.common.utils.DateUtils;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.common.utils.poi.ExcelUtil;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.dao.entity.SysDept;
import com.abclonal.product.dao.entity.SysRole;
import com.abclonal.product.dao.entity.SysUser;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.domain.LoginUser;
import com.abclonal.product.service.domain.vo.TreeSelect;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.api.domain.response.sys.SysRoleResponse;
import com.abclonal.product.service.system.ISysConfigService;
import com.abclonal.product.service.system.ISysPermissionService;
import com.abclonal.product.service.system.ISysUserService;
import com.abclonal.product.service.system.service.I18nService;
import com.abclonal.product.service.system.service.ISysRoleService;
import com.abclonal.product.web.security.annotation.InnerAuth;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "用户管理", description = "提供用户信息的增删改查接口")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysUserBiz sysUserBiz;

    @Autowired
    private SysDeptBiz sysDeptBiz;

    @Autowired
    private I18nService i18nService;

    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    @Operation(
            summary = "查询用户列表",
            description = "根据用户条件分页查询用户信息，支持按用户名、部门、时间范围筛选"
    )
    public R<TableDataInfo<SysUserResponse>> list(SysUserRequest user) {
        return sysUserBiz.list(user);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @Operation(
            summary = "导出",
            description = "根据查询导出"
    )
    public void export(HttpServletResponse response, @RequestBody SysUser user) {
        log.info("user={}", JSON.toJSONString(user));
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.user"));
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    @Operation(
            summary = "导入",
            description = "导入"
    )
    public R<String> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = sysUserBiz.importUser(userList, updateSupport, operName);
        return R.ok(message);
    }

    @PostMapping("/importTemplate")
    @Operation(
            summary = "导入",
            description = "导入"
    )
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, i18nService.getMessage("export.filename.user"));
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    @Operation(
            summary = "获取当前用户信息",
            description = "通过用户名获取用户信息"
    )
    public R<LoginUser> info(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail(i18nService.getMessage("user.password.error"));
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    @Operation(
            summary = "注册用户信息",
            description = "注册用户信息"
    )
    public R<Boolean> register(@RequestBody SysUser sysUser) {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail(i18nService.getMessage("user.register.disabled"));
        }
        if (!userService.checkUserNameUnique(sysUser)) {
            return R.fail(i18nService.getMessage("user.register.exists", username));
        }
        return R.ok(userService.registerUser(sysUser));
    }

    /**
     * 记录用户登录IP地址和登录时间
     */
    @InnerAuth
    @PutMapping("/recordlogin")
    @Operation(
            summary = "记录用户登录IP地址和登录时间",
            description = "记录用户登录IP地址和登录时间"
    )
    public R<Boolean> recordlogin(@RequestBody SysUser sysUser) {
        return R.ok(userService.updateUserProfile(sysUser));
    }

    @GetMapping("getInfo")
    @Operation(
            summary = "获取用户信息",
            description = ""
    )
    public R<LoginUserResponse> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return sysUserBiz.getInfo(loginUser);
    }

    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Date pwdUpdateDate) {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate) {
        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0) {
            if (StringUtils.isNull(pwdUpdateDate)) {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }

    /**
     * 根据用户编号获取详细信息
     */
    @RequiresPermissions("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    @Operation(
            summary = "根据用户编号获取详细信息",
            description = ""
    )
    public R<HashMap<String,Object>> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        return sysUserBiz.getInfo(userId);
    }

    /**
     * 新增用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @Operation(
            summary = "新增用户",
            description = "新增用户插入成功返回UserId"
    )
    public R<Long> add(@Validated @RequestBody SysUserRequest user) {
        return sysUserBiz.add(user);
    }

    /**
     * 修改用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    @Operation(
            summary = "修改用户",
            description = ""
    )
    public R<Boolean> edit(@Validated @RequestBody SysUserRequest user) {
        return sysUserBiz.edit(user);
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("system:user:delete")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    @Operation(
            summary = "删除用户",
            description = ""
    )
    public R<String> remove(@PathVariable Long[] userIds) {
        return sysUserBiz.remove(userIds);
    }

    /**
     * 新增用户 - POST /api/user
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(
            summary = "新增用户",
            description = "新增用户，校验用户名唯一性，成功返回用户ID"
    )
    public R<Long> addUser(@Validated @RequestBody SysUserRequest user) {
        return sysUserBiz.add(user);
    }

    /**
     * 修改用户 - PUT /api/user
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(
            summary = "修改用户",
            description = "根据body中的userId更新用户信息"
    )
    public R<Boolean> editUser(@Validated @RequestBody SysUserRequest user) {
        return sysUserBiz.edit(user);
    }

    /**
     * 批量删除用户 - POST /api/user/batch-delete
     */
    @RequiresPermissions("system:user:delete")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/batch-delete")
    @Operation(
            summary = "批量删除用户",
            description = "根据userIds数组批量删除用户，返回删除条数"
    )
    public R<Integer> batchDelete(@RequestBody DeleteUserRequest deleteRequest) {
        return sysUserBiz.removeUser(deleteRequest.getUserIds());
    }

    /**
     * 重置密码
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    @Operation(
            summary = "重置密码",
            description = ""
    )
    public R<Boolean> resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(userService.resetPwd(user) > 0);
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @Operation(
            summary = "状态修改",
            description = ""
    )
    public R<Integer> changeStatus(@RequestBody SysUser user) {
        return sysUserBiz.changeStatus(user);
    }

    /**
     * 根据用户编号获取授权角色
     */
    @RequiresPermissions("system:user:query")
    @GetMapping("/authRole/{userId}")
    @Operation(
            summary = "根据用户编号获取授权角色",
            description = ""
    )
    public R<Map<String, Object>> authRole(@PathVariable("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        SysUser user = userService.selectUserById(userId);
        List<SysRoleResponse> roles = roleService.selectRolesByUserId(userId);
        result.put("user", user);
        result.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !"admin".equals(r.getRoleKey())).collect(Collectors.toList()));
        return R.ok(result);
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    @Operation(
            summary = "用户授权角色",
            description = ""
    )
    public R<Boolean> insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        roleService.checkRoleDataScope(roleIds);
        userService.insertUserAuth(userId, roleIds);
        return R.ok(true);
    }

    /**
     * 获取部门树列表
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/deptTree")
    @Operation(
            summary = "获取部门树列表",
            description = ""
    )
    public R<List<TreeSelect>> deptTree(SysDept dept) {
        return R.ok(sysDeptBiz.selectDeptTreeList(dept));
    }
}
