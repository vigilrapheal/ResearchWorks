package com.hirrr.crawltest.seleniumtestmaven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssFormater {

	public static String cssBeautifier(String value) {

		CssFormater css = new CssFormater();

		String cleanHtml = css.htmlRemover(value);
		cleanHtml = css.cssBeautify(cleanHtml);

		return cleanHtml;
	}

	private String htmlRemover(String value) {

		String returnVal = value;
		String regex = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		m = p.matcher(returnVal);
		int i = 0;
		while (m.find()) {
			i++;
			if (i > 70) {
				return "";
			}
			String remove = m.group();
			returnVal = contentRemover(returnVal, remove);
		}
		return returnVal.trim();
	}

	public String contentRemover(String returnVal, String remove) {
		StringBuilder strBuild = new StringBuilder();
		String[] returnArr = returnVal.split(remove);
		for (int i = 0; i < returnArr.length; i++) {
			strBuild.append(returnArr[i]);
		}

		return strBuild.toString();
	}

	private String cssBeautify(String value) {

		String css = value;
		for (int i = 0; i < css.length(); i++) {
			// System.out.println(value.charAt(i));
			if (css.charAt(i) == '{') {
				css = css.substring(0, i + 1) + "\n\t\t" + css.substring(i + 1, css.length());
			} else if (css.charAt(i) == '}') {
				css = css.substring(0, i + 1) + "\n\n" + css.substring(i + 1,
						css.length())/*
										 * + css.substring(i, i + 1) + "\n\n" + css.substring(i + 1, css.length())
										 */;
			} else if (css.charAt(i) == ';') {
				css = css.substring(0, i + 1) + "\n\t\t" + css.substring(i + 1, css.length());
			} else if (css.charAt(i) == '/' && i != 0) {
				if (css.charAt(i - 1) == '*') {
					css = css.substring(0, i + 1) + "\n\n" + css.substring(i + 1, css.length());
				}
			} else if (css.charAt(i) == ',' /* && Character.isLetter(css.charAt(i-1)) */ && i != 0) {
				css = css.substring(0, i + 1) + "\n" + css.substring(i + 1, css.length());
			}

		}
		return css;
	}

	public String curlyBraceTraveller(String cssVal) {
		StringBuilder returnBuilder=new StringBuilder();
		String requiredPath = "";
		int closeBrace;
		int cssLength = cssVal.length();
		int j = 0;
		int i = 0;
		String limit = "";
		for (i = 0; i < cssLength;) {
			// cssLength=cssVal.length();
//			System.out.println(cssVal.charAt(i));
			if (cssVal.charAt(i) == '{') {
				requiredPath = cssVal.substring(0, i);
				for (j = i; j < cssLength; j++) {
					// System.out.println(cssVal.charAt(j));
					if (cssVal.charAt(j) == '}') {
						closeBrace = j;
						limit = cssVal.substring(0, closeBrace + 1);
						if (displayAndVisisbility(limit)) {
							returnBuilder.append(commentRemover(requiredPath)+", ");
						}

						cssVal = cssVal.replace(limit, "").trim();
						cssLength = cssVal.length();
						i = 0;
						// System.out.println(limit);
						break;

					} else
						continue;
				}
			} else {
				i++;
			}

		}
		return returnBuilder.toString();
	}

	public String commentRemover(String value) {

		int p = 0;
		String returnVal = value;
		int returnValLentgth = returnVal.length();
		for (int i = 0; i < returnValLentgth; i++) {
			if (returnVal.charAt(i) == '/' && returnVal.charAt(i + 1) == '*') {
				for (p = i; p < returnValLentgth; p++) {
					if (returnVal.charAt(p) == '*' && returnVal.charAt(p + 1) == '/') {
						String remove = returnVal.substring(i, p + 2);
						returnVal = returnVal.replace(remove, "").trim();
						returnValLentgth = returnVal.length();
						break;
					}

				}
			}
		}
		
		return htmlRemover(returnVal.trim());
	}

	private boolean displayAndVisisbility(String value) {

		TestJsoupScraping jsoup = new TestJsoupScraping();
		String reg = " *visibility *: *hidden";
		String reg1 = " *display *: *none";
		Pattern p = Pattern.compile(reg);
		Pattern p1 = Pattern.compile(reg1);

		Matcher m = p.matcher(value);
		Matcher m1 = p1.matcher(value);
		if (m.find()) {
			// jsoup.seperateClassndDiv(m.group());
//			System.out.println(m.group());
			return true;
		}
		else if (m1.find()) {
			// jsoup.seperateClassndDiv(m1.group());
//			System.out.println(m1.group());
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		CssFormater css = new CssFormater();

		css.curlyBraceTraveller(".ddsmoothmenu ul li a.selected{\n" + "		background:#00aff8 !important;\n"
				+ "		color:white;\n" + "		}\n" + "\n" + " .ddsmoothmenu ul li a:hover{\n"
				+ "		background:#00aff8;\n" + "		color:white;\n" + "		}\n" + "\n"
				+ " .ddsmoothmenu ul li ul{\n" + "		position:absolute;\n" + "		left:-3000px;\n"
				+ "		display:none;\n" + "		visibility:hidden;\n" + "		}\n" + "\n" + "\n" + "\n"
				+ " .ddsmoothmenu ul li ul li{\n" + "		display:list-item;\n" + "		float:none;\n" + "		}\n"
				+ "\n" + " .ddsmoothmenu ul li ul li ul{\n" + "		top:0;\n" + "		}\n" + "");

	}

}
