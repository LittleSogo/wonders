package com.imooc.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="people")
public class People {
    
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name="ID")
    private int id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="sex")
    private String sex;
    
  
    
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}



	@Override
	public String toString() {
		return "People [id=" + id + ", name=" + name + ", sex=" + sex + "]";
	}
    
    
}
