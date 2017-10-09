package com.hirrr.tmsrv.cssopt.nodes;


/**
 * CSS operator value implementation
 *
 * @author nitro
 */
public class DefaultOperatorValue extends DefaultValue implements OperatorValue {
	/**
	 * Constructor
	 *
	 * @param value operator value
	 */
	public DefaultOperatorValue(String value) {
		super(value, true);
	}
}
