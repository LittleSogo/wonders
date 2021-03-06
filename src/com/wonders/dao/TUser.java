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
@Table(name = "T_USER")
public class TUser implements Serializable {
    
    /**
     * 
     */
    public static final String T_USER = "T_USER";
    
    /**
     * 
     */
    public static final String ID = "ID";
    
    /**
     * 
     */
    public static final String PASSWORD = "PASSWORD";
    
    /**
     * 
     */
    public static final String REMARKS = "REMARKS";
    
    /**
     * 
     */
    public static final String USER_NAME = "USER_NAME";
    
    public TUser() {
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
    @Column(name = "PASSWORD")
    private String password;

    /**
     * 
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 
     */
    @Column(name = "USER_NAME")
    private String userName;

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
    public String getPassword() {
        return this.password;
    }
    
    /**
     * 
     */
    public String password2Html() {
        return StringHelper.replaceHTMLSymbol(this.password);
    }

    /**
     * 
     */
    public void setPassword(String password) {
        password = AutoCodeConstant.substringBySize(password, 100, false);
        this.password = password;
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
    public String getUserName() {
        return this.userName;
    }
    
    /**
     * 
     */
    public String userName2Html() {
        return StringHelper.replaceHTMLSymbol(this.userName);
    }

    /**
     * 
     */
    public void setUserName(String userName) {
        userName = AutoCodeConstant.substringBySize(userName, 100, false);
        this.userName = userName;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}