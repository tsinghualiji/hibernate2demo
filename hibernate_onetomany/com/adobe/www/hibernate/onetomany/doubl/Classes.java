package com.adobe.www.hibernate.onetomany.doubl;

import java.io.Serializable;
import java.util.Set;

public class Classes implements Serializable{
	private Long cid;
	private String cname;
	private String description;
	private Set<Student> students;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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
