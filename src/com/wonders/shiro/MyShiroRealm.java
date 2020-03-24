package com.wonders.shiro;

import com.wonders.dao.TPermission;
import com.wonders.dao.TRole;
import com.wonders.dao.TUser;
import com.wonders.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：zj
 * @date ：Created in 2020/3/17 14:16
 * @description：自定义的shiro组件
 * @modified By：
 * @version: $
 */
public class MyShiroRealm extends AuthorizingRealm {
    /**用户的业务类**/
    @Autowired
    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /***
     * 获取授权信息
     */
    //根据自己系统规则的需要编写获取授权信息，这里为了快速入门只获取了用户对应角色的资源url信息
   /* @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        String username = (String)pc.fromRealm(getName()).iterator().next();
        if(username!=null){
            List<String> pers = accountService.getPermissionsByUserName(username);
            *//*for (int i = 0; i < pers.size(); i++) {
                String s = pers.get(i);
                System.out.println(s+"==========>>>"+i);
            }*//*


            if(pers!=null && !pers.isEmpty()){
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                for(String each:pers){
                    //将权限资源添加到用户信息中
                    info.addStringPermission(each);
                }
            }
        }
        return null;
    }*/
    /***
     * 获取认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken at) throws AuthenticationException {


        UsernamePasswordToken token = (UsernamePasswordToken) at;
        // 通过表单接收的用户名
        String username  = token.getUsername();
        if(username!=null && !"".equals(username)){
            TUser user = accountService.getUserByUserName(username);
            if (user != null) {
                return new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());
            }
        }
        return null;
    }

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName=(String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        Set<String> roles=new HashSet<String>();

        /**
         * 根据用户名查找用户角色
         * //select r.* from t_user u,t_user_role ur,t_role r where u.id=ur.role_id and ur.role_id=r.id and u.user_name=?
         *          *
         */

        List<TRole> rolesByUserName = accountService.getRolesByUserName(userName);

        System.out.println(rolesByUserName.get(0).getRoleName()+"=============>>>rolesByUserNamerolesByUserNamerolesByUserName<<<=============");

        for (TRole tRole:rolesByUserName){
            roles.add(tRole.getRoleName());
        }

        /**
         * 根据用户名查找权限
         */
        List<TPermission> permissionsByUserNameList = accountService.getPermissionsByUserName2(userName);

        for (TPermission tPermission:permissionsByUserNameList){
            info.setRoles(roles);
        }

        return info;
    }
}
