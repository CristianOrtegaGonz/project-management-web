package se.grouprich.projectmanagement.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.RandomStringUtils;

@MappedSuperclass
public abstract class AbstractEntityData
{
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String controlNumber;

	protected AbstractEntityData()
	{
		controlNumber = RandomStringUtils.randomAlphanumeric(8);
	}

	public Long getId()
	{
		return id;
	}
	
	public String getControlNumber()
	{
		return controlNumber;
	}
}
