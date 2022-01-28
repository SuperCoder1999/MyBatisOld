package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapper;
import com.atguigu.mybatis.dao.EmployeeMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBatisTest {

	//根据全局配置文件 获取 SqlSessionFactory
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	//测试 查询一条记录
	@Test
	public void testGetEmpById() throws IOException {
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

	/**
	 * 测试增删改
	 * 1、mybatis允许增删改直接定义以下类型返回值
	 * 		Integer、Long、Boolean、void
	 * 2、我们需要手动提交数据
	 * 		sqlSessionFactory.openSession();===》手动提交
	 * 		sqlSessionFactory.openSession(true);===》自动提交
	 */
	@Test
	public void testAdd() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//openSession()这个方法被重载.可以传入一个boolean类型值,意思是自动提交
		//如果没有传入,用的是手动提交.需要在所有操作完成后,手动提交
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
			//测试添加
			Employee employee = new Employee(null, "jerry2","123", "1");
			Long res = employeeMapper.addEmp(employee);
			System.out.println(res);
			//获取自增主键值,传到JavaBean中
			System.out.println(employee.getId());
			//测试修改
			/*Employee employee = new Employee(1, "Tom", "jerry@atguigu.com", "0");
			employeeMapper.updateEmp(employee);*/
			//System.out.println(updateEmp);
			//测试删除
			//employeeMapper.deleteEmpById(2);
			//手动提交数据
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}

	//演示MyBatis对传入参数的使用
	@Test
	public void testParam() throws IOException{

		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();

		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			//演示MyBatis对传入多个参数的使用
			Employee employee = mapper.getEmpByIdAndLastName(1, "Tom");
			//演示MyBatis对传入Map参数的使用
			/*Map<String, Object> map = new HashMap<>();
			map.put("id", 1);
			map.put("lastName", "Tom");
			map.put("tableName", "tbl_employee");
			Employee employee = mapper.getEmpByMap(map);*/

			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}

	//演示MyBatis查询结果为List集合
	@Test
	public void testList() throws IOException{

		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();

		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

			List<Employee> like = mapper.getEmpsByLastNameLike("%e%");
			for (Employee employee : like) {
				System.out.println(employee);
			}
		}finally{
			openSession.close();
		}
	}

	//演示MyBatis获取Map集合
	@Test
	public void testMap() throws IOException{

		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();

		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

			//返回一条记录的map；key就是列名，值就是对应的值.意思是Employee类型被转成了Map类型,属性对应key,属性的值对应value
			/*Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
			System.out.println(map.keySet());//{gender=0, last_name=Tom, id=1, email=jerry@atguigu.com}
			System.out.println(map.keySet());//[gender, last_name, id, email]*/

			//多条记录封装一个map：Map<Integer,Employee>:键是这条记录的主键，值是记录封装后的javaBean
			Map<String, Employee> map = mapper.getEmpByLastNameLikeReturnMap("%r%");
			System.out.println(map);
			/*{3=Employee [id=3, lastName=jerry2, email=123, gender=1], 4=Employee [id=4, lastName=jerry2, email=123, gender=1],
			 5=Employee [id=5, lastName=jerry2, email=123, gender=1]}*/
			System.out.println(map.keySet());//[3, 4, 5]

		}finally{
			openSession.close();
		}
	}
}
