package com.adobe.www.hibernate.idgenerator.identity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class IdentityTest {
	private  static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		configuration.configure("cn/itcast/hibernate0909/idgenerator/identity/hibernate.cfg.xml");
		sessionFactory = configuration.buildSessionFactory();
	}
	
	/**
	 * Hibernate: insert into person (pname, psex) values (?, ?)
	 *   因为没有发出select max语句，所以identity的效率比increment要高
	 *   主键不连贯
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
