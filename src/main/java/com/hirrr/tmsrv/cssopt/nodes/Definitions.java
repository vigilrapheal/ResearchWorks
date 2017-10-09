package com.hirrr.tmsrv.cssopt.nodes;

import java.util.List;
import java.util.Set;


/**
 * CSS property definitions block interface
 *
 * @author nitro
 */
public interface Definitions {
	/**
	 * Get selectors
	 *
	 * @return selectors
	 */
	public Set<String> getSelectors();

	/**
	 * Get property definitions
	 *
	 * @return property definitions
	 */
	public List<Definition> getProperties();

	/**
	 * Add a property definition
	 *
	 * @param definition property definition
	 */
	public void addProperty(Definition definition);


	/**
	 * Convert to css source
	 *
	 * @param shrink shrink source flag
	 */
	public String getSource(boolean shrink);
}
