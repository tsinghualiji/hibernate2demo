package com.adobe.www.hibernate.onetomany.single;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.domain.Person;

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
public class OneToManyTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration  configuration = new Configuration();
		configuration.configure("cn/itcast/hibernate0909/onetomany/single/hibernate.cfg.xml");
		sessionFactory = configuration.buildSessionFactory();
	}
	
	@Test
	public void testSaveClasses(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = new Classes();
		classes.setCname("0909java极品就业班");
		classes.setDescription("java学得就是好");
		session.save(classes);
		transaction.commit();
		session.close();
	}
	/**
	 * 这个操作只能插入一student对象，但是在student表中，外键任然为null
	 */
	@Test
	public void testSaveStudent(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = new Student();
		student.setSname("班长");
		student.setDescription("有班秘");
		session.save(student);
		transaction.commit();
		session.close();
	}

	/**
	 * 保存班级的时候同时保存学生
	 */
	@Test
	public void testSaveClassAndStudent(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = new Student();
		student.setSname("班秘");
		student.setDescription("班长旁边的人,男女不限，女士优先");
		session.save(student);
		
		Classes classes = new Classes();
		classes.setCname("0909java精品班");
		classes.setDescription("还是极品好");
		session.save(classes);
		transaction.commit();
		session.close();
	}
	/**
	 * 在保存班级的同时，级联保存学生
	 * 		Hibernate: select max(cid) from Classes
			Hibernate: select max(sid) from Student
			Hibernate: insert into Classes (cname, description, cid) values (?, ?, ?)
			Hibernate: insert into Student (sname, description, sid) values (?, ?, ?)
			Hibernate: update Student set cid=? where sid=?
	 */
	@Test
	public void testSaveClass_Cascade_Student(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = new Student();
		student.setSname("班秘");
		student.setDescription("班长旁边的人,男女不限，女士优先");
		
		
		Set<Student> students = new HashSet<Student>();
		
		students.add(student);
		
		Classes classes = new Classes();
		classes.setCname("0808java几品班");
		classes.setDescription("还是极品好");
		
		classes.setStudents(students);
		session.save(classes);
		
		transaction.commit();
		session.close();
	}
	
	/**
	 * 保存班级的时候同时保存学生，并且建立班级和学生之间的关系
	 * 		在classes.hbm.xml文件中
	 *         <set name="students" cascade="save-update">
	 *        inverse的值为默认或者false,才能让classes维护student的关系
	 */
	@Test
	public void testSaveClassesAndStudent_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = new Student();
		student.setSname("班秘");
		student.setDescription("班长旁边的人,男女不限，女士优先");
		
		
		Set<Student> students = new HashSet<Student>();
		
		students.add(student);
		
		Classes classes = new Classes();
		classes.setCname("0808java几品班");
		classes.setDescription("还是极品好");
		
		classes.setStudents(students);
		session.save(classes);
		
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个班级，新建一个学生，并且建立该学生和该班级之间的关系
	 * 		Hibernate: select classes0_.cid as cid0_0_, classes0_.cname as cname0_0_, classes0_.description as descript3_0_0_ from Classes classes0_ where classes0_.cid=?
			Hibernate: select students0_.cid as cid0_1_, students0_.sid as sid1_, students0_.sid as sid1_0_, students0_.sname as sname1_0_, students0_.description as descript3_1_0_ from Student students0_ where students0_.cid=?
				classes.getStudents()的时候发出的
			Hibernate: select max(sid) from Student
			Hibernate: insert into Student (sname, description, sid) values (?, ?, ?)
			Hibernate: update Student set cid=? where sid=?
	 			建立关系
	 */
	@Test
	public void testSaveStudent_R(){
		/**
		 * 1、查询班级
		 * 2、新建学生
		 * 3、建立关系
		 */
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = new Student();
		student.setSname("习近平");
		student.setDescription("昨天正式宣布");
		
		Classes classes = (Classes)session.get(Classes.class, 1L);
		//建立班级和学生之间的关系
		classes.getStudents().add(student);
		//classes.setStudents(students)  把班级中的学生重新更新了
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，新建一个班级，并且建立该学生和该班级之间的关系
	 * 		Hibernate: select student0_.sid as sid1_0_, student0_.sname as sname1_0_, student0_.description as descript3_1_0_ from Student student0_ where student0_.sid=?
			Hibernate: select max(cid) from Classes
			Hibernate: insert into Classes (cname, description, cid) values (?, ?, ?)
			Hibernate: update Student set cid=? where sid=?
	 */
	@Test
	public void testSaveClasses_R(){
		/**
		 * 1、查找学生
		 * 2、新建班级
		 * 3、建立关系 
		 */
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = (Student)session.get(Student.class, 2L);
		Classes classes = new Classes();
		classes.setCname("政治局肠胃班");
		classes.setDescription("贪污腐败的根源");
		Set<Student> students = new HashSet<Student>();
		students.add(student);
		classes.setStudents(students);
		session.save(classes);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 	Hibernate: select max(cid) from Classes
		Hibernate: insert into Classes (cname, description, cid) values (?, ?, ?)
		Hibernate: update Student set sname=?, description=? where sid=?
		Hibernate: update Student set cid=? where sid=?
	 */
	@Test
	public void testSaveClasses_R_2(){
		/**
		 * 1、查找学生
		 * 2、新建班级
		 * 3、建立关系 
		 */
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = new Student();
		student.setSid(2L);
		Classes classes = new Classes();
		classes.setCname("政治局肠胃班");
		classes.setDescription("贪污腐败的根源");
		Set<Student> students = new HashSet<Student>();
		students.add(student);
		classes.setStudents(students);
		session.save(classes);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 说明：   
	 *     当Classes.hbm.xml映射文件中：
	 *        <set name="students" cascade="save-update">
	 *      当session.save/update classes时，隐式操作的对象是student,
	 *      对student决定save操作还是update操作，主要的判断依据是student中sid值在数据库中有没有对应的值
	 */
	
	/**
	 * 已经存在一个学生，已经存在一个班级，解除该学生和原来班级之间的关系，建立该学生和新班级之间的关系
	 * 		Hibernate: select classes0_.cid as cid0_0_, classes0_.cname as cname0_0_, classes0_.description as descript3_0_0_ from Classes classes0_ where classes0_.cid=?
			Hibernate: select student0_.sid as sid1_0_, student0_.sname as sname1_0_, student0_.description as descript3_1_0_ from Student student0_ where student0_.sid=?
			Hibernate: select classes0_.cid as cid0_0_, classes0_.cname as cname0_0_, classes0_.description as descript3_0_0_ from Classes classes0_ where classes0_.cid=?
			Hibernate: select students0_.cid as cid0_1_, students0_.sid as sid1_, students0_.sid as sid1_0_, students0_.sname as sname1_0_, students0_.description as descript3_1_0_ from Student students0_ where students0_.cid=?
			Hibernate: select students0_.cid as cid0_1_, students0_.sid as sid1_, students0_.sid as sid1_0_, students0_.sname as sname1_0_, students0_.description as descript3_1_0_ from Student students0_ where students0_.cid=?
			Hibernate: update Student set cid=null where cid=?  解除关系
			Hibernate: update Student set cid=? where sid=?   建立关系
			  只需要再重新建立关系即可
	 */
	@Test
	public void testRealse_Rebuild(){
		/**
		 * 1、查找班级
		 * 2、查找学生
		 * 3、解除关系
		 * 4、建立关系
		 */
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		//Classes classes = (Classes)session.get(Classes.class, 5L);
		Student student = (Student)session.get(Student.class, 2L);
		Classes classes2 = (Classes)session.get(Classes.class, 5L);
		//classes.getStudents().remove(student);//解除关系
		classes2.getStudents().add(student);//建立关系
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，解除该学生和该学生所在班级之间的关系
	 * 	Hibernate: select student0_.sid as sid1_0_, student0_.sname as sname1_0_, student0_.description as descript3_1_0_ from Student student0_ where student0_.sid=?
		Hibernate: select classes0_.cid as cid0_0_, classes0_.cname as cname0_0_, classes0_.description as descript3_0_0_ from Classes classes0_ where classes0_.cid=?
		Hibernate: select students0_.cid as cid0_1_, students0_.sid as sid1_, students0_.sid as sid1_0_, students0_.sname as sname1_0_, students0_.description as descript3_1_0_ from Student students0_ where students0_.cid=?
		Hibernate: update Student set cid=null where cid=?
		   解除关系的语句
	 */
	@Test
	public void testRealse_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		/**
		 * 1、查询该学生
		 * 2、查询该班级
		 * 3、解除关系
		 */
		Student student = (Student)session.get(Student.class, 1L);
		Classes classes = (Classes)session.get(Classes.class, 1L);
		classes.getStudents().remove(student);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 解除该班级和所有的学生之间的关系，再重新建立该班级和一些新的学员之间的关系
	 */
	@Test
	public void testRealseAll_Rebuild_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 5L);
		List<Student> students = session.createQuery("from Student where sid in(3,4,5)").list();
		//重新建立班级和学生之间的关系
		classes.setStudents(new HashSet<Student>(students));
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
		Classes classes = (Classes)session.get(Classes.class,5L);
		classes.setStudents(null);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 删除班级
	 *   1、解除该班级和所有的学生之间的关系
	 *       如果班级具体维护关系的能力，hibernate内部自动实现
	 *   2、删除班级
	 */
	@Test
	public void testDeleteClasses(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		session.delete(classes);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 删除班级,同时删除学生
	 * 		Hibernate: select classes0_.cid as cid0_0_, classes0_.cname as cname0_0_, classes0_.description as descript3_0_0_ from Classes classes0_ where classes0_.cid=?
			Hibernate: select students0_.cid as cid0_1_, students0_.sid as sid1_, students0_.sid as sid1_0_, students0_.sname as sname1_0_, students0_.description as descript3_1_0_ from Student students0_ where students0_.cid=?
			Hibernate: update Student set cid=null where cid=?
			    解除班级和学生之间的关系
			Hibernate: delete from Student where sid=?
			Hibernate: delete from Classes where cid=?
	 */
	@Test
	public void testDeleteClasses_Cascade(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		session.delete(classes);
		transaction.commit();
		session.close();
	}
	
	
	/**
	 * 总结：
	 *   1、在整个例子中，班级负责维护关系，只要班级维护关系就会发出update语句
	 *   2、解除关系就是相对应的外键设置为null
	 *   3、改变关系就是相对应的外键从一个值变成另外一个值
	 *   4、在代码中的体现：
	 *   	classes.setStudents();  重新建立关系
	 *      classes.getStudents().remove;  解除关系
	 *      classes.setStudents(null); 解除所有的关系
	 *      classes.getStudents().add() 在原有的关系的基础上再建立关系
	 */
}
