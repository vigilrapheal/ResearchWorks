package com.hirrr.crawltest.seleniumtestmaven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssFormater {

	public static String cssBeautifier(String value) throws IOException {

		CssFormater css = new CssFormater();
//		File file = new File("/home/vigil/Desktop/output/hidden.txt");
//		BufferedReader br = new BufferedReader(new FileReader(file));
//		String value = "";
//		StringBuilder build = new StringBuilder();
//		while ((value = br.readLine()) != null) {
//			build.append(value);
//		}

		String cleanHtml = css.htmlRemover(value);
		cleanHtml = css.cssBeautify(cleanHtml);

		return cleanHtml;
	}

	private String htmlRemover(String value) {
		String regex = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		m = p.matcher(value);
		while (m.find()) {
			value = value.replace(m.group(), "");
		}
		return value.trim();
	}

	private String cssBeautify(String value) {

		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '{') {
				value = value.substring(0, i + 1) + "\n\t\t" + value.substring(i + 1, value.length());
			} else if (value.charAt(i) == '}') {
				value = value.substring(0, i - 1) + "\n" + value.substring(i, i + 1) + "\n\n"
						+ value.substring(i + 1, value.length());
			}
			else if(value.charAt(i) == ';')
			{
				value = value.substring(0, i + 1) + "\n\t\t" + value.substring(i + 1, value.length());
			}
			else if(value.charAt(i) == '/' && i!=0)
			{
				if(value.charAt(i-1)=='*') {
				value = value.substring(0, i + 1) + "\n\n" + value.substring(i + 1, value.length());
				}
			}
			else if(value.charAt(i) == ',' && Character.isLetter(value.charAt(i-1)) &&i!=0)
			{
				value = value.substring(0, i + 1) + "\n" + value.substring(i + 1, value.length());
			}

		}
		return value;
	}

}
