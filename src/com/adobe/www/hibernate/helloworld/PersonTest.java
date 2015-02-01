package com.adobe.www.hibernate.helloworld;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.domain.Person;

public class PersonTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		//加载配置文件
		configuration.configure();
		//采用了工厂模式创建sessionFactory
		sessionFactory = configuration.buildSessionFactory();
	}
	/**
	 * 
	 */
	@Test
	public void testSavePerson(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();
		//由于在映射文件中已经说明主键的产生方式是hibernate内部产生，所以在程序中不用设置主键
		person.setPid(3L);
		person.setPname("王二麻子");
		person.setPsex("纯爷们");
		session.save(person);
		transaction.commit();
		session.close();
	}
	/**
	 * 1、必须从数据库中提取出数据再进行修改
	 * 2、session.get方法创建的对象利用的是默认的构造函数
	 */
	@Test
	public void testUpdatePerson(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		/**
		 * 1、先把修改的那行提取出来
		 * 说明：
		 * 		Serializable是String和包装类的共同的父类   
		 */
		Person person = (Person)session.get(Person.class,1L);
		person.setPsex("不详");
		session.update(person);
		
		/**
		 * 新创建一个person对象
		 * 把pid为1的值设置进去
		 * 修改对象
		 *    这种修改方式不推荐
		 */
//		Person person2 = new Person();
//		person2.setPid(1L);
//		person2.setPsex("女");
		//session.update(person2);
		transaction.commit();
		session.close();
	}
	
	@Test
	public void testQuery(){
		Session session = sessionFactory.openSession();
		List<Person> personList = session.createQuery("from Person").list();
		System.out.println(personList.size());
	}
	/**
	 * 有两种方案
	 */
	@Test
	public void testDelete(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = (Person)session.get(Person.class, 1L);
		//Person person = new Person();
		//person.setPid(1L);
		session.delete(person);
		transaction.commit();
		session.close();
	}
	
	@Test
	public void testQueryPersonByID(){
		Session session = sessionFactory.openSession();
		Person person = (Person)session.get(Person.class, 1);
		System.out.println(person.getPname());
	}
}
