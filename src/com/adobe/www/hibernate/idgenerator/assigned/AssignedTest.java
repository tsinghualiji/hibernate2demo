package com.adobe.www.hibernate.idgenerator.assigned;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class AssignedTest {
	private  static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		configuration.configure("cn/itcast/hibernate0909/idgenerator/assigned/hibernate.cfg.xml");
		sessionFactory = configuration.buildSessionFactory();
	}
	
	@Test
	public void testSavePerson(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();
		person.setPid(5L);
		person.setPname("干露露");
		person.setPsex("腰");
		//session.save()只是把临时状态的对象放到持久状态。
		session.save(person);
		//commit才会产生sql语句。
		transaction.commit();
		session.close();
	}
}
