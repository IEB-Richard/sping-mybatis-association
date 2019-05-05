package tk.mybatis.simple.model;

import java.io.Serializable;
import java.util.Date;

public class CreateInfo implements Serializable {

	private static final long serialVersionUID = -4154753790883924904L;

	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	// Constructors
	public CreateInfo() {
	}
	
	public CreateInfo(String createBy, Date createTime) {
		this.createBy = createBy;
		this.createTime = createTime;
	}

	// Getters and Setters
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "CreateInfo [createBy=" + createBy + ", createTime=" + createTime + "]";
	}
}
