package com.hirrr.crawltest.seleniumtestmaven;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.webdriven.commands.GetAlert;

public class SeleniumTest {

	private static JavascriptExecutor js = null;
	private static WebDriver driver = null;

	public static void main(String[] args) throws InterruptedException {

		// String Xport = System.getProperty("lmportal.xvfb.id", ":1");
		// FirefoxBinary firefoxBinary = new FirefoxBinary();
		// firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);
		driver = new FirefoxDriver(/* firefoxBinary, null */);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		try {

			driver.get("https://www.firstnaukri.com/fnJobSearch/search");
			// wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
			Thread.sleep(5000);
			System.out.println("reached");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception");
		}
		try {
			Thread.sleep(5000);
			js = (JavascriptExecutor) driver;

		} catch (Exception e) {
			e.printStackTrace();
		}

		optionSelector("//select[@id='eo']/optgroup[@label='Popular Courses']/option",
				"alert(document.getElementById(\"eo\").children[1].children.length);");

		optionSelector("//select[@id='eo']/optgroup[@label='UG Courses']/option",
				"alert(document.getElementById(\"eo\").children[2].children.length);");

		optionSelector("//select[@id='eo']/optgroup[@label='PG Courses']/option",
				"alert(document.getElementById(\"eo\").children[3].children.length);");

	}

	private static void optionSelector(String ele, String lengthChi) throws InterruptedException {

		js.executeScript("document.getElementById(\"spanid\").style.display = \"block\"");
		Thread.sleep(2000);
		Object parentCount1 = js.executeScript(lengthChi);

		System.out.println(parentCount1);
		List<WebElement> GreatBands = new ArrayList<>();
		int parentCountint1 = Integer.parseInt(parentCount1.toString());

		for (WebElement ele1 : GreatBands) {
			System.out.println(ele1.getText());
		}

		for (int i = 0; i < parentCountint1; i++) {
			try {
				try {
					js.executeScript("document.getElementById(\"spanid\").style.display = \"block\"");
				} catch (Exception e) {
					e.printStackTrace();
				}

				GreatBands = driver.findElements(By.xpath(ele));
				GreatBands.get(i).click();
				Thread.sleep(4000);
				driver.findElement(By.xpath("//input[@type=\"submit\" and @value=\"Search\"]")).click();
				Thread.sleep(8000);
				System.out.println(i);
			} catch (Exception e) {
				try {
					GreatBands.get(i).click();
				} catch (Exception e1) {

				}
				System.out.println(e);
				System.out.println("Exception @----" + i + "th itration");
				continue;
			}
		}
	}
}
