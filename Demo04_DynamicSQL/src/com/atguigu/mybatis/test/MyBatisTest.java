package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapperDynamicSQL;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MyBatisTest {

    //测试DynamicSQL if  并借用where保证拼接正确性
    @Test
    public void testDynamicSqlIf_Where() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //根据传入的参数,和if判断,会拼接成: select * from tbl_employee where id=? and last_name like ?
            //测试if,并借助where保证拼接正确性
            Employee employee = new Employee(1, "Tom", null, null);
		    List<Employee> emps = mapper.getEmpsByConditionIf(employee );
			for (Employee emp : emps) {
				System.out.println(emp);
			}

            //测试Trim
			/*List<Employee> emps2 = mapper.getEmpsByConditionTrim(employee);
			for (Employee emp : emps2) {
				System.out.println(emp);
			}*/


            //测试choose
			/*List<Employee> list = mapper.getEmpsByConditionChoose(employee);
			for (Employee emp : list) {
				System.out.println(emp);
			}*/

            //测试set标签
			/*mapper.updateEmp(employee);
			openSession.commit();*/

            /*List<Employee> list = mapper.getEmpsByConditionForeach(Arrays.asList(1,2));
            for (Employee emp : list) {
                System.out.println(emp);
            }*/

        }finally{
            openSession.close();
        }
    }

    //测试DynamicSQL if  并借用trim保证拼接正确性
    @Test
    public void testDynamicSqlIf_Trim() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //根据传入的参数,和if判断,会拼接成: select * from tbl_employee where id=? and last_name like ?
            //测试if,并借助where保证拼接正确性
            Employee employee = new Employee(1, "Tom", null, null);

            //测试Trim
			List<Employee> emps2 = mapper.getEmpsByConditionTrim(employee);
			for (Employee emp : emps2) {
				System.out.println(emp);
			}

        }finally{
            openSession.close();
        }
    }

    //测试DynamicSQL choose  并借用where保证拼接正确性
    @Test
    public void testDynamicSqlChoose_Where() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //根据传入的参数,和choose判断,会拼接成: select * from tbl_employee WHERE id=?
            // 或者 select * from tbl_employee WHERE last_name like ?  (只选择一个条件)
            Employee employee = new Employee(null, "Tom", null, null);
            //试试如果压根没有传入所需要的参数.会不会判定为 null  -- 答案:可以
            Employee employee2 = new Employee("Tom", null, null);
            //测试Trim
            List<Employee> emps2 = mapper.getEmpsByConditionChoose(employee2);
            for (Employee emp : emps2) {
                System.out.println(emp);
            }

        }finally{
            openSession.close();
        }
    }

    //测试DynamicSQL set 实现动态更改 set可以自动去掉末尾的 逗号
    @Test
    public void testDynamicSqlSet() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //拼接后: update tbl_employee SET last_name=? where id=?
            Employee employee1 = new Employee(3, "Tom", null, null);
            //拼接后: update tbl_employee SET last_name=?, email=? where id=?
            Employee employee = new Employee(3, "Tom", "12334", null);

            //测试set
            mapper.updateEmpSet(employee);
            openSession.commit();
        }finally{
            openSession.close();
        }
    }

    //测试DynamicSQL Trim 实现动态更改,代替 set(trim也可以去掉末尾的 逗号)
    @Test
    public void testDynamicSqlTrim() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //拼接后: update tbl_employee SET last_name=? where id=?
            Employee employee1 = new Employee(3, "Tom", null, null);
            //拼接后: update tbl_employee SET last_name=?, email=? where id=?
            Employee employee = new Employee(3, "Tom", "123123", null);

            //测试set
            mapper.updateEmpTrim(employee1);
            openSession.commit();
        }finally{
            openSession.close();
        }
    }


    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }
}
