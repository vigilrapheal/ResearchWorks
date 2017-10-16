package com.hirrr.learner.googletranslator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author VIGIL V RAPHEAL
 *
 */
public class SiteEntryChecker {

	private static final String ANCHOR = "a";
	private static final String INPUT = "input";
	private static final String BUTTON = "button";

	public String siteEntryRectifier(String domainUrl) {

		SiteEntryChecker site = new SiteEntryChecker();
		ArrayList<String> hrefList = null;
		String url = domainUrl;
		Document document = null;
		hrefList = new ArrayList<>();

		try {
			document = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:44.0) Gecko/20100101 Firefox/44.0")
					.maxBodySize(0).timeout(60000).get();
			url = site.getResponseUrl(url);
		} catch (Exception e) {
			return "";
		}

		Elements elesA = document.select(ANCHOR);
		Elements elesButton = document.select(BUTTON);
		Elements elesInput = document.select(INPUT);

		if (elesA.isEmpty() && elesButton.isEmpty() && elesInput.isEmpty()) {
			Document doc = site.seleniumRun(url);
			elesA = doc.select(ANCHOR);
			elesButton = doc.select(BUTTON);
			elesInput = doc.select(INPUT);
		}

		HashSet<String> hrefSet = site.extractURLFromElements(elesA, elesInput, elesButton, url);

		hrefList.addAll(hrefSet);
		hrefList = site.mainMethod(url, hrefList);

		return site.sorter(hrefList, url);

	}

	private String sorter(ArrayList<String> hrefList, String url) {

		int listSize = hrefList.size();
		if (listSize < 5 && listSize != 0) {
			for (int i = 0; i < listSize; i++) {
				String returnURL = hrefList.get(i);
				if (returnURL.toUpperCase().contains("HOME") || returnURL.toUpperCase().contains("INDEX")
						|| returnURL.toUpperCase().contains("ABOUT")) {
					return returnURL;
				}
			}
			return hrefList.get(0);
		}
		return url;
	}

	private String getResponseUrl(String url) {

		Response response = null;
		String newURL = "";
		try {
			response = Jsoup.connect(url).followRedirects(true).execute();
		} catch (IOException e) {
			return url;
		}

		newURL = response.url().toString();
		int count = 0;

		for (int i = 0; i < newURL.length(); i++) {
			if (newURL.charAt(i) == '/') {
				count++;
				if (count == 3) {
					newURL = newURL.substring(0, i);
				}
			}
		}
		return newURL;
	}

	private HashSet<String> extractURLFromElements(Elements elesA, Elements elesInput, Elements elesButton,
			String url) {

		HashSet<String> hrefSet = new HashSet<>();

		hrefSet.addAll(fromAnchor(elesA, url));
		hrefSet.addAll(fromInputAndButton(elesInput, url));
		hrefSet.addAll(fromInputAndButton(elesButton, url));

		return hrefSet;
	}

	private HashSet<String> fromAnchor(Elements elesA, String url) {

		HashSet<String> hrefSet = new HashSet<>();

		for (Element element : elesA) {
			String href = element.attr("href");
			if (!href.trim().isEmpty() && !href.trim().equals(url.trim()) && !"#".equals(href)) {

				hrefSet.add(href + "::" + element.text());
			}
		}

		return hrefSet;
	}

	private HashSet<String> fromInputAndButton(Elements elesInput, String url) {

		HashSet<String> hrefSet = new HashSet<>();

		for (Element element : elesInput) {
			String type = element.attr("type");
			String value = element.attr("value");
			if (type.equals("submit") || type.equals(BUTTON)) {
				String onclick = element.attr("onclick");
				if (!onclick.trim().isEmpty() && !onclick.trim().equals(url.trim()) && !"#".equals(onclick))
					hrefSet.add(onclick + "::" + value);
			}
		}

		return hrefSet;
	}

	private boolean requiredURLChecker(String url, String returnURL) {

		return ((returnURL.toUpperCase().contains("HOME") || returnURL.toUpperCase().contains("INDEX")
				|| returnURL.toUpperCase().contains("ABOUT") || badWordCheck(returnURL) || url.compareTo(returnURL) > 0)
				&& !englishLetterRemover(returnURL));
	}

	private boolean badWordCheck(String returnURL) {

		String value = returnURL.toUpperCase();

		return (value.contains("FORGOT") || value.contains("MAILTO") || value.contains("INSTAGRAM")
				|| value.contains("TWITTER") || value.contains("VIMEO") || value.contains("YOUTUBE")
				|| value.contains("WHATSAPP") || value.contains("REGISTER") || value.contains("TEL")
				|| value.contains("JAVASCRIPT")) ? false : true;
	}

	private boolean englishLetterRemover(String returnURL) {

		String returnVal = returnURL.split("::")[1];
		String regex = "[a-zA-Z0-9]";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		m = p.matcher(returnVal);
		while (m.find()) {
			returnVal = returnVal.replace(m.group(), "");
		}
		returnVal = removeSpecialChara(returnVal);

		return !returnVal.isEmpty();
	}

	private String removeSpecialChara(String value) {

		String regex = "[-!$%^&*()_+|~=`{}\\[\\]:\";'<>?\\@\\©\\#,.\\/]";
		String returnVal = value;
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		m = p.matcher(returnVal);
		while (m.find()) {
			returnVal = returnVal.replace(m.group(), "");
		}
		returnVal = returnVal.replace("’", "").replace("–", "");
		return returnVal.trim();
	}

	private String urlCorrector(String url, String scrappedURL) {

		String returnURL = scrappedURL;
		if (!scrappedURL.trim().startsWith("http")) {
			if (!scrappedURL.startsWith("/") && !url.endsWith("/")) {
				returnURL = url + "/" + scrappedURL;
			} else {
				returnURL = url + scrappedURL;
			}

		}
		if (requiredURLChecker(url, returnURL)) {
			return returnURL;
		}

		return "";
	}

	private String onClickURLExtractor(String scrappedURL) {

		String href = "";
		String splittedUrl = "";
		String name = "";
		if (scrappedURL.toUpperCase().contains("HREF")) {
			splittedUrl = scrappedURL.split("::")[0];
			name = scrappedURL.split("::")[1];
			href = splittedUrl.split("href")[1];
			int start = 0;
			int end = 0;
			int count = 0;
			for (int i = 0; i < href.length(); i++) {
				if (href.charAt(i) == '\'' || href.charAt(i) == '\"') {
					if (count == 0) {
						count++;
						start = i;
					} else if (count == 1) {
						end = i;
						break;
					}
				}
			}
			return href.substring(start + 1, end) + "::" + name;
		}
		return "";
	}

	private Document seleniumRun(String url) {

		Document doc = new Document(url);
		WebDriver driver = new FirefoxDriver(/* firefoxBinary, null */);
		Logger log = Logger.getLogger(SiteEntryChecker.class.toString());
		try {
			driver.get(url);
			Thread.sleep(3000);
			log.info("reached");
			doc = Jsoup.parse(driver.getPageSource());
		} catch (Exception e) {
			log.info(e.toString());
			log.info("exception");
			driver.quit();
			return doc;
		} finally {
			driver.quit();
		}

		return doc;
	}

	private ArrayList<String> mainMethod(String url, ArrayList<String> arrList) {

		ArrayList<String> anchor = new ArrayList<>();
		for (int i = 0; i < arrList.size(); i++) {
			String val = arrList.get(i);
			val = onClickURLExtractor(val);
			if (val.isEmpty()) {
				val = urlCorrector(url, arrList.get(i));
			} else {
				val = urlCorrector(url, val);
			}
			if (!val.isEmpty()) {
				anchor.add(val);
			}
		}
		return anchor;
	}

}
