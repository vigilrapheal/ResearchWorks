package com.hirrr.tmsrv.cssopt;

import java.io.*;
import java.util.*;

import org.w3c.css.sac.*;

import com.hirrr.tmsrv.cssopt.nodes.DefaultDefinition;
import com.hirrr.tmsrv.cssopt.nodes.DefaultDefinitions;
import com.hirrr.tmsrv.cssopt.nodes.DefaultGroup;
import com.hirrr.tmsrv.cssopt.nodes.DefaultImport;
import com.hirrr.tmsrv.cssopt.nodes.DefaultMedia;
import com.hirrr.tmsrv.cssopt.nodes.DefaultPage;
import com.hirrr.tmsrv.cssopt.nodes.Definitions;
import com.hirrr.tmsrv.cssopt.nodes.Group;
import com.hirrr.tmsrv.cssopt.nodes.Import;
import com.hirrr.tmsrv.cssopt.nodes.Media;
import com.hirrr.tmsrv.cssopt.nodes.Page;


/**
 * CSS parser
 *
 * @author nitro
 */
public class CSS implements DocumentHandler, ErrorHandler {
	/**
	 * Parse a style-sheet
	 *
	 * @param source css source
	 * @return parsed css
	 * @throws Exception if any error occurs
	 */
	public static CSS parse(String source) throws Exception {
		return parse(new StringReader(source));
	}

	/**
	 * Parse a style-sheet
	 *
	 * @param is css source input stream
	 * @return parsed css
	 * @throws Exception if any error occurs
	 */
	public static CSS parse(InputStream is) throws Exception {
		return parse(new InputStreamReader(is));
	}

	/**
	 * Parse a style-sheet
	 *
	 * @param r css source reader
	 * @return parsed css
	 * @throws Exception if any error occurs
	 */
	public static CSS parse(Reader r) throws Exception {
		return new CSS(r);
	}


	/** CSS parser */
	private Parser parser = new org.w3c.flute.parser.Parser();

	/** Parsed @import rules */
	private List<Import> imports = new LinkedList<Import>();

	/** Parsed @media rules */
	private List<Media> medias = new LinkedList<Media>();

	/** Parsed @page rules */
	private List<Page> pages = new LinkedList<Page>();

	/** Main group */
	private Group root = new DefaultGroup();

	/** Current media */
	private Media currentMedia = null;

	/** Current page */
	private Page currentPage = null;

	/** Current group */
	private Group currentGroup = root;

	/** Current definitions block */
	private Definitions currentDefinitions = null;


	/**
	 * Constructor
	 *
	 * @param r css reader
	 * @throws Exception if any error occurs
	 */
	private CSS(Reader r) throws Exception {
		this.parser.setDocumentHandler(this);
		this.parser.setErrorHandler(this);
		this.parser.parseStyleSheet(new InputSource(r));
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getSource(false);
	}


	/**
	 * Convert to css source
	 *
	 * @param shrink shrink source flag
	 */
	public String getSource(boolean shrink) {
		StringBuilder buffer = new StringBuilder();

		for (Import _import : imports) {
			buffer.append(_import.getSource(shrink));
			if (!shrink) {
				buffer.append("\n");
			}
		}
		for (Media media : medias) {
			buffer.append(media.getSource(shrink));
			if (!shrink) {
				buffer.append("\n");
			}
		}
		for (Page page : pages) {
			buffer.append(page.getSource(shrink));
			if (!shrink) {
				buffer.append("\n");
			}
		}
		buffer.append(root.getSource(shrink));

		return buffer.toString();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startDocument(InputSource source) throws CSSException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endDocument(InputSource source) throws CSSException {
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI) throws CSSException {
		imports.add(
			new DefaultImport(uri, media, defaultNamespaceURI)
		);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startMedia(SACMediaList media) throws CSSException {
		if (currentMedia != null) {
			throw new IllegalStateException("already has an active media");
		}
		currentMedia = new DefaultMedia(media);
		currentGroup = currentMedia;

		medias.add(currentMedia);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endMedia(SACMediaList media) throws CSSException {
		currentGroup = root;
		currentMedia = null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startPage(String name, String pseudoPage) throws CSSException {
		if (currentPage != null) {
			throw new IllegalStateException("already has an active page");
		}
		if (currentDefinitions != null) {
			throw new IllegalStateException("already has an active definitions");
		}
		currentDefinitions = currentPage = new DefaultPage(name, pseudoPage);

		pages.add(currentPage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endPage(String name, String pseudoPage) throws CSSException {
		currentDefinitions = null;
		currentPage = null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startSelector(SelectorList selectors) throws CSSException {
		if (currentDefinitions != null) {
			throw new IllegalStateException("already has an active definitions");
		}
		currentDefinitions = new DefaultDefinitions(selectors);
		currentGroup.addDefinitions(currentDefinitions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endSelector(SelectorList selectors) throws CSSException {
		currentDefinitions = null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void property(String name, LexicalUnit value, boolean important) throws CSSException {
		if (currentDefinitions == null) {
			throw new IllegalStateException("already has an active definitions");
		}
		currentDefinitions.addProperty(
			new DefaultDefinition(name, value, important)
		);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ignorableAtRule(String atRule) throws CSSException {
//		System.out.println("ignorableAtRule(" + atRule + ")");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void comment(String text) throws CSSException {
//		System.out.println("comment(" + text + ")");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startFontFace() throws CSSException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endFontFace() throws CSSException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void namespaceDeclaration(String prefix, String uri) throws CSSException {
		throw new UnsupportedOperationException();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatalError(CSSParseException e) throws CSSException {
		throw new CSSException(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(CSSParseException e) throws CSSException {
		throw new CSSException(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(CSSParseException e) throws CSSException {
		throw new CSSException(e);
	}
}
