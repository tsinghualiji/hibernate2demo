package com.adobe.www.hibernate.idgenerator.uuid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class UUIDTest {
	private  static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		configuration.configure("cn/itcast/hibernate0909/idgenerator/uuid/hibernate.cfg.xml");
		sessionFactory = configuration.buildSessionFactory();
	}
	
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
