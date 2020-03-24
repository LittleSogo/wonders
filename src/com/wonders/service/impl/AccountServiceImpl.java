package com.wonders.service.impl;

import com.wonders.dao.*;
import com.wonders.service.AccountService;
import common.utils.MWYHandleDaoUtil;
import coral.widget.data.DataSet;
import coral.widget.data.RowSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfc.service.database.Condition;
import wfc.service.database.Conditions;
import wfc.service.database.RecordSet;
import wfc.service.util.OrderedHashMap;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zj
 * @date ：Created in 2020/3/17 14:22
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private TPermissionDao tPermissionDao;

    @Resource
    private TRoleDao tRoleDao;

    @Resource
    private TUserDao tUserDao;

    @Override
    public List<String> getPermissionsByUserName(String username) {
        List<String> idList = new ArrayList<String>();

        Conditions conds = Conditions.newAndConditions();

        conds.add(new Condition("PERMISSION_NAME",Condition.OT_EQUAL,username));
        List<TPermission> queryList = tPermissionDao.query(conds, null);

        for (int i = 0; i <queryList.size() ; i++) {
            TPermission tPermission = queryList.get(i);
            idList.add(tPermission.getId().toString());
        }
        return idList;
    }

    @Override
    public TUser getUserByUserName(String username) {
        List<String> idList = new ArrayList<String>();

        Conditions conds = Conditions.newAndConditions();

        conds.add(new Condition("USER_NAME",Condition.OT_EQUAL,username));
        List<TUser> queryList = tUserDao.query(conds, null);

        if(queryList.size()>0){
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public List<TRole> getRolesByUserName(String userName) {
        List<TRole> roleList = new ArrayList<TRole>();
        String sql ="select r.* from t_user u,t_user_role ur,t_role r where u.id=ur.role_id and ur.role_id=r.id and u.user_name='"+userName+"'";

        RecordSet recordSet = MWYHandleDaoUtil.sqlExcute(sql);

        while (recordSet.next()){
            TRole tRole = new TRole();
            tRole.setId(recordSet.getBigDecimal("ID"));
            tRole.setRemarks(recordSet.getOriginalString("REMARKS"));
            tRole.setRoleName(recordSet.getOriginalString("ROLE_NAME"));

            roleList.add(tRole);
        }

       /* DataSet dataCountSet = DataSet.convert(recordSet);

        List<RowSet> rowSetList = dataCountSet.getRows();  //

        for (RowSet rowSet : rowSetList) {
            OrderedHashMap oldrowMap = rowSet.getRow();
            OrderedHashMap rowMap = new OrderedHashMap();
        }*/




        return roleList;
        //return tRoleDao.findListBySql(sql);
    }

    @Override
    public List<TPermission> getPermissionsByUserName2(String username) {

        List<TPermission> tPermissionList = new ArrayList<TPermission>();

        String sql ="select p.* from t_user u,t_user_role ur,t_role_permission rp,t_permission p where u.id=ur.user_id and ur.role_id=rp.role_id and rp.permission_id=p.id and u.user_name='"+username+"'";

        RecordSet recordSet = MWYHandleDaoUtil.sqlExcute(sql);

        while (recordSet.next()){
            TPermission tPermission = new TPermission();
            tPermission.setId(recordSet.getBigDecimal("ID"));
            tPermission.setPermissionName(recordSet.getOriginalString("PERMISSION_NAME"));
            tPermission.setRemarks(recordSet.getOriginalString("REMARKS"));

            tPermissionList.add(tPermission);
        }
        return tPermissionList;
    }
}
