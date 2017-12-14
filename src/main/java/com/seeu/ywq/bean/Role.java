package com.seeu.ywq.bean;

/**
 * 角色表
 * @author Scary
 *
 */
public class Role {

	private Long id;//主键
	
	private String roleNo;//角色号
	
	private String roleName;//角色名
	
	private String roleDescr;//角色说明

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescr() {
		return roleDescr;
	}

	public void setRoleDescr(String roleDescr) {
		this.roleDescr = roleDescr;
	}
	
	
}
