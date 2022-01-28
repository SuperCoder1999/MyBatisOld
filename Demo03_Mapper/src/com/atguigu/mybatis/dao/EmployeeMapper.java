package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {
	
	public Employee getEmpById(Integer id);

	/**
	 * 1、mybatis允许增删改直接定义以下类型返回值
	 * 		Integer/Long/Boolean(影响一行及以上就返回true)、void (这些类型对应的基本类型也可以)
	 */
	public Long addEmp(Employee employee);

	public void updateEmp(Employee employee);

	public void deleteEmpById(Integer id);

	//演示MyBatis对传入参数的使用
	public Employee getEmpByMap(Map<String, Object> map);

	public Employee getEmpByIdAndLastName(@Param("id")Integer id,@Param("lastName")String lastName);

	//返回List集合时,resultType设置的是集合元素类型
	public List<Employee> getEmpsByLastNameLike(String lastName);

	//多条记录封装一个map：Map<Integer,Employee>:键是这条记录的主键，值是记录封装后的javaBean
	//@MapKey:告诉mybatis封装这个map的时候使用哪个属性作为map的key
	@MapKey("id")
	public Map<String, Employee> getEmpByLastNameLikeReturnMap(String lastName);

	//返回一条记录的map；key就是列名，值就是对应的值.意思是Employee类型被转成了Map类型,属性对应key,属性的值对应value
	public Map<String, Object> getEmpByIdReturnMap(Integer id);

}
