package com.adobe.www.hibernate.state;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.domain.Person;

/**
 * 对象的状态
 * 
 * @author Administrator
 * 
 */
public class StateTest {
	private static SessionFactory sessionFactory;
	static {
		Configuration configuration = new Configuration();
		configuration.configure();
		sessionFactory = configuration.buildSessionFactory();
	}

	@Test
	public void testUpdatePerson() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = (Person) session.get(Person.class, 6L);
//		person.setPname("aaa");
		session.update(person);
		transaction.commit();
		session.close();
	}

	/**
	 * 该程序发出了 Hibernate: select max(pid) from person
	 * 这条sql语句是在session.save(person);发出的 Hibernate: insert into
	 * hibernate0909.person (pname, psex, pid) values (?, ?, ?)
	 * 是在transaction.commit();发出的 两条sql语句
	 */
	@Test
	public void testSavePerson() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();
		person.setPname("abc");
		person.setPsex("asdf");
		session.close();
		session.save(person);
		transaction.commit();

	}

	@Test
	public void testSaveOrUpdate() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();// 临时状态
		person.setPname("abc");// 临时状态
		person.setPsex("asdf");// 临时状态
		// session.save(person);//临时状态转化成持久化状态
		person.setPname("aaaa");// 持久化状态
		session.save(person);// 无用，因为已经是持久化状态
		transaction.commit();// 同步到数据库中
		session.close();// 托管状态
	}

	@Test
	public void testUpdatePerson2() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = (Person) session.get(Person.class, 2L);
		person.setPname("aaa");
		/**
		 * 在执行session.update的时候， hibernate内部会为person对象去对照内存快照（副本），如果属性发生改变才要执行
		 * update语句，如果没有改变，则不发出update语句
		 */
		session.update(person);
		transaction.commit();// 并没有发出update语句
		session.close();
	}

	/**
	 * 如果执行session.save方法，hibernate内部会检查主键是否存在，如果存在，则不管
	 * 如果执行session.update方法，看属性是否发生变化，如果发生变化，则执行update语句，如果没有发生改变，则不执行
	 * 在hibernate内部，是通过标识符来判断一个对象在数据库中是否有值
	 */
	@Test
	public void testUpdate3() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();
		person.setPid(2L);
		session.update(person);
		transaction.commit();
		session.close();
	}

	@Test
	public void testSavePerson3() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person person = new Person();// 临时状态
		person.setPname("abc");
		person.setPsex("asdf");
		session.save(person);// 持久化状态
		transaction.commit();
		session.close();// 当session关闭以后，事务环境不存在了
		session = sessionFactory.openSession();// 打开了一个新的session,这个时候的session并没有保存person对象
		Transaction transaction2 = session.beginTransaction();
		session.update(person);// person中的id已经有值了
		transaction2.commit();
		session.close();
	}

	@Test
	public void testaa() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Person per = new Person();
		//Person per = (Person)session.get(Person.class, 1L);
		per.setPid(1L);
		per.setPsex("男");
		per.setPname("Steven");
		session.update(per);
		session.getTransaction().commit();
		// 数据库中id、name、sex全都一样，为什么还执行update语句？
		session.close();

	}
}
