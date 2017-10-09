package com.hirrr.tmsrv.cssopt.nodes;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.css.sac.SACMediaList;


/**
 * CSS @import rule implementation
 *
 * @author nitro
 */
public class DefaultImport implements Import {
	/** Import uri */
	private String uri;

	/** Target media list */
	private Set<String> mediaNames = new TreeSet<String>();

	/** ? */
	private String defaultNamespaceURI;


	/**
	 * Constructor
	 *
	 * @param uri import uri
	 * @param media target media list
	 * @param defaultNamespaceURI ?
	 */
	public DefaultImport(String uri, SACMediaList media, String defaultNamespaceURI) {
		this.uri = uri;
		this.defaultNamespaceURI = defaultNamespaceURI;
		for (int i = 0; i < media.getLength(); i++) {
			this.mediaNames.add(media.item(i));
		}
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
	public String getURI() {
		return uri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultNamespaceURI() {
		return defaultNamespaceURI;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getMediaNames() {
		return Collections.unmodifiableSet(mediaNames);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSource(boolean shrink) {
		StringBuilder buffer = new StringBuilder();
		int i = 0;

		buffer.append("@import \"");
		buffer.append(quote(uri));
		buffer.append('"');
		for (String mediaName : mediaNames) {
			if (i > 0) {
				buffer.append(',');
				if (!shrink) {
					buffer.append(' ');
				}
			} else {
				buffer.append(' ');
			}
			buffer.append(mediaName);
			i++;
		}
		buffer.append(';');
		return buffer.toString();
	}


	/**
	 * Quote given text
	 *
	 * @param text text to quote
	 * @return quoted text
	 */
	private static String quote(String text) {
		return text.replaceAll("\"", "\\\"");
	}
}
