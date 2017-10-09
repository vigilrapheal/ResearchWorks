package com.hirrr.tmsrv.cssopt.nodes;

import java.util.LinkedList;
import java.util.List;

import org.w3c.css.sac.LexicalUnit;


/**
 * CSS function value implementation
 *
 * @author nitro
 */
public class DefaultFunctionValue extends DefaultValue implements FunctionValue {
	/** Function parameters */
	private List<Value> parameters = new LinkedList<Value>();


	/**
	 * Constructor
	 *
	 * @param function function name
	 * @param parameter parsed parameter(s)
	 */
	public DefaultFunctionValue(String function, LexicalUnit parameter) {
		super(function, true);
		while (parameter != null) {
			this.parameters.add(from(parameter));

			parameter = parameter.getNextLexicalUnit();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(super.toString());
		buffer.append('(');
		for (Value parameter : parameters) {
			buffer.append(parameter);
		}
		buffer.append(')');
		return buffer.toString();
	}
}
