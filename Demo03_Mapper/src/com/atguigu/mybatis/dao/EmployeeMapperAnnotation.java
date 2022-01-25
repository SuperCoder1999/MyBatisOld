package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Select;

public interface EmployeeMapperAnnotation {
    //注解的方式实现 sql映射  (缺点是只能简单的sql语句,且没有databaseId等属性)
    @Select(value="select * from tbl_employee where id=#{id}")
    public Employee getEmpById(Integer id);

}
