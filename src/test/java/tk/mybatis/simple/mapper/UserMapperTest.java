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
