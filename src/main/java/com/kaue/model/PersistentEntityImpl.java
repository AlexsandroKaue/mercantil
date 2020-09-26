package com.kaue.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistentEntityImpl implements PersistentEntity<Integer>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		else if (obj == null)
		{
			return false;
		}
		else if (this.getClass() != obj.getClass())
		{
			return false;
		}
		else 
		{
			PersistentEntity other = (PersistentEntity) obj;
			
			if (this.getId() != null) 
			{
				if (!getId().equals(other.getId()))
					return false;
				else
					return true;
			}
		}

		return false;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		
		if (getId() instanceof Number) 
		{
			Number n = (Number) getId();

			result = prime * result + n.intValue();
		} 
		else 
		{
			result = super.hashCode();
		}

		return result;
	}
}