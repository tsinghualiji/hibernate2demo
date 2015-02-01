package com.adobe.www.hibernate.hql;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

/**
 * hql针对查询
 * @author Administrator
 *
 */
public class HQLTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		//加载配置文件
		configuration.configure("cn/itcast/hibernate0909/hql/hibernate.cfg.xml");
		//采用了工厂模式创建sessionFactory
		sessionFactory = configuration.buildSessionFactory();
	}
	/***********************************************************************************/
	     /**
	      * 单表
	      */
	/**
	 * 查询classes表中所有的数据
	 */
	@Test
	public void testClasses_All(){
		Session session = sessionFactory.openSession();
		List<Classes> classesList = session.createQuery("from Classes").list();
		System.out.println(classesList.size());
		session.close();
	}
	/**
	 * 按照需求查询数据，但是结构不怎么样
	 */
	@Test
	public void testClasses_Select(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("select cid,cname from Classes").list();
		classesList.size();
		session.close();
	}
	
	@Test
	public void testClasses_Select_Constructor(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("select new cn.itcast.hibernate0909.hql.Classes(cid,cname) from Classes").list();
		classesList.size();
		session.close();
	}
	
	@Test
	public void testClasses_Select_OrderBy(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("from Classes order by cid").list();
		classesList.size();
		session.close();
	}
	
	/******************************************************************************************/
		/**
		 * 一对多
		 * 		1、等值连接
		 * 		2、内连接
		 * 		3、左外连接
		 * 		4、迫切左外连接
		 * 		5、迫切右外连接
		 *      6、带构造函数的select与迫切的关系
		 */
	/**
	 * 等值连接
	 */
	@Test
	public void testSelect_Classes_Student_EQ(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("from Classes c,Student s where c.cid=s.classes.cid").list();
		classesList.size();
		session.close();
	}
	/**
	 * 迫切内连接
	 */
	@Test
	public void testSelect_Classes_Student_EQ_FETCH(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("from Classes c inner join fetch c.students s").list();
		classesList.size();
		session.close();
	}
	
	/**
	 * 迫切内连接
	 */
	@Test
	public void testSelect_Student_Classes_EQ_FETCH(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("from Student s inner join fetch s.classes c").list();
		classesList.size();
		session.close();
	}
	
	@Test
	public void testSelect_Classes_Student_Left(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("from Classes c left outer join c.students s").list();
		classesList.size();
		session.close();
	}
	
	@Test
	public void testSelect_Classes_Student_Left_Fetch(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("from Classes c left outer join fetch c.students s").list();
		classesList.size();
		session.close();
	}
	
	/**
	 * 在页面上显示cname,sname
	 *    *  采用迫切内连接可以做
	 *       这样做不好的一点就是把数据库中两张表的所有的字段全部放入到内存中
	 *    *  带构造函数的select查询和fetch只能选择其中一个
	 */
	@Test
	public void testSelect_Constructor_Classes_Student_Inner(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("select new cn.itcast.hibernate0909.hql.ClassesView(c.cname,s.sname) " +
				                                  "from Classes c inner join c.students s").list();
		classesList.size();
		session.close();
	}
	
	@Test
	public void testSelect_Constructor_Classes_Student_left(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("select new cn.itcast.hibernate0909.hql.ClassesView(c.cname,s.sname) " +
				                                  "from Classes c left join c.students s").list();
		classesList.size();
		session.close();
	}
	
	/*********************************************************************************************/
	/**
	 * 多对多
	 * 		1、等值连接
	 * 		2、内连接
	 * 		3、左外连接
	 * 		4、迫切左外连接
	 * 		5、迫切右外连接
	 *      6、带构造函数的select与迫切的关系
	 */
	@Test
	public void testSelect_Inner(){
		Session session = sessionFactory.openSession();
		List classesList = session.createQuery("from Course c inner join c.students s").list();
		classesList.size();
		session.close();
	}
	
	@Test
	public void testCreateDB(){
		
	}
	/*********************************************************************************************/
		/**
		 * 一对多和多对多
		 */
		/**
		 * 查询所有的班级的所有的学生，所有的学生的所有的课程
		 */
		@Test
		public void testQueryAll(){
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("from");
			stringBuffer.append(" Classes c left outer join fetch c.students s left outer join fetch s.courses cc");
			Session session = sessionFactory.openSession();
			List<Classes> classesList = session.createQuery(stringBuffer.toString()).list();
			/**
			 * 如果list中有重复的元素，转化成set集合，就可以去掉重复的元素
			 */
			for(Classes classes:classesList){
				Set<Student> students = classes.getStudents();
				for(Student student:students){
					Set<Course> courses = student.getCourses();
					for(Course course:courses){
						System.out.println(course.getCname());
					}
				}
			}
			session.close();
		}
	/*********************************************************************************************/

	/**
	 * 1、要求用迫切左外连接查询所有的学生，并且把有课程的学生的课程查询出来
	 * 2、只需要查询cname,sname
	 * 3、采用内连接查询有课程的学生和有学生的课程
	 * 4、用迫切内连接把classes和student和course三张表的数据联系在一起，如果有重复的数据去掉重复
	 */
		/**
		 * 1、要求用迫切左外连接查询所有的学生，并且把有课程的学生的课程查询出来
		 * 2、只需要查询cname,sname
		 * 3、采用内连接查询有课程的学生和有学生的课程
		 * 4、用迫切内连接把classes和student和course三张表的数据联系在一起，如果有重复的数据去掉重复
		 */
			
//			1、要求用迫切左外连接查询所有的学生，并且把有课程的学生的课程查询出来
			
	@Test
	public void test1(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
//	SELECT s.sid,s.sname,s.description,cc.cname FROM course cc LEFT JOIN student s ON cc.cid=s.sid
		List list = session.createQuery("from Course c left join fetch c.students").list();
		System.out.println(list.size());
		
		
		transaction.commit();
		session.close();
		
	}
	
//			2、只需要查询cname,sname
	@Test
	public void test2(){
		
	}
	
//			3、采用内连接查询有课程的学生和有学生的课程
	@Test
	public void test3(){
		
	}
	
//			4、用迫切内连接把classes和student和course三张表的数据联系在一起，如果有重复的数据去掉重复
	@Test
	public void test4(){
		
	}
	
	/**
	 * session.createQuery(stringBuffer.toString()).list();
	 * classesList.size();session.createQuery("from d").iterate()
	 * 有什么区别
	 * 	从 发出的语句 怎么不同
	 * 	利用缓存的不同?
	 */
	
	/**
	 * query.setFirstResult(4)
		query.setMaxResults(5)
		实现后台分页
	 */
}
