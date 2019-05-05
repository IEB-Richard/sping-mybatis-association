# Spring and MyBatis - One-To-One Demo 02

This is the second demo project that demostrate how to combine two tables with ResultMap for one-to-one scenario. 

In this demo project, we use association tag in xml file not only for defining nested resultMap, but also for defining nested search.

Key technical points:

- association - fetchType
- global setttings - aggressiveLazyLoading



***Please Note:***

***This document only contains the key steps of the development, please check on the code for more details.***



### Global Settings - mybatis-config.xml

Please note that previous than mybatis version 3.41, the global setting `aggressiveLazyLoading` was configured as true by default, and since version 3.41, it's configured as false by default. 

The settings of this project.

```xml
	<settings>
		<setting name="logImpl" value="LOG4J" />
		<setting name="mapUnderscoreToCamelCase" value="false" />
		<setting name="aggressiveLazyLoading" value="true"/>
	</settings>
```

Please check on the [official document]([http://www.mybatis.org/mybatis-3/configuration.html) for more details.

> **Note:** 
>
> We set it back to `true` for testing lazy fetch purpose.



###Update the RoleMapper.xml file

Define a normal resultMap and named it as `roleMap`. also defined one `select` with `id="roleMap"`.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
					"http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="tk.mybatis.simple.mapper.RoleMapper">
	<resultMap id="roleMap"
		type="tk.mybatis.simple.model.SysRole">
		<id property="id" column="id" />
		<result property="roleName" column="role_name" />
		<result property="enabled" column="enabled" />
		<result property="createBy" column="create_by" />
		<result property="createTime" column="create_time"
			jdbcType="TIMESTAMP" />
	</resultMap>
	
	<select id="selectRoleById" resultMap="roleMap">
		select * from sys_role where id = #{id}
	</select>
</mapper>
```



###Define the RoleMapper interface

```java
package tk.mybatis.simple.mapper;

import tk.mybatis.simple.model.SysRole;

public interface RoleMapper {
	/**
	 * Select Role by Id
	 * @param id
	 * @return
	 */
	SysRole selectRoleById(Long id);
}

```

The above settings are only for one table configurations. Next we need to call these settings from another entity UserMapper.



###Updte the UserMapper.xml 

Please check on how the resultMap `userRoleMapSelect` was defined.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
					"http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="tk.mybatis.simple.mapper.UserMapper">

	<resultMap id="userMap" type="SysUser">
		<id property="id" column="id" />
		<result property="userName" column="user_name" />
		<result property="userPassword" column="user_password" />
		<result property="userEmail" column="user_email" />
		<result property="userInfo" column="user_info" />
		<result property="headImg" column="head_img" jdbcType="BLOB" />
		<result property="createTime" column="create_time"
			jdbcType="TIMESTAMP" />
	</resultMap>
	
	<!-- Please try to remove property fectchType and check the program log -->
	<resultMap id="userRoleMapSelect" extends="userMap" type="SysUser">
		<association 
			property="role" 
			column="{id=role_id}"
			fetchType="lazy"
			select="tk.mybatis.simple.mapper.RoleMapper.selectRoleById">
		</association>
	</resultMap>
	
	
	<select id="selectUserAndRoleByIdSelect" resultMap="userRoleMapSelect">
		select 
			u.id,
			u.user_name,
			u.user_email,
			u.user_info,
			u.head_img,
			u.create_time,
			ur.role_id
		from sys_user u
		inner join  sys_user_role ur on u.id = ur.user_id
		where u.id = #{id}
	</select>

</mapper>
```

Because we need to connect RoleMap with `column="{id=role_id}"`, so here we only need to inner join table `sys_user_role` and get the user's `role_id` out rom the table.

###Update UserMapper.java classes

update the UserMapper.java interface file as following.

```java
package tk.mybatis.simple.mapper;

import tk.mybatis.simple.model.SysUser;

public interface UserMapper {
	/**
	 * Select User and its role by user id
	 * only demo MyBatis one-to-one association solution
	 * @param id
	 * @return
	 */
	SysUser selectUserAndRoleByIdSelect(Long id);
	
}
```



###Then write the test cases

```java
package tk.mybatis.simple.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import tk.mybatis.simple.model.SysUser;

public class UserMapperTest extends BaseMapperTest {

	@Test
	public void testSelectUserAndRoleByIdSelect() {
		// get sql session
		SqlSession sqlSession = getSqlSession();
		try {
			// get UserMapper object do search action
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = userMapper.selectUserAndRoleByIdSelect(1001l);
			
			// Check results
			Assert.assertNotNull(user);
			System.out.println(">>>>>before call method user.getRole()");
		  Assert.assertNotNull(user.getRole());
		  System.out.println(">>>>>after call method user.getRole()");

		} finally {
			// Please don't forget to close the session after reach test
			sqlSession.close();
		}
	}

}
```



Change the global `aggressiveLazyLoading` setting value between `true` and `false`, update the `fetchType`of resultMap `userRoleMapSelect`. check on how the program log populate.



### About lazyLoadTriggerMethods

This is a global settings which specifies which object's methods trigger a lazy load. It's default value, `equals,clone,hashCode,toString`,  is a method name list seperated by commas.

Update the test method and check on the program log again.

```java
package tk.mybatis.simple.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import tk.mybatis.simple.model.SysUser;

public class UserMapperTest extends BaseMapperTest {

	@Test
	public void testSelectUserAndRoleByIdSelect() {
		// get sql session
		SqlSession sqlSession = getSqlSession();
		try {
			// get UserMapper object do search action
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			SysUser user = userMapper.selectUserAndRoleByIdSelect(1001l);
			
			// Check results
			Assert.assertNotNull(user);
			System.out.println(">>>>>call user.equals(null)");
			user.equals(null);
			System.out.println(">>>>>before call method user.getRole()");
		    Assert.assertNotNull(user.getRole());
		    System.out.println(">>>>>after call method user.getRole()");

		} finally {
			// Please don't forget to close the session after reach test
			sqlSession.close();
		}
	}

}
```

Please note:

Here we add two additional line:

```java
		System.out.println(">>>>>call user.equals(null)");
		user.equals(null);
```
