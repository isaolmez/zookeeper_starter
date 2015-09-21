package com.isa.curator.operations;

import java.io.Serializable;

/**
 * 
 * @author seref
 *
 */
public class FieldCountElement  implements Serializable{

	private long datetime;
	private int minFieldCount;
	private int maxFieldCount;
	
	public long getDatetime() {
		return datetime;
	}
	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}
	public int getMinFieldCount() {
		return minFieldCount;
	}
	public void setMinFieldCount(int minFieldCount) {
		this.minFieldCount = minFieldCount;
	}
	public int getMaxFieldCount() {
		return maxFieldCount;
	}
	public void setMaxFieldCount(int maxFieldCount) {
		this.maxFieldCount = maxFieldCount;
	}
	
	@Override
	public String toString() {
		return "FieldCountElement [datetime=" + datetime + ", minFieldCount=" + minFieldCount + ", maxFieldCount=" + maxFieldCount + "]";
	}
	
}
