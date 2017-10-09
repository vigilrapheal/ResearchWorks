package com.hirrr.tmsrv.cssopt.nodes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * CSS property definitions blocks group implementation
 *
 * @author nitro
 */
public class DefaultGroup implements Group {
	/** Definitions list */
	private List<Definitions> list = new LinkedList<Definitions>();


	/**
	 * Constructor
	 *
	 */
	public DefaultGroup() {
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getSource(false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Definitions> getDefinitions() {
		return Collections.unmodifiableList(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDefinitions(Definitions definitions) {
		list.add(definitions);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSource(boolean shrink) {
		StringBuilder buffer = new StringBuilder();

		for (Definitions definitions : list) {
			buffer.append(definitions.getSource(shrink));
			if (!shrink) {
				buffer.append('\n');
			}
		}
		return buffer.toString();
	}
}
