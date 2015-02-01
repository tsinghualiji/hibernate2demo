package com.adobe.www.hibernate.sessionfactory.cache;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.adobe.www.hibernate.onetomany.doubl.Classes;

/**
 * 二级缓存
 *    *  二级缓存是一个共享缓存
 *    *  在二级缓存中存放的数据是共享数据
 *    *  特性
 *        修改不能特别频繁
 *        数据可以公开
 *    *  二级缓存在sessionFactory中，因为sessionFactory本身是线程安全，所以二级缓存的数据线程也是安全的
 *    *  二级缓存的生命周期和sessionFactory一样
 *    *  怎么样把数据放入到二级缓存中
 *        通过session.get,session.load,session.update方法都可以把对象放入到二级缓存中
 *    *  怎么样把数据从二级缓存中提取
 *        通过session.get和session.load方法都可以提取二级缓存中的数据
 *    *  .....
 *    *  配置二级缓存
 *       *  在hibernate的配置文件中
 *           	<property name="cache.provider_class">
					org.hibernate.cache.EhCacheProvider
				</property>
				<property name="cache.use_second_level_cache">true</property>
	     *  在classes.hbm.xml文件中
	            <cache usage="read-write"/>
	     *  把二级缓存中的数据放到磁盘上
	        *  在classpath下存放一个文件ehcache.xml
	              <Cache
		            name="cn.itcast.hibernate0909.onetomany.doubl.Classes"
		            maxElementsInMemory="5" 
		            eternal="false"
		            timeToIdleSeconds="120"
		            timeToLiveSeconds="120"
		            overflowToDisk="true"
		            maxElementsOnDisk="10000000"
		            diskPersistent="false"
		            diskExpiryThreadIntervalSeconds="120"
		            memoryStoreEvictionPolicy="LRU"
		            />
	        *  当内存中的二级缓存存放够5个对象时，剩余的对象就放入到磁盘上 
 * @author Administrator
 *
 */
public class TwoCacheTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		//加载配置文件
		configuration.configure("cn/itcast/hibernate0909/sessionfactory/cache/hibernate.cfg.xml");
		//采用了工厂模式创建sessionFactory
		sessionFactory = configuration.buildSessionFactory();
	}
	/**
	 * session.get
	 *    *  把一个对象变成持久化状态的对象
	 *    *  把该对象存到session的一级缓存中
	 *    *  把该对象存到二级缓存中
	 *    *  session.clear清空一级和二级缓存
	 */
	@Test
	public void testGet(){
		Session session = sessionFactory.openSession();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		session.close();
		session = sessionFactory.openSession();
		classes = (Classes)session.get(Classes.class, 1L);
		System.out.println(classes.getCname());
		session.close();
	}
	/**
	 * 同上
	 */
	@Test
	public void testLoad(){
		Session session = sessionFactory.openSession();
		Classes classes = (Classes)session.load(Classes.class, 1L);
		classes.getCname();
		session.close();
		session = sessionFactory.openSession();
		classes = (Classes)session.get(Classes.class, 1L);
		System.out.println(classes.getCname());
		session.close();
	}
	
	/**
	 * session.update
	 *    session.update也要进入二级缓存
	 */
	@Test
	public void testUpdate(){
		Session session = sessionFactory.openSession();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		session.clear();
		session.update(classes);
		session.close();
		session = sessionFactory.openSession();
		classes = (Classes)session.get(Classes.class, 1L);
		session.close();
	}
	
	/**
	 * session.save
	 *   该方法只把对象放入到了一级缓存中，没有放入到二级缓存中
	 *   在执行session.flush的时候，把数据要保存到二级缓存中
	 */
	@Test
	public void testSave(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = new Classes();
		classes.setCname("0909java极品班");
		classes.setDescription("都是极品");
		session.save(classes);
		//transaction.commit();
		//session.flush();
		session.close();
		session = sessionFactory.openSession();
		classes = (Classes)session.get(Classes.class, classes.getCid());
		transaction.commit();
		session.close();
	}
	/**
	 * 把溢出的数据存放到磁盘上
	 */
	@Test
	public void testBatchSave()throws Exception{
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		for(int i=1;i<10;i++){
			Classes classes = new Classes();
			classes.setCname("0909java极品班"+i);
			classes.setDescription("都是极品"+i);
			session.save(classes);
		}
		transaction.commit();
		Thread.sleep(1000);
		session.close();
	}
	/**
	 * 查询缓存
	 *   * 查询缓存是建立在二级缓存基础之上的
	 *   * 查询缓存不是默认开启的，需要设置
	 *       <property name="cache.use_query_cache">true</property>
	 *   * 在代码中进行设置
	 *      query.setCacheable(true);
	 */
	@Test
	public void testQuery(){
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Classes");
		query.setCacheable(true);
		List<Classes> classesList = query.list();
		query = session.createQuery("from Classes");
		query.setCacheable(true);
		classesList = query.list();
		session.close();
	}
	
	/**
	 * session.createQuery("from Classes").list();
	 * session.createQuery("from Classes").iterate();
	 * 这两者有什么区别
	 *    *  发出sql语句的不同
	 *    *  利用缓存的不同
	 *    
	 * query.setFirstResult(4);
	   query.setMaxResults(5);
	      	这两个方法就是hibernate的分页  
	 * 
	 */
}
