package com.hirrr.tmsrv.cssopt.nodes;

import java.util.Set;


/**
 * CSS @media rule interface
 *
 * @author nitro
 */
public interface Media extends Group {
	/**
	 * Get media names
	 *
	 * @return media names
	 */
	public Set<String> getNames();
}
