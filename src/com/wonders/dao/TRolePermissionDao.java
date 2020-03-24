package com.wonders.dao;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.springframework.stereotype.Repository;
import wfc.facility.tool.autocode.*;
import wfc.service.database.*;

/**
 * 
 * @author generated by wfc.facility.tool.autocode.InternalDaoGenerator
 */
@Repository
public class TRolePermissionDao {

    private Connection con = null;

    public TRolePermissionDao() {
    }

    public TRolePermissionDao(Connection con) {
        this.con = con;
    }

    public void add(TRolePermission info) {
        String sql = "insert into T_ROLE_PERMISSION(ID, REMARKS, PERMISSION_ID, ROLE_ID) values (?, ?, ?, ?)";
        Object[] obj = {
            info.getId(),
            info.getRemarks(),
            info.getPermissionId(),
            info.getRoleId()
        };
        if (con == null) {
            SQL.execute(sql, obj);
        } else {
            SQL.execute(con, sql, obj);
        }
    }

    public int update(Map<String, Object> map, Conditions conds) {
        String sql = "update T_ROLE_PERMISSION set ";
        List<Object> list = new ArrayList<Object>();
        int i = 0;
        for (String field : map.keySet()) {
            if (i++ > 0) {
                sql += ", ";
            }
            sql += field + " = ?";
            list.add(map.get(field));
        }
        String subsql = conds != null ? conds.toString() : "";
        if ("".equals(subsql)) {
            if (con == null) {
                return SQL.execute(sql).TOTAL_RECORD_COUNT;
            } else {
                return SQL.execute(con, sql).TOTAL_RECORD_COUNT;
            }
        } else {
            sql += " where " + subsql;
            list.addAll(conds.getObjectList());
            if (con == null) {
                return SQL.execute(sql, list.toArray()).TOTAL_RECORD_COUNT;
            } else {
                return SQL.execute(con, sql, list.toArray()).TOTAL_RECORD_COUNT;
            }
        }
    }
    
    public int delete(Conditions conds) {
        String sql = "delete from T_ROLE_PERMISSION";
        String subsql = conds != null ? conds.toString() : "";
        if ("".equals(subsql)) {
            if (con == null) {
                return SQL.execute(sql).TOTAL_RECORD_COUNT;
            } else {
                return SQL.execute(con, sql).TOTAL_RECORD_COUNT;
            }
        } else {
            sql += " where " + subsql;
            if (con == null) {
                return SQL.execute(sql, conds.getObjectArray()).TOTAL_RECORD_COUNT;
            } else {
                return SQL.execute(con, sql, conds.getObjectArray()).TOTAL_RECORD_COUNT;
            }
        }
    }

    public PaginationArrayList<TRolePermission> query(Conditions conds, String suffix, int pageSize, int currentPage) {
        RecordSet rs;
        if (con == null) {
            rs = SQL.query("T_ROLE_PERMISSION", "*", conds, suffix, pageSize, currentPage);
        } else {
            rs = SQL.query(con, "T_ROLE_PERMISSION", "*", conds, suffix, pageSize, currentPage);
        }
        PaginationArrayList<TRolePermission> pal = new PaginationArrayList<TRolePermission>(rs.TOTAL_RECORD_COUNT, rs.COMMON_PAGE_SIZE, rs.CURRENT_PAGE);
        while (rs.next()) {
            TRolePermission info = new TRolePermission();
            info.setId(rs.getBigDecimal("ID"));
            info.setRemarks(rs.getOriginalString("REMARKS"));
            info.setPermissionId(rs.getBigDecimal("PERMISSION_ID"));
            info.setRoleId(rs.getBigDecimal("ROLE_ID"));
            pal.add(info);
        }
        return pal;
    }

    public List<TRolePermission> query(Conditions conds, String suffix) {
        RecordSet rs;
        if (con == null) {
            rs = SQL.query("T_ROLE_PERMISSION", "*", conds, suffix);
        } else {
            rs = SQL.query(con, "T_ROLE_PERMISSION", "*", conds, suffix);
        }
        ArrayList<TRolePermission> al = new ArrayList<TRolePermission>();
        while (rs.next()) {
            TRolePermission info = new TRolePermission();
            info.setId(rs.getBigDecimal("ID"));
            info.setRemarks(rs.getOriginalString("REMARKS"));
            info.setPermissionId(rs.getBigDecimal("PERMISSION_ID"));
            info.setRoleId(rs.getBigDecimal("ROLE_ID"));
            al.add(info);
        }
        return al;
    }

}