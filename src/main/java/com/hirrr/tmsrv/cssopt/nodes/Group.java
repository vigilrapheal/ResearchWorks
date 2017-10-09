package com.hirrr.tmsrv.cssopt.nodes;

import java.util.List;


/**
 * CSS property definitions blocks group interface
 *
 * @author nitro
 */
public interface Group {
	/**
	 * Get property definition blocks
	 *
	 * @return property definition blocks
	 */
	public List<Definitions> getDefinitions();

	/**
	 * Add a property definition block
	 *
	 * @param definitions property definition block
	 */
	public void addDefinitions(Definitions definitions);


	/**
	 * Convert to css source
	 *
	 * @param shrink shrink source flag
	 */
	public String getSource(boolean shrink);
}
