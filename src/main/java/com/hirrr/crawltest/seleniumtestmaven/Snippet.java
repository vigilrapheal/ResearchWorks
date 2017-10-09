package com.hirrr.crawltest.seleniumtestmaven;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Snippet {



	public static void main(String[]  args) {
		
		WebDriver driver = new FirefoxDriver(/* firefoxBinary, null */);
		Logger log = Logger.getLogger(SeleniumIssuescrap.class.toString());
		try {

			driver.get("http://www.rajoo.com/careers.html");
			Thread.sleep(3000);

			log.info("reached");
		} catch (Exception e) {
			log.info(e.toString());
			log.info("exception");
		}
		
		System.out.println(driver.findElement(By.tagName("head")).getAttribute("outerHTML"));
	}
}
