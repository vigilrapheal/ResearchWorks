package com.hirrr.tmsrv.cssopt.nodes;

import java.util.List;


/**
 * CSS property definition interface
 *
 * @author nitro
 */
public interface Definition {
	/**
	 * Get property name
	 *
	 * @return property name
	 */
	public String getProperty();

	/**
	 * Get property values
	 *
	 * @return property values
	 */
	public List<Value> getValues();

	/**
	 * Check if property definition is marked as "!important"
	 *
	 * @return true if important, false otherwise
	 */
	public boolean isImportant();
}
