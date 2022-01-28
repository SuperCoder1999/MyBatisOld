package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.DepartmentMapper;
import com.atguigu.mybatis.dao.EmployeeMapperPlus;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class EmployeeMapperPlusTest {
    //根据全局配置文件 获取 SqlSessionFactory
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }
    //级联查询 关联 单一对象
    @Test
    public void testAssociation() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
            //测试自定义resultMap(结果集映射)
			/*Employee empById = mapper.getEmpById(1);
			System.out.println(empById);*/

            //测试两种不太常用的级联查询方法
            /*Employee empAndDept = mapper.getEmpAndDept(1);
			System.out.println(empAndDept);
			System.out.println(empAndDept.getDept());*/

            //测试分步 级联查询
            Employee employee = mapper.getEmpByIdStep(3);
            System.out.println(employee.getLastName());//调用toString会触发第二步查询??(有点不清楚,因为toString中没有用到dept)
            //System.out.println(employee.getDept());
            // 不开启延迟加载（懒加载/按需加载),即使不用到dept属性中的内容也会执行该步骤查询
            System.out.println(employee.getDept());//开启后,只有程序中用到才会进行查询
        }finally{
            openSession.close();
        }
    }

    //级联查询 关联 多个对象
    @Test
    public void testCollection() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try{
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);

            //collection 的嵌套结果集
			/*Department department = mapper.getDeptByIdPlus(1);
			System.out.println(department);
			System.out.println(department.getEmps());*/

            //collection的 分布查询
            Department deptByIdStep = mapper.getDeptByIdStep(1);
            System.out.println(deptByIdStep.getDepartmentName());
            System.out.println(deptByIdStep.getEmps());
        }finally{
            openSession.close();
        }
    }

    //测试 鉴别器
    @Test
    public void testDiscriminator() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);

            //测试查询女性
           /* Employee empByDis0 = mapper.getEmpByDis(1);
            System.out.println(empByDis0.getLastName());
            System.out.println(empByDis0.getDept());*/

            //测试查询男性
            Employee empByDis1 = mapper.getEmpByDis(3);
            System.out.println(empByDis1);
            System.out.println(empByDis1.getDept());
        }finally{
            openSession.close();
        }
    }
}
