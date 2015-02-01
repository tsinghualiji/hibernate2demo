package com.adobe.www.hibernate.onetomany.doubl;

import java.util.HashSet;
import java.util.Set;

import javax.print.attribute.HashAttributeSet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

/**
 * 1、保存班级
 * 2、保存学生
 * 3、保存班级的时候同时保存学生
 * 4、保存班级的时候同时保存学生，并且建立班级和学生之间的关系
 * 5、已经存在一个班级，新建一个学生，并且建立该学生和该班级之间的关系
 * 6、已经存在一个学生，新建一个班级，并且建立该学生和该班级之间的关系
 * 7、已经存在一个学生，已经存在一个班级，解除该学生和原来班级之间的关系，建立该学生和新班级之间的关系
 * 8、已经存在一个学生，解除该学生和该学生所在班级之间的关系
 * 9、解除该班级和所有的学生之间的关系，再重新建立该班级和一些新的学员之间的关系
 * 10、解除该班级和所有的学生之间的关系
 * 11、删除班级
 *      *
 *      	*  解除该班级和所有的学生之间的关系
 *      	*  删除该班级
 *      *   
 *          删除班级的同时删除学生
 * 12、删除学生
 *      同删除班级
 * @author Administrator
 *
 */
public class OneToManyDoubleTest {
	private static SessionFactory sessionFactory = null;
	static{
		Configuration  configuration = new Configuration();
		configuration.configure("cn/itcast/hibernate0909/onetomany/doubl/hibernate.cfg.xml");
		sessionFactory = configuration.buildSessionFactory();
	}
	/**
	 * 保存班级的时候同时保存学生
	 * 		Hibernate: select max(cid) from Classes
			Hibernate: select max(sid) from Student
			Hibernate: insert into Classes (cname, description, cid) values (?, ?, ?)
			Hibernate: insert into Student (sname, description, cid, sid) values (?, ?, ?, ?)
			Hibernate: update Student set cid=? where sid=?
			    	更新外键
		说明：
		  classes.setStudents(students);  通过classes来维护关系 ，要发出update语句，更新外键 
	 */
	@Test
	public void testSaveClasses_Cascade_S(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = new Classes();
		classes.setCname("aaaa");
		classes.setDescription("asdffd");
		Student student = new Student();
		student.setSname("李克强");
		student.setDescription("tttttt");
		Set<Student> students = new HashSet<Student>();
		students.add(student);
		//通过classes建立classes与students之间的关系
		classes.setStudents(students);
		//通过student建立classes与students之间的关系
		//student.setClasses(classes);
		session.save(classes);
		transaction.commit();
		session.close();
	}
	/**
	 * 	Hibernate: select max(sid) from Student
		Hibernate: select max(cid) from Classes
		Hibernate: insert into Classes (cname, description, cid) values (?, ?, ?)
		Hibernate: insert into Student (sname, description, cid, sid) values (?, ?, ?, ?)
	 		student.setClasses(classes);通过student来维护classes
	 		 对student的增、删、改本身就是对外键的操作，所以这里不再发出update语句
	 	    一对多，多的一方维护关系，效率比较高
	 */
	@Test
	public void testSaveStudent_Cascade_C(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = new Classes();
		classes.setCname("aaaa");
		classes.setDescription("asdffd");
		Student student = new Student();
		student.setSname("李克强");
		student.setDescription("tttttt");
		Set<Student> students = new HashSet<Student>();
		students.add(student);
		//通过classes建立classes与students之间的关系
		//classes.setStudents(students);
		//通过student建立classes与students之间的关系
		student.setClasses(classes);
		session.save(student);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个班级，新建一个学生，并且建立该学生和该班级之间的关系
	 */
	@Test
	public void testSaveStudent_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		Student student = new Student();
		student.setSname("里活命");
		student.setDescription("ufo");
		//通过student建立classes与student的关系
		student.setClasses(classes);
		session.save(student);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，新建一个班级，并且建立该学生和该班级之间的关系
	 *   通过分析：
	 *      *  因为存在建立关系的操作，所以操作学生端效率比较高
	 *      *  在这里存在保存班级的操作，所以应该是通过更新学生级联保存班级
	 */
	@Test
	public void testUpdateStudent_CasCade(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = (Student)session.get(Student.class, 2L);
		Classes classes = new Classes();
		classes.setCname("传智播客集团部");
		classes.setDescription("都是高官");
		student.setClasses(classes);
		session.save(classes);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，已经存在一个班级，解除该学生和原来班级之间的关系，建立该学生和新班级之间的关系
	 */
	@Test
	public void testRebuild_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = (Student)session.get(Student.class, 2L);
		Classes classes = (Classes)session.get(Classes.class, 1L);
		student.setClasses(classes);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，解除该学生和该学生所在班级之间的关系
	 */
	@Test
	public void testRealse_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = (Student)session.get(Student.class, 2L);
		student.setClasses(null);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 解除该班级和所有的学生之间的关系，再重新建立该班级和一些新的学员之间的关系
	 */
	@Test
	public void testRealse_Rebuild_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		/**
		 * 1、获取班级
		 * 2、获取该班级的所有的学生
		 * 3、遍历学生，把学生的班级设置为null
		 * 4、新建两个学员
		 * 5、建立两个学员与班级之间的关系
		 */
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
		 * 当发生transaction.commit的时候，hibernate内部会检查所有的持久化对象
		 *   会对持久化对象做一个更新,因为classes是一个持久化状态的对象，所以hibernate
		 *   内部要对classes进行更新，因为在classes.hbm.xml文件中<set name="students" cascade="all" inverse="true">
		 *   意味着在更新classes的时候，要级联操作student,而student是一个临时状态的对象
		 *   所以要对student进行保存，在保存student的时候，就把外键更新了
		 */
		transaction.commit();
		session.close();
	}
	
	/**
	 * 解除该班级和所有的学生之间的关系
	 */
	@Test
	public void testRealseAll_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		Set<Student> students = classes.getStudents();
		for(Student student:students){
			student.setClasses(null);
		}
		transaction.commit();
		session.close();
	}
	/**
	 * 先解除关系，再删除班级
	 */
	@Test
	public void testDeleteClass_1(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		Set<Student> students = classes.getStudents();
		for(Student student:students){
			student.setClasses(null);
		}
		session.delete(classes);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 在删除班级的时候，同时删除整个班级的学生
	 */

	/**
	 * 删除学生
	 *    直接删除学生即可
	 */
	
	@Test
	public void teetst(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		Set<Student> students = classes.getStudents();
		for(Student student:students){
				student.setClasses(null);
				break;
		}
		transaction.commit();
		session.close();
	}
}
