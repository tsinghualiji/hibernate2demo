package com.adobe.www.hibernate.session.cache;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.onetomany.doubl.Classes;
import com.adobe.www.hibernate.onetomany.doubl.Student;

/**
 * 1、一级缓存的生命周期
 *     一级缓存在session中存放，只要打开session,一级缓存就存在了，当session关闭的时候,一级缓存就不存在了
 * 2、一级缓存是依赖于谁存在的
 *     依赖于session存在的
 * 3、怎么样把数据存放到一级缓存中
 *     利用session.get、update、save
 * 4、怎么样从一级缓存中获取数据
 *     利用session.get方法可以获取数据
 * 5、怎么样把缓存中的数据同步到数据库
 *     * 只要是一个持久化状态的数据就在一级缓存中
 *     * 利用session.flush方法
 * 6、怎么样把数据库的数据同步到一级缓存中
 *     session.reflesh方法   只能同步一个对象
 * 7、一级缓存的特性
 *    也叫session级别的缓存
 *    session中存放私有数据
 *    可以通过新建session和从当前线程中获取session保证数据的安全性
 * 8、从一级缓存中清楚某一个对象
 *    session.evcit方法  并且把一个对象从持久化状态转化成脱管状态
 * 9、清空一级缓存中所有的数据
 * @author Administrator
 *
 */
public class OneCacheTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		//加载配置文件
		configuration.configure("cn/itcast/hibernate0909/session/hibernate.cfg.xml");
		//采用了工厂模式创建sessionFactory
		sessionFactory = configuration.buildSessionFactory();
	}
	/**
	 * session.get方法
	 *     * 通过该方法得到的对象是一个持久化对象
	 *     * 通过该方法可以把该对象存放到一级缓存中(session缓存中)
	 */
	@Test
	public void testGet(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		session.get(Classes.class, 1L);
		transaction.commit();
	}
	
	/**
	 * session.save方法
	 *    *  可以把一个对象变成持久化状态的对象
	 *    *  可以把一个对象放入到一级缓存中
	 */
	@Test
	public void testSave(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = new Classes();
		classes.setCname("极品班");
		classes.setDescription("极品人士：例如干露露");
		session.save(classes);
		Classes classes2 = (Classes)session.get(Classes.class, classes.getCid());
		transaction.commit();
	}
	
	/**
	 * session.update方法
	 *    *  能把一个对象的状态转化成持久化状态
	 *    *  能把一个对象存放到一级缓存中
	 */
	@Test
	public void testUpdate(){
		/**
		 * 1、利用get方法获取
		 * 2、利用session.evict方法清空
		 * 3、再利用update方法
		 * 4、再用get方法获取
		 */
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 2L);
		session.evict(classes);
		classes.setDescription("aaa");
		session.update(classes);
		Classes classes2 = (Classes)session.get(Classes.class,2L);
		transaction.commit();
	}
	
	@Test
	public void testFlush(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		//持久化状态的数据
		Classes classes = (Classes)session.get(Classes.class, 1L);
		Set<Student> students = classes.getStudents();
		for(Student student:students){
			student.setClasses(null);
		}
		Student student = new Student();
		student.setSname("王二麻子");
		student.setDescription("爷们");
		Student student2 = new Student();
		student2.setSname("王二麻子的哥");
		student2.setDescription("爷们的哥");
		//student.setClasses(classes);
		//student2.setClasses(classes);
		students.add(student);
		students.add(student2);
		/**
		 * session.flush方法
		 *   *  hibernate内部会去检查session缓存中的所有的对象
		 *   *  如果该对象是持久化对象，并且该对象的ID在数据库中有对应的记录，并且该对象的属性
		 *      有变化，则会自动发出update语句，如果该对象的属性没有变化，则不发出update语句
		 *   *  检查持久化对象中是否有关联的对象，如果有关联的对象，并且设置了级联操作，这个时候
		 *      会检查级联对象的id在数据库中有没有对应的记录，如果有，则发出update语句，如果没有，则
		 *      发出insert语句
		 *   *  如果有维护关系的代码，则还会改变关系
		 *   *  全部检查完成以后，就发出sql语句，把一级缓存中的内容同步到数据库中
		 */
		session.flush();
		transaction.commit();
		session.close();
	}
	
	@Test
	public void testFlush2(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Student student1 = (Student)session.get(Student.class, 1L);
		Student student2 = (Student)session.get(Student.class, 2L);
		Student student3 = (Student)session.get(Student.class, 3L);
		Student student4 = (Student)session.get(Student.class, 4L);
		student1.getClasses().setCname("aaa");
		student2.getClasses().setDescription("asdf");
		session.flush();
		transaction.commit();
	}
	
	@Test
	public void testReflesh(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		classes.setCname("bbb");
		//把一个对象对应的数据库的数据重新更新到一级缓存中
		session.refresh(classes);
		
		session.clear();//清空一级缓存中的所有的数据
		classes = (Classes)session.get(Classes.class, 1L);
		System.out.println(classes.getCname());
		transaction.commit();
	}
	/**
	 * 批量操作
	 */
	@Test
	public void testBatchSave(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		for(int i=0;i<100000;i++){
			if(i%50==0){
				session.flush();
				session.clear();
			}
			Classes classes = new Classes();
			classes.setCname(i+"aaa");
			session.save(classes);
		}
		transaction.commit();
	}
}
