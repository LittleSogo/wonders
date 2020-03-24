package com.wonders.service;

import com.wonders.dao.TPermission;
import com.wonders.dao.TRole;
import com.wonders.dao.TUser;

import java.util.List;

public interface AccountService {

    /**
     * 根据用户名获取权限
     * @param username
     * @return
     */
    List<String> getPermissionsByUserName(String username);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    TUser getUserByUserName(String username);

    /**
     * 根据用户名查找用户角色
     * @param userName
     * @return
     */
    List<TRole> getRolesByUserName(String userName);

    /**
     * 根据用户名查找用户权限
     * @return
     */
    List<TPermission> getPermissionsByUserName2(String username);


}
