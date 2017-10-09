package com.hirrr.tmsrv.cssopt.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SiblingSelector;


/**
 * CSS property definitions block implementation
 *
 * @author nitro
 */
public class DefaultDefinitions implements Definitions {
	/** Selectors */
	private Set<String> selectors = new TreeSet<String>();

	/** Properties */
	private Map<String, Definition> properties = new TreeMap<String, Definition>();


	/**
	 * Constructor
	 *
	 * @param selectors parsed selectors
	 */
	public DefaultDefinitions(SelectorList selectors) {
		if (selectors != null) {
			for (int i = 0; i < selectors.getLength(); i++) {
				Selector selector = selectors.item(i);

				this.selectors.add(from(selector));
			}
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
	public Set<String> getSelectors() {
		return Collections.unmodifiableSet(selectors);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Definition> getProperties() {
		return Collections.unmodifiableList(
			new ArrayList<Definition>(properties.values())
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addProperty(Definition definition) {
		properties.put(definition.getProperty(), definition);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSource(boolean shrink) {
		StringBuilder buffer = new StringBuilder();

		for (String selector : selectors) {
			if (buffer.length() > 0) {
				buffer.append(',');
				if (!shrink) {
					buffer.append(' ');
				}
			}
			buffer.append(selector);
		}
		if (shrink) {
			buffer.append('{');
		} else {
			buffer.append(" {\n");
		}
		for (Definition definition : properties.values()) {
			if (!shrink) {
				buffer.append(' ');
			}
			buffer.append(definition);
			buffer.append(';');
			if (!shrink) {
				buffer.append('\n');
			}
		}
		buffer.append('}');
		return buffer.toString();
	}


	/**
	 * Convert a parsed selector
	 *
	 * @param selector parsed selector
	 * @return selector text
	 */
	private static String from(Selector selector) {
//		if (selector instanceof CharacterDataSelector) {
//		}
		if (selector instanceof ConditionalSelector) {
			ConditionalSelector cs = (ConditionalSelector)selector;

			switch (selector.getSelectorType()) {
			case Selector.SAC_CONDITIONAL_SELECTOR:
				if (from(cs.getSimpleSelector()).equals("*")) {
					return (from(cs.getCondition()));
				}
				return (from(cs.getSimpleSelector()) + from(cs.getCondition()));
			}
			throw new IllegalStateException("unsupported conditional selector type : " + selector.getSelectorType());
		}
		if (selector instanceof DescendantSelector) {
			DescendantSelector ds = (DescendantSelector)selector;

			switch (selector.getSelectorType()) {
			case Selector.SAC_DESCENDANT_SELECTOR:
				return (from(ds.getAncestorSelector()) + " " + from(ds.getSimpleSelector()));

			case Selector.SAC_CHILD_SELECTOR:
				if (from(ds.getSimpleSelector()).startsWith(":")) {
					return (from(ds.getAncestorSelector()) + from(ds.getSimpleSelector()));
				}
				return (from(ds.getAncestorSelector()) + ">" + from(ds.getSimpleSelector()));
			}
			throw new IllegalStateException("unsupported descendant selector type : " + selector.getSelectorType());
		}
		if (selector instanceof ElementSelector) {
			ElementSelector es = (ElementSelector)selector;

			switch (selector.getSelectorType()) {
			case Selector.SAC_ELEMENT_NODE_SELECTOR:
				if (es.getLocalName() != null) {
					return es.getLocalName();
				}
				return "*";

			case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
				return (":" + es.getLocalName());
			}
			throw new IllegalStateException("unsupported element selector type : " + selector.getSelectorType());
		}
//		if (selector instanceof NegativeSelector) {
//		}
//		if (selector instanceof ProcessingInstructionSelector) {
//		}
		if (selector instanceof SiblingSelector) {
			SiblingSelector ss = (SiblingSelector)selector;

			switch (selector.getSelectorType()) {
			case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
				return (from(ss.getSelector()) + "+" + from(ss.getSiblingSelector()));
			}
			throw new IllegalStateException("unsupported sibling selector type : " + selector.getSelectorType());
		}
		throw new IllegalStateException("unsupported selector : " + selector.getClass());
	}

	/**
	 * Convert a parsed condition
	 *
	 * @param condition parsed condition
	 * @return condition text
	 */
	private static String from(Condition condition) {
		if (condition instanceof AttributeCondition) {
			AttributeCondition ac = (AttributeCondition)condition;

			switch (condition.getConditionType()) {
			case Condition.SAC_ATTRIBUTE_CONDITION:
				if (ac.getValue() != null) {
					return ("[" + ac.getLocalName() + "=\"" + quote(ac.getValue()) + "\"]");
				}
				return ("[" + ac.getLocalName() + "]");

			case Condition.SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
				return ("[" + ac.getLocalName() + "|=\"" + quote(ac.getValue()) + "\"]");

			case Condition.SAC_CLASS_CONDITION:
				return ("." + ac.getValue());

			case Condition.SAC_ID_CONDITION:
				return ("#" + ac.getValue());

			case Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION:
				return ("[" + ac.getLocalName() + "~=\"" + quote(ac.getValue()) + "\"]");

			case Condition.SAC_PSEUDO_CLASS_CONDITION:
				return (":" + ac.getValue());
			}
			throw new IllegalStateException("unsupported attribute condition type : " + condition.getConditionType());
		}
		if (condition instanceof CombinatorCondition) {
			CombinatorCondition cc = (CombinatorCondition)condition;

			switch (condition.getConditionType()) {
			case Condition.SAC_AND_CONDITION:
				return (from(cc.getFirstCondition()) + from(cc.getSecondCondition()));
			}
			throw new IllegalStateException("unsupported combinator condition type : " + condition.getConditionType());
		}
//		if (condition instanceof ContentCondition) {
//		}
		if (condition instanceof LangCondition) {
			LangCondition lc = (LangCondition)condition;

			switch (condition.getConditionType()) {
			case Condition.SAC_LANG_CONDITION:
				return (":lang(" + lc.getLang() + ")");
			}
			throw new IllegalStateException("unsupported lang condition type : " + condition.getConditionType());
		}
//		if (condition instanceof NegativeCondition) {
//		}
//		if (condition instanceof PositionalCondition) {
//		}
		throw new IllegalStateException("unsupported condition : " + condition.getClass());
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
