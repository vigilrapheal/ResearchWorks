package com.hirrr.tmsrv.cssopt.nodes;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.css.sac.SACMediaList;


/**
 * CSS @media rule implementation
 *
 * @author nitro
 */
public class DefaultMedia extends DefaultGroup implements Media {
	/** Target media list */
	private Set<String> names = new TreeSet<String>();


	/**
	 * Constructor
	 *
	 * @param media target media list
	 */
	public DefaultMedia(SACMediaList media) {
		super();
		for (int i = 0; i < media.getLength(); i++) {
			this.names.add(media.item(i));
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getNames() {
		return Collections.unmodifiableSet(names);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSource(boolean shrink) {
		StringBuilder buffer = new StringBuilder();
		int i = 0;

		buffer.append("@media ");
		for (String name : names) {
			if (i > 0) {
				buffer.append(',');
				if (!shrink) {
					buffer.append(' ');
				}
			}
			buffer.append(name);
			i++;
		}

		buffer.append('{');
		if (!shrink) {
			buffer.append('\n');
		}
		buffer.append(super.getSource(shrink));
		buffer.append('}');

		return buffer.toString();
	}
}
