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
