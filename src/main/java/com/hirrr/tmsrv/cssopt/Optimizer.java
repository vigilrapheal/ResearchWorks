package com.hirrr.tmsrv.cssopt;

import java.io.*;
import java.util.*;


public class Optimizer {
	public static String beautify(String source) throws Exception {
		CSS css = CSS.parse(source);

		return css.getSource(false);
	}

	public static String beautify(InputStream is) throws Exception {
		CSS css = CSS.parse(is);

		return css.getSource(false);
	}

	public static String beautify(Reader r) throws Exception {
		CSS css = CSS.parse(r);

		return css.getSource(false);
	}


	public static String shrink(String source) throws Exception {
		CSS css = CSS.parse(source);

		return css.getSource(true);
	}

	public static String shrink(InputStream is) throws Exception {
		CSS css = CSS.parse(is);

		return css.getSource(true);
	}

	public static String shrink(Reader r) throws Exception {
		CSS css = CSS.parse(r);

		return css.getSource(true);
	}


	public static void main(String [] args) throws Exception {
		// parse css document
		
		File file=new File("/home/vigil/Desktop/output/hidden.txt");
		CSS css = CSS.parse(new FileReader(file));

		// TODO
		System.out.println(css.getSource(true));
	}
}
