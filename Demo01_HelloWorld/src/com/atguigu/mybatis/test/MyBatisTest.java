package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 1、接口式编程
 * 	原生：		Dao		====>  DaoImpl
 * 	mybatis：	Mapper(只是接口)	====>  xxMapper.xml(配置文件,相当于对Mapper的实现)
 * 
 * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 3、SqlSession和connection一样她都是非线程安全。每次使用都应该去获取新的对象。
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		（将接口和xml进行绑定）
 * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 * 		1).mybatis的全局配置文件(mybatis-config.xml)：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		[有方法不用xml配置文件获取sqlSessionFactory,文档有]
 * 		2).sql映射文件(必须有)：保存了每一个sql语句的映射信息：
 * 					将sql抽取出来。
 */
public class MyBatisTest {
	/**
	 * 1、根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象 有数据源和一些运行环境信息
	 * 2、sql映射文件(EmployeeMapper.xml)；配置了每一个sql，以及sql的封装规则等。
	 * 3、将sql映射文件注册在全局配置文件(mybatis-config.xml)中
	 * 4、写代码：
	 * 		1）、根据全局配置文件得到SqlSessionFactory；
	 * 		2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
	 * 			一个sqlSession就是代表和数据库的一次会话，用完关闭
	 * 		3.1)[旧版本的sql映射文件]、使用sql的唯一标志(一般用sql映射文件的namespace和唯一标志id共同确定id)来告诉MyBatis执行哪个sql。
	 * 		sql语句都是保存在sql映射文件中的。
	 * 		3.2)[新版本sql映射文件+绑定的接口]. 获取接口的实现类对象.使用sqlSession对象为接口自动的创建一个代理对象，代理对象去执行增删改查方法
	 */

	//根据全局配置文件 获取 SqlSessionFactory
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	//新版本 sql映射文件  的使用
	@Test
	public void test01() throws IOException {
		// 1、获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		// 2、获取sqlSession对象
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			// 3、获取接口的实现类对象
			//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());//class com.sun.proxy.$Proxy4
			System.out.println(employee);
		} finally {
			openSession.close();
		}
	}

	//也是调用 老版本sql映射文件(和接口绑定使用).[其实虽然,绑定了接口,但是由于调用的过程还是用唯一标识符,所以还是通过旧版本的方式调用的]
	@Test
	public void test() throws IOException {

		// 2、获取sqlSession实例，能直接执行已经映射的sql语句
		// sql的唯一标识：statement Unique identifier matching the statement to use.
		// 执行sql要用的参数：parameter A parameter object to pass to the statement.
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			Employee employee = openSession.selectOne(
					"com.atguigu.mybatis.dao.EmployeeMapper.getEmpById", 1);
			System.out.println(employee);
		} finally {
			openSession.close();
		}
	}

	//这种调用的是 老版本 sql映射文件(没有和接口绑定使用) [EmployeeMapper.xml中的注释部分就是老版本sql映射文件]
	@Test
	public void test03() throws IOException {

		// 2、获取sqlSession实例，能直接执行已经映射的sql语句
		// sql的唯一标识：statement Unique identifier matching the statement to use.
		// 执行sql要用的参数：parameter A parameter object to pass to the statement.
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			Employee employee = openSession.selectOne(
					"com.atguigu.mybatis.EmployeeMapper.selectOne", 1);
			System.out.println(employee);
		} finally {
			openSession.close();
		}
	}
}
