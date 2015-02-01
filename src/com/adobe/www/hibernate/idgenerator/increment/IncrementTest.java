package com.adobe.www.hibernate.idgenerator.increment;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class IncrementTest {
	private  static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		configuration.configure("cn/itcast/hibernate0909/idgenerator/increment/hibernate.cfg.xml");
		sessionFactory = configuration.buildSessionFactory();
	}
	
	/*
	 * 	Hibernate: select max(pid) from person
		Hibernate: insert into person (pname, psex, pid) values (?, ?, ?)
		  increment
		    hibernate内部会去查询该表中的主键的最大值，然后把最大值加1
	 */
	@Test
	public void testSavePerson(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();
		person.setPname("干露露");
		person.setPsex("腰");
		session.save(person);
		transaction.commit();
		session.close();
	}
}
