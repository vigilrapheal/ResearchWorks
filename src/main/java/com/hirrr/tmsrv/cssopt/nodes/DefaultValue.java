package com.hirrr.tmsrv.cssopt.nodes;

import org.w3c.css.sac.LexicalUnit;


/**
 * CSS value implementation
 *
 * @author nitro
 */
public class DefaultValue implements Value {
	/** Value */
	private String value;


	/**
	 * Constructor
	 *
	 * @param value integer value
	 */
	public DefaultValue(int value) {
		this.value = Integer.toString(value);
	}

	/**
	 * Constructor
	 *
	 * @param value floating-point value
	 */
	public DefaultValue(float value) {
		this(value, "");
	}

	/**
	 * Constructor
	 *
	 * @param value floating-point value
	 * @param unit unit (dimension)
	 */
	public DefaultValue(float value, String unit) {
		if (Math.floor(value) == value) {
			this.value = ((int)value) + unit;
		} else {
			this.value = value + unit;
		}
	}

	/**
	 * Constructor
	 *
	 * @param value string value
	 * @param identifier identifier flag
	 */
	public DefaultValue(String value, boolean identifier) {
		if (identifier) {
			this.value = value;
		} else {
			this.value = "\"" + quote(value) + "\"";
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return value;
	}


	/**
	 * Convert a parsed value
	 *
	 * @param value parsed value
	 * @return value descriptor
	 */
	static Value from(LexicalUnit value) {
		switch (value.getLexicalUnitType()) {
		case LexicalUnit.SAC_ATTR:
			return new DefaultValue(value.getStringValue(), true);

		case LexicalUnit.SAC_IDENT:
			return new DefaultValue(value.getStringValue(), true);

		case LexicalUnit.SAC_INHERIT:
			return new DefaultValue("inherit", true);

		case LexicalUnit.SAC_INTEGER:
			return new DefaultValue(value.getIntegerValue());

		case LexicalUnit.SAC_REAL:
			return new DefaultValue(value.getFloatValue());

		case LexicalUnit.SAC_STRING_VALUE:
			return new DefaultValue(value.getStringValue(), false);

		case LexicalUnit.SAC_URI:
			return new DefaultValue(value.getStringValue(), false);

		case LexicalUnit.SAC_COUNTER_FUNCTION:
		case LexicalUnit.SAC_COUNTERS_FUNCTION:
		case LexicalUnit.SAC_FUNCTION:
		case LexicalUnit.SAC_RECT_FUNCTION:
			return new DefaultFunctionValue(value.getFunctionName(), value.getParameters());

		case LexicalUnit.SAC_RGBCOLOR:
			return new DefaultFunctionValue("rgb", value.getParameters());

		case LexicalUnit.SAC_CENTIMETER:
		case LexicalUnit.SAC_DEGREE:
		case LexicalUnit.SAC_DIMENSION:
		case LexicalUnit.SAC_EM:
		case LexicalUnit.SAC_EX:
		case LexicalUnit.SAC_GRADIAN:
		case LexicalUnit.SAC_HERTZ:
		case LexicalUnit.SAC_INCH:
		case LexicalUnit.SAC_KILOHERTZ:
		case LexicalUnit.SAC_MILLIMETER:
		case LexicalUnit.SAC_MILLISECOND:
		case LexicalUnit.SAC_PERCENTAGE:
		case LexicalUnit.SAC_PICA:
		case LexicalUnit.SAC_PIXEL:
		case LexicalUnit.SAC_POINT:
		case LexicalUnit.SAC_RADIAN:
		case LexicalUnit.SAC_SECOND:
			return new DefaultValue(value.getFloatValue(), value.getDimensionUnitText());

		case LexicalUnit.SAC_OPERATOR_COMMA:
			return new DefaultOperatorValue(",");
		case LexicalUnit.SAC_OPERATOR_EXP:
			return new DefaultOperatorValue("^");
		case LexicalUnit.SAC_OPERATOR_GE:
			return new DefaultOperatorValue(">=");
		case LexicalUnit.SAC_OPERATOR_GT:
			return new DefaultOperatorValue(">");
		case LexicalUnit.SAC_OPERATOR_LE:
			return new DefaultOperatorValue("<=");
		case LexicalUnit.SAC_OPERATOR_LT:
			return new DefaultOperatorValue("<");
		case LexicalUnit.SAC_OPERATOR_MINUS:
			return new DefaultOperatorValue("-");
		case LexicalUnit.SAC_OPERATOR_MOD:
			return new DefaultOperatorValue("%");
		case LexicalUnit.SAC_OPERATOR_MULTIPLY:
			return new DefaultOperatorValue("*");
		case LexicalUnit.SAC_OPERATOR_PLUS:
			return new DefaultOperatorValue("+");
		case LexicalUnit.SAC_OPERATOR_SLASH:
			return new DefaultOperatorValue("/");
		case LexicalUnit.SAC_OPERATOR_TILDE:
			return new DefaultOperatorValue("~");
		}
		throw new IllegalStateException("unsupported lexical unit type : " + value.getLexicalUnitType());
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
