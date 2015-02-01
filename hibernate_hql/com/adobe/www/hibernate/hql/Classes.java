package com.adobe.www.hibernate.hql;

import java.io.Serializable;
import java.util.Set;

public class Classes implements Serializable{
	private Long cid;
	private String cname;
	private String description;
	
	public Classes(){
		
	}
	
	public Classes(Long cid,String cname){
		this.cid = cid;
		this.cname = cname;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private Set<Student> students;

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
}
