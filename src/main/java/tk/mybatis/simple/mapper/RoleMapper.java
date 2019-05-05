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
