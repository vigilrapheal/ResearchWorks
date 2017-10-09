package com.hirrr.tmsrv.cssopt.nodes;


/**
 * CSS @page rule implementation
 *
 * @author nitro
 */
public class DefaultPage extends DefaultDefinitions implements Page {
	/** Page name */
	private String name;

	/** Pseudo-page */
	private String pseudoPage;


	/**
	 * Constructor
	 *
	 * @param name page name (or null)
	 * @param pseudoPage pseudo-page (or null)
	 */
	public DefaultPage(String name, String pseudoPage) {
		super(null);
		this.name = name;
		this.pseudoPage = pseudoPage;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPseudoName() {
		return pseudoPage;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSource(boolean shrink) {
		StringBuilder buffer = new StringBuilder();
		int i = 0;

		buffer.append("@page ");
		if (name != null) {
			buffer.append(name);
			buffer.append(" ");
		}
		if (pseudoPage != null) {
			buffer.append(":");
			buffer.append(pseudoPage);
			buffer.append(" ");
		}
		buffer.append(super.getSource(shrink));

		return buffer.toString();
	}
}
