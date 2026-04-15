package com.abtk.product.biz.system;

import com.abtk.product.api.domain.request.SysUserRequest;
import com.abtk.product.api.domain.request.sys.SysRoleRequest;
import com.abtk.product.api.domain.response.LoginUserResponse;
import com.abtk.product.api.domain.response.sys.SysRoleResponse;
import com.abtk.product.api.domain.response.SysUserResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.PageUtil;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.utils.bean.BeanValidators;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.SysRole;
import com.abtk.product.dao.entity.SysUser;
import com.abtk.product.domain.converter.UserConverter;
import com.abtk.product.service.domain.LoginUser;
import com.abtk.product.service.security.TokenService;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.*;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.service.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.abtk.product.common.utils.PageUtils.startPage;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/3 19:53
 *
 */
@Component
@Slf4j
public class SysUserBiz extends AbstractBiz {

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysDeptBiz sysDeptBiz;

    @Autowired
    private SysRoleBiz sysRoleBiz;

    @Autowired
    protected Validator validator;

    @Autowired
    private I18nService i18nService;

    public R<LoginUserResponse> getInfo(LoginUser loginUser) {
        SysUser user = loginUser.getSysUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        LoginUserResponse vo = new LoginUserResponse();
        vo.setUserId(String.valueOf(loginUser.getUserid()));
        vo.setRoles(roles);
        vo.setUsername(loginUser.getUsername());
        vo.setAvatar(loginUser.getSysUser().getAvatar());
        vo.setHomePath(loginUser.getDefaultPage());
        vo.setDesc(loginUser.getSysUser().getRemarks());
        vo.setPermissions(permissions);
        return R.ok(vo);
    }

    public List<SysUser> selectUserList(SysUser user) {
        return sysUserService.selectUserList(user);
    }

    public R<LoginUser> getUserInfo(@PathVariable("username") String username) {
        SysUser sysUser = sysUserService.selectUserByUserName(username);
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
        // 计算默认首页：用户自定义 > 角色默认 > /home
        String defaultPage = sysUser.getDefaultPage();
        if (StringUtils.isEmpty(defaultPage) && sysUser.getRoles() != null && !sysUser.getRoles().isEmpty()) {
            for (SysRole role : sysUser.getRoles()) {
                if (StringUtils.isNotEmpty(role.getDefaultPage())) {
                    defaultPage = role.getDefaultPage();
                    break;
                }
            }
        }
        if (StringUtils.isEmpty(defaultPage)) {
            defaultPage = "/home";
        }
        sysUserVo.setDefaultPage(defaultPage);
        return R.ok(sysUserVo);
    }

    /**
     * 记录用户登录IP地址和登录时间
     */
    public R<Boolean> recordUserLogin(@RequestBody SysUser sysUser) {
        return R.ok(sysUserService.updateUserProfile(sysUser));
    }

    /**
     * 注册用户信息
     */
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser) {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail(i18nService.getMessage("user.register.disabled"));
        }
        if (!sysUserService.checkUserNameUnique(sysUser)) {
            return R.fail(i18nService.getMessage("user.register.exists", username));
        }
        return R.ok(sysUserService.registerUser(sysUser));
    }


    public R<Integer> changeStatus(SysUser user) {
        sysUserService.checkUserAllowed(user);
        checkUserDataScope(user.getUserId());
        user.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(sysUserService.updateUserStatus(user));
    }

    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = sysUserService.selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException(i18nService.getMessage("permission.denied"));
            }
        }
    }

    public R<HashMap<String, Object>> getInfo(Long userId) {
        HashMap hashMap = new HashMap();
        if (StringUtils.isNotNull(userId)) {
            checkUserDataScope(userId);
            SysUser sysUser = sysUserService.selectUserById(userId);
            hashMap.put("postIds", postService.selectPostListByUserId(userId));
            hashMap.put("roleIds", sysUser.getRoles().stream().map(SysRole::getId).collect(Collectors.toList()));
        }
        List<SysRoleResponse> roles = roleService.selectRoleList(new SysRoleRequest());
        hashMap.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !"admin".equals(r.getRoleKey())).collect(Collectors.toList()));
        hashMap.put("posts", postService.selectPostAll());
        return R.ok(hashMap);
    }

    /**
     *
     *
     * @param sysUserRequest
     * @description: 插入成功返回 userID
     * @return: com.abtk.product.common.domain.R<java.lang.Long>
     * @author: zpy
     * @time: 2026/2/12 11:25
     */
    public R<Long> add(SysUserRequest sysUserRequest) {
        sysDeptBiz.checkDeptDataScope(sysUserRequest.getDeptId());
        sysRoleBiz.checkRoleDataScope(sysUserRequest.getRoleIds());
        SysUser user = UserConverter.INSTANCE.requestToSysUser(sysUserRequest);
        if (!sysUserService.checkUserNameUnique(user)) {
            return R.fail(i18nService.getMessage("user.add.exists.account", sysUserRequest.getUserName()));
        } else if (StringUtils.isNotEmpty(sysUserRequest.getPhonenumber()) && !sysUserService.checkPhoneUnique(user)) {
            return R.fail(i18nService.getMessage("user.add.exists.phone", sysUserRequest.getUserName()));
        } else if (StringUtils.isNotEmpty(sysUserRequest.getEmail()) && !sysUserService.checkEmailUnique(user)) {
            return R.fail(i18nService.getMessage("user.add.exists.email", sysUserRequest.getUserName()));
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(sysUserRequest.getPassword()));
        sysUserService.insertUser(user);

        return R.ok(user.getUserId());
    }

    /**
     *
     *
     * @param sysUserRequest
     * @description: 修改成功返回true, 修改失败返回false
     * @return: com.abtk.product.common.domain.R<java.lang.Boolean>
     * @author: zpy
     * @time: 2026/2/12 11:27
     */
    public R<Boolean> edit(@Validated @RequestBody SysUserRequest sysUserRequest) {
        SysUser user = UserConverter.INSTANCE.requestToSysUser(sysUserRequest);
        sysUserService.checkUserAllowed(user);
        checkUserDataScope(user.getUserId());
        sysDeptBiz.checkDeptDataScope(user.getDeptId());
        sysRoleBiz.checkRoleDataScope(user.getRoleIds());
        if (!sysUserService.checkUserNameUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !sysUserService.checkPhoneUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !sysUserService.checkEmailUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(sysUserService.updateUser(user) == 1);
    }

    public R<TableDataInfo<SysUserResponse>> list(SysUserRequest user) {
        startPage();
        SysUser dto = UserConverter.INSTANCE.requestToSysUser(user);
        List<SysUser> list = sysUserService.selectUserList(dto);
        TableDataInfo<SysUserResponse> tableDataInfo = PageUtil.convertPage(list, this::toResponse);
        return R.ok(tableDataInfo, i18nService.getMessage("data.query.success"));
    }

    private SysUserResponse toResponse(SysUser sysUser) {
        SysUserResponse sysUserResponse = UserConverter.INSTANCE.sysUserToResponse(sysUser);
        return sysUserResponse;
    }

    /**
     *
     *
     * @param userIds
     * @description: 批量删除
     * @return: com.abtk.product.common.domain.R<java.lang.String>
     * @author: zpy
     * @time: 2026/2/12 14:03
     */
    public R<String> remove(Long[] userIds) {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            return R.fail(i18nService.getMessage("user.delete.self.forbidden"));
        }
        for (Long userId : userIds) {
            sysUserService.checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        sysUserService.deleteUserByIds(userIds);
        return R.ok(i18nService.getMessage("data.delete.success"));
    }

    /**
     * 批量删除用户 - DELETE /api/user
     *
     * @param userIds 用户ID数组
     * @return R<Integer> 删除成功返回删除条数
     * @description: 批量删除用户，返回删除条数
     * @author: backend2
     * @time: 2026/3/21
     */
    public R<Integer> removeUser(Long[] userIds) {
        if (userIds == null || userIds.length == 0) {
            return R.fail("删除用户ID不能为空");
        }
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            return R.fail(i18nService.getMessage("user.delete.self.forbidden"));
        }
        for (Long userId : userIds) {
            sysUserService.checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        int result = sysUserService.deleteUserByIds(userIds);
        return R.ok(result);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException(i18nService.getMessage("batch.add.list.empty"));
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = sysUserService.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    sysDeptBiz.checkDeptDataScope(user.getDeptId());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    sysUserService.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " " + i18nService.getMessage("data.insert.success"));
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    sysUserService.checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    sysDeptBiz.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    sysUserService.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " " + i18nService.getMessage("data.update.success"));
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " " + i18nService.getMessage("data.already.exists"));
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " " + i18nService.getMessage("data.insert.failed") + "：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, i18nService.getMessage("batch.add.failed", String.valueOf(failureNum)));
        }
        successMsg.insert(0, i18nService.getMessage("batch.add.success", String.valueOf(successNum)));

        String str = String.format("Total:%s, Success:%s<br>Failed:%s", userList.size(), successMsg.toString(), failureMsg.toString());
        return str;
    }
}
