package com.hirrr.learner.googletranslator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author VIGIL V RAPHEAL
 *
 */
public class LanguageDetector {

	private int counter = 0;
	private int englishCounter = 0;
	private static HashSet<String> set = new HashSet<>();

	public String languageDetector(String url) throws IOException, InterruptedException {

		WebDriver driver = null;
		Document mainDoc = null;

		try {
			driver = seleniumHTMLRunner();
		} catch (NullPointerException e) {
			 return "Cannot Process";
		}

		ArrayList<String> arr = new ArrayList<>();

		mainDoc = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
				.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:44.0) Gecko/20100101 Firefox/44.0")
				.maxBodySize(0).timeout(60000).get();

		Elements eles = mainDoc.select("div");
		String string=eles.toString();
		String ret = htmlRemover(string);
		String[] splitval = ret.trim().split(" ");
		for (int i = 0; i < splitval.length; i++) {
			if (counter > 3 || englishCounter > 3 || set.size() > 2) {
				break;
			}
			getLanguage(splitval[i].trim(), driver);
		}
		arr.addAll(set);
		set.clear();
		StringBuilder strBuild = new StringBuilder();
		if (arr.size() == 1) {
			 return arr.get(0);
		} else if (arr.size() > 1) {
			for (int i = 0; i < arr.size(); i++) {
				strBuild.append(arr.get(i));
				if (i != arr.size() - 1) {
					strBuild.append("~~~");
				}
			}
			 return strBuild.toString();
		} 
		counter = 0;
		englishCounter = 0;
		
		 return "English";
	}

	private String htmlRemover(String value) {

		String returnVal = value;
		String regex = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		m = p.matcher(returnVal);
		while (m.find()) {
			returnVal = returnVal.replace(m.group(), "");
		}
		return whiteSpaceRemover(returnVal.trim());
	}

	private String whiteSpaceRemover(String value) {

		String returnVal = value;
		String regex = "\\s+";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		m = p.matcher(returnVal);
		while (m.find()) {
			returnVal = returnVal.replace(m.group(), " ");
		}
		return englishLetterRemover(returnVal.trim());

	}

	private String englishLetterRemover(String value) {

		String returnVal = value;
		String regex = "[a-zA-Z0-9]";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		m = p.matcher(returnVal);
		while (m.find()) {
			returnVal = returnVal.replace(m.group(), "");
		}
		return removeSpecialChara(returnVal.trim());

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

	private WebDriver seleniumHTMLRunner() {
		WebDriver driver = new FirefoxDriver();
		try {
			driver.get("https://translate.google.com/");
			Thread.sleep(3000);
		} catch (Exception e) {
			return null;
		}

		return driver;
	}

	private void getLanguage(String text, WebDriver driver) throws InterruptedException {
		if (!text.trim().isEmpty() && !(text.contains("\\") || text.contains("*") || text.contains("&"))) {
			driver.findElement(By.id("source")).sendKeys(text);
			Thread.sleep(3000);

			WebElement ele1 = driver.findElement(
					By.xpath("/html/body/div[3]/div[2]/form/div[2]/div/div/div[1]/div[1]/div[1]/div[1]/div[5]"));

			if (ele1.getText().trim().toUpperCase().contains("LANGUAGE")) {
				driver.navigate().refresh();
				Thread.sleep(3000);
				ele1 = driver.findElement(
						By.xpath("/html/body/div[3]/div[2]/form/div[2]/div/div/div[1]/div[1]/div[1]/div[1]/div[5]"));
			}

			if (!ele1.getText().toUpperCase().contains("ENGLISH")) {
				counter++;
				set.add(ele1.getText().split("-")[0].trim());
			} else {
				englishCounter++;
			}
			Thread.sleep(3000);
			try {
				driver.findElement(By.id("gt-clear")).click();
				Thread.sleep(4000);
			} catch (Exception e) {
				Thread.sleep(4000);
			}
		}
	}
}