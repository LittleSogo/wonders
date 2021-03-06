package com.wonders.dao;
		
import java.io.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import javax.persistence.*;
import javax.xml.bind.annotation.adapters.*;
import org.apache.commons.lang.builder.*;
import wfc.facility.tool.autocode.*;
import wfc.service.util.*;

/**
 * 
 * @author generated by wfc.facility.tool.autocode.InternalDaoGenerator
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_ROLE")
public class TRole implements Serializable {
    
    /**
     * 
     */
    public static final String T_ROLE = "T_ROLE";
    
    /**
     * 
     */
    public static final String ID = "ID";
    
    /**
     * 
     */
    public static final String REMARKS = "REMARKS";
    
    /**
     * 
     */
    public static final String ROLE_NAME = "ROLE_NAME";
    
    public TRole() {
    }

    /**
     * 
     */
    @Id
    @Column(name = "ID")
    private BigDecimal id;

    /**
     * 
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 
     */
    @Column(name = "ROLE_NAME")
    private String roleName;

	/**
     * 
     */
    public BigDecimal getId() {
        return this.id;
    }
    
    /**
     * 
     */
    public String id2Html(int precision) {
        if (this.id == null) {
            return "";
        } else {
            String pattern = "0";
            if (precision > 0) {
                pattern += ".";
                for (int i = 0; i < precision; i++) {
                    pattern += "0";
                }
            }
            return new DecimalFormat(pattern).format(this.id);
        }
    }

    /**
     * 
     */
    public void setId(BigDecimal id) {
        
        this.id = id;
    }

	/**
     * 
     */
    public String getRemarks() {
        return this.remarks;
    }
    
    /**
     * 
     */
    public String remarks2Html() {
        return StringHelper.replaceHTMLSymbol(this.remarks);
    }

    /**
     * 
     */
    public void setRemarks(String remarks) {
        remarks = AutoCodeConstant.substringBySize(remarks, 1000, false);
        this.remarks = remarks;
    }

	/**
     * 
     */
    public String getRoleName() {
        return this.roleName;
    }
    
    /**
     * 
     */
    public String roleName2Html() {
        return StringHelper.replaceHTMLSymbol(this.roleName);
    }

    /**
     * 
     */
    public void setRoleName(String roleName) {
        roleName = AutoCodeConstant.substringBySize(roleName, 100, false);
        this.roleName = roleName;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}