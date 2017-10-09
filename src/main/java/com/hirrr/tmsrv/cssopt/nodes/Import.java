package com.hirrr.tmsrv.cssopt.nodes;

import java.util.Set;


/**
 * CSS @import rule interface
 *
 * @author nitro
 */
public interface Import {
	/**
	 * Get import uri
	 *
	 * @return import uri
	 */
	public String getURI();

	/**
	 * Get default namespace uri
	 *
	 * @return default namespace uri
	 */
	public String getDefaultNamespaceURI();

	/**
	 * Get media names (if any)
	 *
	 * @return media names
	 */
	public Set<String> getMediaNames();


	/**
	 * Convert to css source
	 *
	 * @param shrink shrink source flag
	 */
	public String getSource(boolean shrink);
}
