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
public class TPermissionDao {

    private Connection con = null;

    public TPermissionDao() {
    }

    public TPermissionDao(Connection con) {
        this.con = con;
    }

    public void add(TPermission info) {
        String sql = "insert into T_PERMISSION(ID, PERMISSION_NAME, REMARKS) values (?, ?, ?)";
        Object[] obj = {
            info.getId(),
            info.getPermissionName(),
            info.getRemarks()
        };
        if (con == null) {
            SQL.execute(sql, obj);
        } else {
            SQL.execute(con, sql, obj);
        }
    }

    public int update(Map<String, Object> map, Conditions conds) {
        String sql = "update T_PERMISSION set ";
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
        String sql = "delete from T_PERMISSION";
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

    public PaginationArrayList<TPermission> query(Conditions conds, String suffix, int pageSize, int currentPage) {
        RecordSet rs;
        if (con == null) {
            rs = SQL.query("T_PERMISSION", "*", conds, suffix, pageSize, currentPage);
        } else {
            rs = SQL.query(con, "T_PERMISSION", "*", conds, suffix, pageSize, currentPage);
        }
        PaginationArrayList<TPermission> pal = new PaginationArrayList<TPermission>(rs.TOTAL_RECORD_COUNT, rs.COMMON_PAGE_SIZE, rs.CURRENT_PAGE);
        while (rs.next()) {
            TPermission info = new TPermission();
            info.setId(rs.getBigDecimal("ID"));
            info.setPermissionName(rs.getOriginalString("PERMISSION_NAME"));
            info.setRemarks(rs.getOriginalString("REMARKS"));
            pal.add(info);
        }
        return pal;
    }

    public List<TPermission> query(Conditions conds, String suffix) {
        RecordSet rs;
        if (con == null) {
            rs = SQL.query("T_PERMISSION", "*", conds, suffix);
        } else {
            rs = SQL.query(con, "T_PERMISSION", "*", conds, suffix);
        }
        ArrayList<TPermission> al = new ArrayList<TPermission>();
        while (rs.next()) {
            TPermission info = new TPermission();
            info.setId(rs.getBigDecimal("ID"));
            info.setPermissionName(rs.getOriginalString("PERMISSION_NAME"));
            info.setRemarks(rs.getOriginalString("REMARKS"));
            al.add(info);
        }
        return al;
    }

}