package com.adobe.www.hibernate.session;

import java.lang.annotation.Target;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.onetomany.doubl.Classes;

/**
 * *  hibernate中的crud操作是利用session来完成的
 * *  hibernate中的事务是依赖于session环境的
 * *  session的产生的方式
 *      *  sessionFactory.openSession
 *         每次都会新创建一个session,只要新创建一个session,hibernate都会打开应用程序和数据库的连接
 *         所以这种方式效率比较低
 *      *  sessionFactory.getCurrentSession
 *         *  如果当前线程中没有session,先openSession，然后把session存放到当前线程中
 *         *  从当前线程中得到session
 *         *  crud操作必须有事务环境
 *         *  不用手动去close掉
 * *  session的一级缓存
 * @author Administrator
 */
public class SessionTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		//加载配置文件
		configuration.configure("cn/itcast/hibernate0909/session/hibernate.cfg.xml");
		//采用了工厂模式创建sessionFactory
		sessionFactory = configuration.buildSessionFactory();
	}
	
	
	private void testSession(String name){
		Session session = sessionFactory.getCurrentSession();
		//Transaction transaction = session.beginTransaction();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		//classes.setCname(name);
		//transaction.commit();
		//session.close();
	}
	
	@Test
	public void test(){
		SessionTest sessionTest = new SessionTest();
		sessionTest.testSession("aaa");
	}
}
