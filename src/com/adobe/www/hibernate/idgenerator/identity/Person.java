package com.adobe.www.hibernate.idgenerator.identity;

import java.io.Serializable;

public class Person implements Serializable{
	private Long pid;
	private String pname;
	private String psex;
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPsex() {
		return psex;
	}
	public void setPsex(String psex) {
		this.psex = psex;
	}
}
