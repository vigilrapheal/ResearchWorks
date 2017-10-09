package com.hirrr.crawltest.seleniumtestmaven;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Hiddenwebelements {
	public static void main(String[] args) throws InterruptedException {

		WebDriver driver = new FirefoxDriver();

		driver.manage().window().maximize();
		driver.get("http://www.capitalfirst.com/careers");

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		hiddenCheck(driver);

	}

	public static void hiddenCheck(WebDriver driver) {

		List<WebElement> number_of_displa_block = driver.findElements(By.cssSelector("div[style*='block']"));

		System.out.println("Display Block =: " + number_of_displa_block.size());
		System.out.println("========================================================");
		for (int i = 0; i < number_of_displa_block.size(); i++) {

			if (!(number_of_displa_block.get(i).getText().trim().isEmpty())) {
				System.out.println(
						"Displa Block Element = " + i + " " + number_of_displa_block.get(i).getAttribute("style").trim()
								+ " " + number_of_displa_block.get(i).getText().trim());
			} else {
				System.out.println("Displa Block Element = " + i + " "
						+ number_of_displa_block.get(i).getAttribute("style").trim());
			}
		}

		List<WebElement> number_of_visibility_hidden = driver.findElements(By.cssSelector("div[style*='hidden']"));

		System.out.println("\n\nvisibility: hidden = " + number_of_visibility_hidden.size());
		System.out.println("========================================================");
		for (int i = 0; i < number_of_visibility_hidden.size(); i++) {

			if (!(number_of_visibility_hidden.get(i).getText().trim().isEmpty())) {
				System.out.println("visibility: hidden Element = " + i + " "
						+ number_of_visibility_hidden.get(i).getAttribute("style").trim() + " "
						+ number_of_visibility_hidden.get(i).getText().trim());
			} else {
				System.out.println("visibility: hidden Element = " + i + " "
						+ number_of_visibility_hidden.get(i).getAttribute("style").trim());
			}
		}

		List<WebElement> number_of_display_none = driver.findElements(By.cssSelector("div[style*='none']"));

		System.out.println("\n\ndisplay: none = " + number_of_display_none.size());
		System.out.println("========================================================");
		for (int i = 0; i < number_of_display_none.size(); i++) {

			if (!(number_of_display_none.get(i).getText().trim().isEmpty())) {
				System.out.println("display: none Element = " + i + " "
						+ number_of_display_none.get(i).getAttribute("style").trim() + " "
						+ number_of_display_none.get(i).getText().trim());
			} else {
				System.out.println("display: none Element = " + i + " "
						+ number_of_display_none.get(i).getAttribute("style").trim());
			}
		}

		List<WebElement> number_of_hidden_input_Elements = driver.findElements(By.tagName("input"));

		System.out.println("\n\nInput Hidden Element =: " + number_of_hidden_input_Elements.size());
		System.out.println("========================================================");
		for (int i = 0; i < number_of_hidden_input_Elements.size(); i++) {

			if (number_of_hidden_input_Elements.get(i).getAttribute("type").trim().equalsIgnoreCase("hidden")) {

				if (!(number_of_hidden_input_Elements.get(i).getAttribute("value").trim().isEmpty())) {

					System.out.println("Input Hidden Element = " + i + " "
							+ number_of_hidden_input_Elements.get(i).getAttribute("value").trim());
				}
			}
		}

//		driver.quit();
	}
}