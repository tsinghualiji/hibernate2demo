package com.adobe.www.hibernate.hql;

import org.junit.Test;

import com.adobe.www.hibernate.idgenerator.identity.Person;

public class ClassCastExceptionTest {
	@Test
	public void test(){
		Object object = new Person();
		Classes classes = (Classes)object;
	}
}
