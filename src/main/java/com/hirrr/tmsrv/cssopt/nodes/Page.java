package com.hirrr.tmsrv.cssopt.nodes;


/**
 * CSS @page rule interface
 *
 * @author nitro
 */
public interface Page extends Definitions {
	/**
	 * Get page name. (or null if none)
	 *
	 * @return page name
	 */
	public String getName();

	/**
	 * Get pseudo-page. (or null if none)
	 *
	 * @return pseudo-page
	 */
	public String getPseudoName();
}
