package com.imooc.model;

import com.common.Page;

import javax.persistence.*;

@Table(name = "t_person")
@Entity
public class TPerson extends Page {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "t_name")
    private String tName;

    @Column(name = "t_password")
    private String tPassword;

    @Column(name = "t_age")
    private String tAge;

    @Column(name = "t_email")
    private String tEmail;

    @Column(name = "t_message")
    private String tMessage;

    @Column(name = "t_sex")
    private String tSex;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return t_name
     */
    public String gettName() {
        return tName;
    }

    /**
     * @param tName
     */
    public void settName(String tName) {
        this.tName = tName;
    }

    /**
     * @return t_password
     */
    public String gettPassword() {
        return tPassword;
    }

    /**
     * @param tPassword
     */
    public void settPassword(String tPassword) {
        this.tPassword = tPassword;
    }

    /**
     * @return t_age
     */
    public String gettAge() {
        return tAge;
    }

    /**
     * @param tAge
     */
    public void settAge(String tAge) {
        this.tAge = tAge;
    }

    /**
     * @return t_email
     */
    public String gettEmail() {
        return tEmail;
    }

    /**
     * @param tEmail
     */
    public void settEmail(String tEmail) {
        this.tEmail = tEmail;
    }

    /**
     * @return t_message
     */
    public String gettMessage() {
        return tMessage;
    }

    /**
     * @param tMessage
     */
    public void settMessage(String tMessage) {
        this.tMessage = tMessage;
    }

    /**
     * @return t_sex
     */
    public String gettSex() {
        return tSex;
    }

    /**
     * @param tSex
     */
    public void settSex(String tSex) {
        this.tSex = tSex;
    }
}