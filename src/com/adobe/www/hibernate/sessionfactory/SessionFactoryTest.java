package com.adobe.www.hibernate.sessionfactory;
/**
 * 1、在sessionFactory中存放着配置文件和所有的映射文件
 * 2、sessionFactory是一个重量级别的类
 * 3、一个数据库只能有一个sessionFactory
 * 4、一个配置文件只能连接一个数据库
 * 5、只要创建了sessionFactory,表就创建完毕了
 * 6、sessionFactory中存放的数据是共享数据，但是这个类本身是线程安全的
 * 7、SessionFactory实际上是接口，实现了是SessionFactoryImpl
 * @author Administrator
 *
 */
public class SessionFactoryTest {

}
