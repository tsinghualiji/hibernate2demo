package com.adobe.www.hibernate.state;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.domain.Person;

public class StateExam {
	private static SessionFactory sessionFactory;
	static {
		Configuration configuration = new Configuration();
		configuration.configure();
		sessionFactory = configuration.buildSessionFactory();
	}
	/**
	 * 利用session.createQuery("from Person").list方法把person表的所有的数据全部提取出来
   			然后遍历list,把person中的属性做一些改变，最后提交事务，关闭session.
	 */
	@Test
	public void testEx01(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		List<Person> personList = session.createQuery("from Person").list();
		for(Person person:personList){
			person.setPname("干露露");//person已经是持久化对象的状态
		}
		transaction.commit();
		session.close();
	}
	/**
	 * 新创建一个person对象，执行save方法，再关闭session,
   		再重新打开一个session,让person对象的属性值发生改变，把改变后的结果同步到数据库中
	 */
	@Test
	public void testEx02(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();
		person.setPname("干露露的妈");
		person.setPsex("腰");
		session.save(person);
		transaction.commit();
		session.close();
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		person.setPsex("老妖");
		session.update(person);
		transaction.commit();
		session.close();
	}
}
