package com.hirrr.tmsrv.cssopt.nodes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.w3c.css.sac.LexicalUnit;


/**
 * CSS property definition implementation
 *
 * @author nitro
 */
public class DefaultDefinition implements Definition {
	/** Property name */
	private String property;

	/** List of values */
	private List<Value> values = new LinkedList<Value>();

	/** Important flag */
	private boolean important;


	/**
	 * Constructor
	 *
	 * @param property property name
	 * @param value parsed value(s)
	 * @param important important flag
	 */
	public DefaultDefinition(String property, LexicalUnit value, boolean important) {
		this.property = property;
		this.important = important;
		while (value != null) {
			this.values.add(DefaultValue.from(value));

			value = value.getNextLexicalUnit();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(property);
		buffer.append(':');
		for (Value value : values) {
			buffer.append(' ');
			buffer.append(value);
		}
		if (important) {
			buffer.append(" !important");
		}
		return buffer.toString();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty() {
		return property;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Value> getValues() {
		return Collections.unmodifiableList(values);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isImportant() {
		return important;
	}
}
