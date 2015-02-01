package com.adobe.www.hibernate.lazy;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.onetomany.doubl.Classes;
import com.adobe.www.hibernate.onetomany.doubl.Student;

/**
 * 懒加载
 *    针对数据库中的大数据，不希望特别早的加载到内存中，当用到它的时候才加载
 *    类的懒加载
 *    集合的懒加载   
 *       true
 *       false
 *       extra 
 *    单端关联的懒加载
 *       多对一
 * @author Administrator
 *
 */
public class LazyTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		//加载配置文件
		configuration.configure("cn/itcast/hibernate0909/onetomany/doubl/hibernate.cfg.xml");
		//采用了工厂模式创建sessionFactory
		sessionFactory = configuration.buildSessionFactory();
	}
	/**
	 * 类的懒加载
	 *    *  在默认情况下，类就是执行懒加载
	 *    *  只有使用了load方法以后才能用懒加载
	 *    *  如果在相应的映射文件中，设置<class>的lazy="false"懒加载将失去效果
	 */
	@Test
	public void testLoad(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.load(Classes.class, 1L);
		String cname = classes.getCname();//发出sql语句
		System.out.println(cname);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 集合的懒加载
	 *   针对一多对的情况或者多对多的情况
	 *     根据一方加载set集合，决定在什么时候给set集合填充数据
	 *     true
	 *        在遍历集合中的每一个元素的时候发出sql语句
	 *     false
	 *        在得到集合的时候，发出sql语句
	 *     extra
	 *        students.size()这个时候用extra仅仅能够得到大小
	 */
	@Test
	public void testCollectionLazy(){
		Session session = sessionFactory.openSession();
		Classes classes = (Classes)session.load(Classes.class, 2L);
		Set<Student> students = classes.getStudents();
		System.out.println(students.size());
		session.close();
	}
	
	/**
	 * 单端关联
	 *   根据多的一方加载一的一方
	 *     false
	 *     proxy  就相当于true
	 *     no-proxy
	 *      根据多的一方加载一的一方数据量特别少，所以怎么样都行
	 */
	
}
